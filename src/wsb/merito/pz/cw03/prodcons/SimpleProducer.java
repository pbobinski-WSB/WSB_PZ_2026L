package wsb.merito.pz.cw03.prodcons;

import java.util.concurrent.BlockingQueue;

/**
 * Prosty producent. Produkuje ciągi znaków.
 * @author kmi
 */
public class SimpleProducer implements Producer {

	/**
	 * Kolejka, na której odbędzie się synchronizacja.
	 */
	private BlockingQueue<String> bq = null;

	/**
	 * Licznik kolejno wyprodukowanych ciągów znaków.
	 */
	private int l = 0;

	/**
	 * Konstrukcja prostego producenta.
	 * @param bq kolejka, na której odbędzie się synchronizacja.
	 */
	public SimpleProducer(BlockingQueue<String> bq) {
		this.bq = bq;
	}

	@Override
	public void run() {
		while (true) {
			produce();
		}
	}

	@Override
	public void produce() {
		try {
			//Thread.yield(); 
			sleeep(1000);
			String s = "String " + l++;
			System.out.println("produced: [" + s + "]");
			bq.put(s);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Zatrzymuje działanie wątku, który wywołał tę metodę na losowy czas w
	 * zakresie 0..t milisekund.
	 * @param t maksymalny czas, na który nastąpi zatrzymanie wątku.
	 */
	private void sleeep(long t) {
		try {			
			Thread.sleep((long) (t * Math.random()));
		} catch (InterruptedException ie) {
		}
	}

}
