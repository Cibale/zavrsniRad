package hr.fer.zemris.prviProgrami;

public final class MySingleton {
	private static MySingleton instanca;

	private MySingleton() {
		// privatni konstruktor
	}

	public static MySingleton getInstance() {
		if (instanca == null) {
			instanca = new MySingleton();
		}
		return instanca;
	}
}
