package hr.fer.zemris.prviProgrami;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class Primjer3 {
	public static void main(String[] args) {
		// inicijalizacija SparkContext-a
		SparkConf conf = new SparkConf().setMaster("local")
				.setAppName("Brojanje rijeci");
		JavaSparkContext sc = new JavaSparkContext(conf);

		// učitavanje podataka
		JavaRDD<String> ulaz = sc.textFile("access_log.txt");

		// transformacije
		JavaRDD<String> burze = ulaz.filter(redak -> redak.contains("burza"));
		JavaRDD<String> indexi = ulaz.filter(redak -> redak.contains("index"));
		JavaRDD<String> burzeUnijaIndexi = burze.union(indexi);

		// akcije
		long brojLinija = burzeUnijaIndexi.count();
		long ukupanBrojLinija = ulaz.count();
		System.out.printf(
				"Broj linija koje sadrže riječ 'burza' ili 'index' je: %d, odnosno %f%%.\n",
				brojLinija, (double) 100 * brojLinija / ukupanBrojLinija);
		System.out.printf(
				"Prva linija koja sadrži riječ 'burza' ili 'index' je: %s\n",
				burzeUnijaIndexi.first());
		sc.close();
	}
}
