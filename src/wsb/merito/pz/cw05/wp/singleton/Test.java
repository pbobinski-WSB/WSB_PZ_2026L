package wsb.merito.pz.cw05.wp.singleton;

public class Test {

    public static void main(String[] args) {



        SingletonClassDemo scd = SingletonClassDemo.getInstance();
        System.out.println(scd);


        SingletonEnum se = SingletonEnum.SINGLETON_ENUM_INSTANCE;
        System.out.println(se);


    }
}
