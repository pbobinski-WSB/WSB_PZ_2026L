package wsb.merito.pz.cw03.prodcons;

import java.util.concurrent.BlockingQueue;

/**
 * Prosty konsument. Konsumuje ciągi znaków.
 * @author kmi
 */
public class SimpleConsumer implements Consumer {

	/**
	 * Kolejka, na której odbędzie się synchronizacja.
	 */
	private BlockingQueue<String> bq = null;

	/**
	 * Konstrukcja prostego konsumenta.
	 * @param bq kolejka, na której odbędzie się synchronizacja.
	 */
	public SimpleConsumer(BlockingQueue<String> bq) {
		this.bq = bq;
	}

	@Override
	public void run() {
		while (true) {
			consume();
		}
	}

	@Override
	public void consume() {
		try {
			// Thread.yield();
			sleeep(1000);
			String s = bq.take();
			System.out.println("consumed: [" + s + "]");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Zatrzymuje działanie wątku, który wywołał tę metodę na losowy czas
	 * w zakresie 0..t milisekund.
	 * @param t maksymalny czas, na który nastąpi zatrzymanie wątku.
	 */
	private void sleeep(long t) {
		try {
			Thread.sleep((long) (t * Math.random()));
		} catch (InterruptedException ie) {
		}
	}
}
