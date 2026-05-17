package wsb.merito.pz.datafaker;

import net.datafaker.Beer;
import net.datafaker.Faker;

import java.util.*;

public class BasicTests {


    public static void main(String[] args) {
        Faker faker1 = new Faker(new Locale("pl"));
        Faker faker2 = new Faker(new Locale("pt"));

        List<Faker> fakers = Arrays.asList(faker1, faker2);

        for (int i = 0; i < 10; i++) {
            Faker randomFaker = new Faker().options().nextElement(fakers);
            System.out.println("a. "+randomFaker.address().fullAddress());
            System.out.println("b. "+randomFaker.starWars().planets());
            System.out.println("c. "+beerToString(randomFaker.beer()));
            System.out.println("d. "+randomFaker.chuckNorris().fact());
            System.out.println("e. "+randomFaker.yoda().quote());



        }
    }

    private static String beerToString(Beer beer) {
        StringBuilder sb = new StringBuilder();
        sb.append(beer.name());
        sb.append(";");
        sb.append(beer.style());
        sb.append(";");
        sb.append(beer.hop());
        sb.append(";");
        sb.append(beer.malt());
        sb.append(";");
        sb.append(beer.yeast());
        sb.append(";");
        return sb.toString();
    }

}
