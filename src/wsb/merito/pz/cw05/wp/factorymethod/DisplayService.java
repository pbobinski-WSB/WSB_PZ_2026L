package wsb.merito.pz.cw05.wp.factorymethod;

public abstract class DisplayService {
	
	public void display(){
		XMLParser parser = getParser();
		String msg = parser.parse();
		System.out.println(msg);
	}
	
	protected abstract XMLParser getParser();

}
