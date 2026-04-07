package wsb.merito.pz.cw03.prodcons;



import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/*
 * Eksperymenty:
 * 1. zmieniaj parametr konstruktora SimpleProducerConsumer
 * 2. odkomentuj Thread.yield() w klasach SimpleProducer i SimpleConsumer 
 *    (w jednej albo w obu jednocześnie)
 * 3. odkomentuj wywołanie sleeep() w klasach SimpleProducer i SimpleConsumer 
 *    (w jednej albo w obu jednocześnie)
 * 4. zmieniaj parametry sleeep()
 * 5. zmień kolejność wykonania operacji w konstruktorze SimpleProducerConsumer 
 *    (co pierwsze ? konsument ? producent ?)
 * 6. wszelkie kombinacje powyższych
 */
public class SimpleProducerConsumer {
	
	/**
	 * Konstruktor tworzy obiekty producenta i konsumenta, oraz uruchamia
	 * wątki napędzające produkcję i konsumpcję.
	 * 
	 * @param n długość kolejki przechowującej produkty.
	 */
	public SimpleProducerConsumer(int n) {
		BlockingQueue<String> abq = new ArrayBlockingQueue<>(n);
		
		Consumer c = new SimpleConsumer(abq);
		new Thread(c).start();
		Producer p = new SimpleProducer(abq);
		new Thread(p).start();
	}

	/**
	 * @param args nie używane.
	 */
	public static void main(String[] args) {
		new SimpleProducerConsumer(10);
	}
}
