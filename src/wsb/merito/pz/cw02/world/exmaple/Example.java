package wsb.merito.pz.cw02.world.exmaple;

import wsb.merito.pz.cw02.world.dao.CountryDao;
import wsb.merito.pz.cw02.world.dao.InMemoryWorldDao;
import wsb.merito.pz.cw02.world.domain.Country;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;



/**
 * @author Binnur Kurt (binnur.kurt@gmail.com)
 */
public class Example {

    public static void main(String[] args) {

        //Find and print all countries
        CountryDao countryDao = InMemoryWorldDao.getInstance();
        //declarative way
        System.out.println("All countries declarative");
        countryDao.findAllCountries().forEach(System.out::println);
        //imperative way
        System.out.println("All countries imperative");
        for (Country c : countryDao.findAllCountries()) {
            System.out.println(c);
        }

        //Find and print countries whose name starts with the letter M. Write down the name, code and continent
        //declarative way
        System.out.println("M... named countries declarative");
        countryDao.findAllCountries()
                .stream()
                .filter(c -> c.getName().startsWith("M"))
                .map(c -> Arrays.asList(c.getName(), c.getCode(), c.getContinent()))
                .forEach(System.out::println);
        //imperative way
        System.out.println("M... named countries imperative");
        for (Country c : countryDao.findAllCountries()) {
            if (c.getName().startsWith("M")) {
                List<String> n = new ArrayList<>();
                n.add(c.getName());
                n.add(c.getCode());
                n.add(c.getContinent());
                System.out.println(n);
            }
        }
    }

}
