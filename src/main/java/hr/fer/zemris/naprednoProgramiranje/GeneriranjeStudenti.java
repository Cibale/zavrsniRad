package hr.fer.zemris.naprednoProgramiranje;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

/**
 * Generira datoteku studenti-ocjene.txt. Svaki student ima vrijednosti: JMBAG,
 * sifraPredmeta, ocjenaIzPredmeta. Vrijednosti su odvojene tabom (\t).
 *
 * @author mmatak
 *
 */
public class GeneriranjeStudenti {
	private final static String IZLAZNA_DATOTEKA = "studenti-ocjene.txt";

	private final static int BROJ_RAZLICITH_STUDENATA = 500;

	private final static int MIN_PREDMETA_PO_STUDENTU = 3;
	private final static int MAX_PREDMETA_PO_STUDENTU = 30;

	private final static int SIFRA_PREDMETA_DG = 777;
	private final static int SIFRA_PREDMETA_GG = 888;

	private final static String JMBAG_PREFIX = "00";
	private final static int JMBAG_MIN_VALUE = 36477876;

	private final static int MIN_OCJENA = 1;
	private final static int MAX_OCJENA = 5;

	public static void main(String[] args) {
		Random rand = new Random();
		try (BufferedWriter output = Files.newBufferedWriter(
				Paths.get(IZLAZNA_DATOTEKA), StandardCharsets.UTF_8)) {
			for (int i = 0; i < BROJ_RAZLICITH_STUDENATA; i++) {
				int jmbagPostfix = JMBAG_MIN_VALUE + i;
				String jmbag = JMBAG_PREFIX + jmbagPostfix;
				int brojPredmeta = MIN_PREDMETA_PO_STUDENTU
						+ rand.nextInt(MAX_PREDMETA_PO_STUDENTU
								- MIN_PREDMETA_PO_STUDENTU + 1);
				Student student = new Student(jmbag);
				int brojDodanihPredmeta = 0;
				do {
					String sifraPredmeta = String
							.valueOf(SIFRA_PREDMETA_DG + rand.nextInt(
									SIFRA_PREDMETA_GG - SIFRA_PREDMETA_DG) + 1);
					int ocjena = MIN_OCJENA
							+ rand.nextInt(MAX_OCJENA - MIN_OCJENA + 1);
					if (student.dodajPredmet(sifraPredmeta, ocjena)) {
						brojDodanihPredmeta++;
					}
				} while (brojDodanihPredmeta < brojPredmeta);
				output.write(student.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		System.out.println(
				"Datoteka " + IZLAZNA_DATOTEKA + " uspjesno kreirana!");
	}
}
