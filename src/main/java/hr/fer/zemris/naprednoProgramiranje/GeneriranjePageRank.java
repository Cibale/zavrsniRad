package hr.fer.zemris.naprednoProgramiranje;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GeneriranjePageRank {
	public static void main(String[] args) throws IOException {
		Map<String, Set<String>> pageRank = new HashMap<>();
		List<String> linkovi = new ArrayList<>();
		for (String line : Files.readAllLines(Paths.get("urls.txt"))) {
			linkovi.add(line.trim());
		}
		Random rand = new Random();
		for (int i = 0; i < linkovi.size(); i++) {
			int numOfNeighbours = 1 + rand.nextInt(10);
			String link = linkovi.get(i);
			pageRank.put(link, new HashSet<>());
			Set<String> susjedi = pageRank.get(link);
			for (int j = 0; j < numOfNeighbours; j++) {
				String neighbour = linkovi.get(rand.nextInt(linkovi.size()));
				if (neighbour.equals(linkovi.get(i))) {
					j--;
					continue;
				}
				if (susjedi.contains(neighbour)) {
					j--;
					continue;
				}
				susjedi.add(neighbour);
			}
		}
		try (BufferedWriter output = Files.newBufferedWriter(
				Paths.get("pageRankInput.txt"), StandardCharsets.UTF_8)) {
			for (Map.Entry<String, Set<String>> entry : pageRank.entrySet()) {
				for (String link : entry.getValue()) {
					output.write(entry.getKey() + " " + link + "\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Datoteka uspjesno kreirana.");
	}
}
