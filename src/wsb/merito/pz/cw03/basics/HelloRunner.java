package wsb.merito.pz.cw03.basics;

/**
 * Prosty wątek, wypisujący kilka razy tekst na standardowe wyjście.
 * @author kmi
 */
class HelloRunner implements Runnable {
	private final int ileRazy;
	
	HelloRunner(int ileRazy) {
		this.ileRazy = ileRazy;
	}
	
	public void run() {
		for (int i = 0; i < ileRazy; i++) {// wypisz kilka razy
			System.out.println("Z wątku: " + i);
		}
		System.out.println("koniec wątku roboczego");
	}
}