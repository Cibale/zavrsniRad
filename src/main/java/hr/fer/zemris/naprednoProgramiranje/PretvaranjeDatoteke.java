package hr.fer.zemris.naprednoProgramiranje;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.google.common.io.Files;

public class PretvaranjeDatoteke {

	public static void main(String[] args) throws IOException {
		Map<String, HashMap<String, String>> outputMapa = new HashMap<>();
		for (String line : Files.readLines(Paths.get("outputTomo.txt").toFile(),
				StandardCharsets.UTF_8)) {
			String[] dijelovi = line.split("=");
			String znak = dijelovi[0];
			String[] vrijednosti = dijelovi[1]
					.substring(1, dijelovi[1].length() - 1).split(",");
			if (outputMapa.get(znak) == null) {
				outputMapa.put(znak, new HashMap<>());
			}
			outputMapa.get(znak).put(vrijednosti[0], vrijednosti[1]);
		}
		StringBuilder buff = new StringBuilder();
		for (Map.Entry<String, HashMap<String, String>> entry : outputMapa
				.entrySet()) {
			buff.append(entry.getKey().toString()).append("x=");
			for (Map.Entry<String, String> innerEntry : entry.getValue()
					.entrySet()) {
				buff.append(innerEntry.getKey()).append(",");
			}
			buff.deleteCharAt(buff.toString().length() - 1);
			buff.append("\n");
			buff.append(entry.getKey().toString()).append("y=");
			for (Map.Entry<String, String> innerEntry : entry.getValue()
					.entrySet()) {
				buff.append(innerEntry.getValue()).append(",");
			}
			buff.deleteCharAt(buff.toString().length() - 1);
			buff.append("\n\n");
		}
		Files.write(buff.toString().getBytes(),
				Paths.get("tomoFormattedOutput.txt").toFile());
	}
}
