package wsb.merito.pz.cw03.prodcons;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Producent - konsument, wersja natywna z wykorzystaniem wait i notify.
 * @author kmi
 */
public class NativeProdCons {

    AtomicInteger pudełkoNaProdukt = new AtomicInteger(0);

    NativeProdCons() {
    	 new Thread(new Runnable() {
             public void run() {
                 while (true) {
                     konsumuj();
                 }
             }
         }).start();
    	
         new Thread(new Runnable() {
            public void run() {
                while (true) {
                    produkuj();
                }
            }
        }).start();
    }

    synchronized void konsumuj() {
    	while (pudełkoNaProdukt.get() == 0) {//dopóki pudełko jest puste
    		try {
    			wait();// czekamy na wyprodukowanie
    		} catch (InterruptedException ie) {
    			ie.printStackTrace();
    		}
    	}
        int produkt = pudełkoNaProdukt.getAndSet(0);//opróżniamy pudełko
        System.out.print("Konsumuję " + produkt + "... ");
        sleeep();// konsumujemy
        System.out.println("...skonsumowałem");
        notify();
    }

    synchronized void produkuj() {
    	while (pudełkoNaProdukt.get() != 0) {//dopóki pudełko niepuste
    		try {
    			wait();// czekamy na skonsumowanie
    		} catch (InterruptedException ie) {
    			ie.printStackTrace();
    		}
    	}
    	int produkt = 1 + (int) (Math.random() * 100);
    	System.out.print("Produkuję " + produkt + "... ");
        sleeep();// produkujemy
        System.out.println("...wyprodukowałem");
    	pudełkoNaProdukt.set(produkt);//wkładamy do pudełka
        notify();
    }
    
    void sleeep() {
    	try {
    		Thread.sleep((long) (50 + 500 * Math.random()));
    	} catch (InterruptedException ie) {
    		ie.printStackTrace();
    	}
    }

    public static void main(String... qrgs) {
        new NativeProdCons();
    }
}