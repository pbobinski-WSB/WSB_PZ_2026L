package wsb.ticr.unittests;

public class OrderService {

	public void doPayment() {
		try {
			Thread.sleep(1000);// 10 seconds
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void printShippingLabel() {
		try {
			Thread.sleep(2000);// 20 seconds
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
