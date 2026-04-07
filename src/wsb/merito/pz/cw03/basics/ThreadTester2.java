/**
 * 
 */
package wsb.merito.pz.cw03.basics;

/**
 * Test wątku roboczego - wersja z dziedziczeniem.
 * @author kmi
 */
public class ThreadTester2 extends Thread {
	/**
	 * Dana w klasie. 
	 */
	private static final int N = 5;
	
	/**
	 * Dana w obiekcie (i wątku)
	 */
	private int i;
	 
	public void run() {
		for (i = 0; i < N; i++) {
			System.out.println("z run " + i);
		}
		System.out.println("koniec run");
	}
	 
	public static void main(String args[]) {
		Thread t = new ThreadTester2();
		t.start();
		for (int k = 0; k < N; k++) {
			System.out.println("z main " + k);
		}
		System.out.println("koniec main");
	}
}
