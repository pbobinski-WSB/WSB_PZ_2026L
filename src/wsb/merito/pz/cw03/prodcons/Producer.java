package wsb.merito.pz.cw03.prodcons;

/**
 * Interfejs wszelkich producentów w problemie <i>producent-konsument</i>.
 * @author kmi
 */
public interface Producer extends Runnable {
	/**
	 * Metoda wołana w celu wyprodukowania <i>produktu</i> tego producenta.
	 */
	public void produce();
}
