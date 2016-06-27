package hr.fer.zemris.naprednoProgramiranje;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.google.common.collect.Iterables;

import scala.Tuple2;

/**
 * Program računa rang pojedine stranice. Očekuje se da svaki redak ulazne
 * datoteke sadrži samo 2 stranice "StranicaA StranicaB", a taj zapis
 * predstavlja da je StranicaB susjed od StranicaA.
 *
 * @author mmatak
 *
 */
public class AlgoritamPageRank {
	private static final String ULAZNA_DATOTEKA = "pageRankInput.txt";
	private static final int BROJ_ITERACIJA = 10;

	public static void main(String[] args) {
		// Inicijalizacija SparkContext-a.
		SparkConf conf = new SparkConf().setMaster("local")
				.setAppName("Brojanje rijeci");
		JavaSparkContext sc = new JavaSparkContext(conf);

		// Učitavanje podataka.
		JavaRDD<String> ulaz = sc.textFile(ULAZNA_DATOTEKA);

		// Pročitaj sve ulazne URL-e i inicijaliziraj njihove susjede.
		JavaPairRDD<String, Iterable<String>> linkovi = ulaz
				.mapToPair(redak -> {
					String[] elementi = redak.split("\\s+");
					return new Tuple2<String, String>(elementi[0], elementi[1]);
				}).distinct().groupByKey().cache();
		// Svako ime susjeda zamijeni s 1.0 i vrati novi skup podataka.
		JavaPairRDD<String, Double> rangovi = linkovi.mapValues(value -> 1.0);

		// iteracija 2. i 3. koraka algoritma
		for (int i = 0; i < BROJ_ITERACIJA; i++) {
			// za svaki link izracunaj njegovu doprinos drugim linkovima
			JavaPairRDD<String, Double> doprinosi = linkovi.join(rangovi)
					.values().flatMapToPair(linkoviRang -> {
						int brojSusjeda = Iterables.size(linkoviRang._1);
						List<Tuple2<String, Double>> rezultati = new ArrayList<Tuple2<String, Double>>();
						for (String susjed : linkoviRang._1) {
							rezultati.add(new Tuple2<String, Double>(susjed,
									linkoviRang._2 / brojSusjeda));
						}
						return rezultati;
					});
			rangovi = doprinosi.reduceByKey((v1, v2) -> v1 + v2)
					.mapValues(suma -> 0.15 + suma * 0.85);
		}
		// spremi u memoriju
		List<Tuple2<String, Double>> rangoviFinal = rangovi.collect();
		// ispis
		for (Tuple2<String, Double> entry : rangoviFinal) {
			System.out.printf("Stranica %s ima rang %.2f\n", entry._1,
					entry._2);
		}
		sc.close();
	}
}
