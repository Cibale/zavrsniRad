package hr.fer.zemris.naprednoProgramiranje;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Student {
	private String jmbag;
	private Map<String, Integer> predmetiOcjene = new HashMap<>();

	public Student(String jmbag) {
		this.jmbag = jmbag;
	}

	/**
	 * Vraca true ako je predmet uspjesno dodan (za njega vec ne postoji
	 * ocjena), false inace.
	 * 
	 * @param predmet
	 * @param ocjena
	 * @return
	 */
	public boolean dodajPredmet(String predmet, Integer ocjena) {
		if (predmetiOcjene.containsKey(predmet)) {
			return false;
		} else {
			predmetiOcjene.put(predmet, ocjena);
			return true;
		}
	}

	@Override
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (Entry<String, Integer> entry : predmetiOcjene.entrySet()) {
			buff.append(jmbag).append('\t').append(entry.getKey()).append('\t')
					.append(entry.getValue()).append('\n');
		}
		return buff.toString();
	}
}
