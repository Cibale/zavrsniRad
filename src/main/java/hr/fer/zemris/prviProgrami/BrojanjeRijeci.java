/**
 *
 */
package hr.fer.zemris.prviProgrami;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

/**
 * Razred ima svrhu prikazati osnovnu funkcionalnost Spark tehnologije na
 * primjeru prebrojavanja riječi u tekstualnoj datoteci. Kao rezultat program će
 * zapisati u tekstualnu datoteku koja riječ se koliko puta ponavlja. Očekuje se
 * dva argumenta kroz naredbeni redak, a to su putanja do tekstualne datoteke u
 * kojoj treba izbrojati riječi i putanja do direktorija u koji će se zapisati
 * rezultat.
 *
 * @author mmatak
 *
 */
public class BrojanjeRijeci {
	/**
	 * Metoda koja se pokrene kada se pokrene program. Očekuje putanju do
	 * datoteke s riječima i putanju do direktorija gdje će zapisati rezultat
	 * izvođenja programa.
	 *
	 * @param args
	 *            Argumenti naredbenog retka.
	 */
	@SuppressWarnings("serial")
	public static void main(String[] args) {
		String ulaznaDatoteka = args[0];
		String izlazniDirektorij = args[1];

		// inicijalizacija SparkContext-a
		SparkConf conf = new SparkConf().setMaster("local")
				.setAppName("Brojanje rijeci");
		JavaSparkContext sc = new JavaSparkContext(conf);

		// učitavanje podataka
		JavaRDD<String> ulaz = sc.textFile(ulaznaDatoteka);

		// razmak se koristi da bi razdvojio dvije riječi
		JavaRDD<String> rijeci = ulaz
				.flatMap(redak -> Arrays.asList(redak.split(" ")));

		// transformiraj u parove (rijec,1) i broji
		JavaPairRDD<String, Integer> brojRijeci = rijeci
				.mapToPair(rijec -> new Tuple2<String, Integer>(rijec, 1))
				.reduceByKey((x, y) -> x + y);

		// spremi rezultat u izlaznu datoteku
		brojRijeci.saveAsTextFile(izlazniDirektorij);
		// zatvori "tunel" odnosno SparkContext
		sc.close();
	}
}
