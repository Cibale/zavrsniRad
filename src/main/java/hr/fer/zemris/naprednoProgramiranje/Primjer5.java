package hr.fer.zemris.naprednoProgramiranje;

import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;

import scala.Tuple2;

public class Primjer5 {
	public static void main(String[] args) {
		// Inicijalizacija SparkContext-a.
		SparkConf conf = new SparkConf().setMaster("local")
				.setAppName("Razasiljane varijable");
		try (JavaSparkContext sc = new JavaSparkContext(conf)) {
			// inicijalizacija naselja
			JavaPairRDD<String, String> naseljaRDD = sc
					.textFile("tblNaselja.txt").mapToPair(redakTablice -> {
						String[] redak = redakTablice.split(",");
						return new Tuple2<String, String>(redak[0], redak[1]);
					});
			// odasiljanje read only varijable "naselja" kroz grozd
			final Broadcast<Map<String, String>> naselja = sc
					.broadcast(naseljaRDD.collectAsMap());

			// prijave
			JavaPairRDD<String, String> prijaveRDD = sc
					.textFile("tblPrijave.txt").mapToPair(redakTablice -> {
						String[] redak = redakTablice.split(",");
						return new Tuple2<String, String>(redak[0], redak[1]);
					});
			JavaPairRDD<String, Tuple2<String, String>> imenovanePrijave = prijaveRDD
					.mapToPair(
							prijava -> new Tuple2<String, Tuple2<String, String>>(
									prijava._1,
									new Tuple2<String, String>(prijava._2,
											naselja.getValue()
													.get(prijava._2))));
			for (Tuple2<String, Tuple2<String, String>> element : imenovanePrijave
					.collect()) {
				System.out.printf("Id prijave: %s \t Ime naselja: %s\n",
						element._1, element._2._2);
			}
		}
	}
}
