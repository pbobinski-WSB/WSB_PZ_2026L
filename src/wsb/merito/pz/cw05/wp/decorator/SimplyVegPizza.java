package wsb.merito.pz.cw05.wp.decorator;

public class SimplyVegPizza implements Pizza{

	@Override
	public String getDesc() {
		return "SimplyVegPizza (230)";
	}
	
	@Override
	public double getPrice() {
		return 230;
	}


}
