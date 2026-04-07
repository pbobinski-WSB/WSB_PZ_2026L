package wsb.merito.pz.cw03.basics;

/**
 * Test wątku roboczego - wersja z implementacją interfejsu. 
 * @author kmi
 */
public class ThreadTester {
	public static void main(String args[]) {
		final int ileRazy = 5;
		HelloRunner r = new HelloRunner(ileRazy);
		
		Thread t = new Thread(r);
		t.start();
		
		for (int i = 0; i < ileRazy; i++) {
			System.out.println("z main " + i);
		}
		System.out.println("koniec main");
	}
}
