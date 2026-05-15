package wsb.merito.pz.cw05.wp.singleton;

public enum SingletonEnum {

	SINGLETON_ENUM_INSTANCE;

	@Override
	public String toString() {
		return "SingletonEnum{}";
	}
}

// The singleton instance can be accessed via "SingletonEnum.INSTANCE".