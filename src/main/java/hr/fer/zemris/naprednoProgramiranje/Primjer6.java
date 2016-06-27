package hr.fer.zemris.naprednoProgramiranje;

import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class Primjer6 {
	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setMaster("local")
				.setAppName("Koristenje akumulatora");
		try (JavaSparkContext sc = new JavaSparkContext(conf)) {
			// inicijalizacija naselja
			JavaRDD<String> zapisi = sc.textFile("log.txt");

			// inicijalizacija akumulatora
			Accumulator<Integer> valjaniZapisi = sc.accumulator(0);
			Accumulator<Integer> neValjaniZapisi = sc.accumulator(0);

			// obrada podataka
			for (String redak : zapisi.collect()) {
				if (redak.contains("error")) {
					neValjaniZapisi.add(1);
				} else {
					valjaniZapisi.add(1);
				}
			}
			// ispis rezultata
			System.out.printf("Valjanih zapisa ima %d\n",
					valjaniZapisi.value());
			System.out.printf("Nevaljanih zapisa ima %d\n",
					neValjaniZapisi.value());
		}
	}
}
