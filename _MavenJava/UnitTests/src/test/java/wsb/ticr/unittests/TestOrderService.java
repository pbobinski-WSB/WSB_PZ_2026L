package wsb.ticr.unittests;

import org.junit.jupiter.api.Test;


import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;


public class TestOrderService {

	@Test
	public void doPaymentNotExceed15Seconds() {
		OrderService orderService = new OrderService();
		assertTimeout(ofSeconds(15), () -> {
			// This method runs in 10 seconds
			orderService.doPayment();
		});
	}

	//@Disabled
	@Test
	public void doPaymentExceed5Seconds() {
		OrderService orderService = new OrderService();
		assertTimeout(ofSeconds(5), () -> {
			// This method runs in 10 seconds
			orderService.doPayment();
		} , "The doPayment method take more than 5 seconds");
	}

	//@Disabled
	@Test
	public void printShippingLabelExceeded15SecondsWithPreemptiveTermination() {
		OrderService orderService = new OrderService();
		assertTimeoutPreemptively(ofSeconds(15), () -> {
			// This method takes 20 seconds to run
			orderService.printShippingLabel();
		} , () -> "The printShippingLabel method took more than 15 seconds and was aborted.");
	}
}
