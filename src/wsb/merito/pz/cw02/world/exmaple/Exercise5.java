package wsb.merito.pz.cw02.world.exmaple;

import wsb.merito.pz.cw02.world.dao.*;
import wsb.merito.pz.cw02.world.domain.*;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import static java.lang.String.format;
import static java.lang.System.out;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

/**
 * @author Binnur Kurt (binnur.kurt@gmail.com)
 */
public class Exercise5 {

    public static void main(String[] args) {
        // Sort the countries by number of cities in descending order
        CountryDao countryDao = InMemoryWorldDao.getInstance();
        Comparator<Country> sortByNumOfCities = comparing(country -> country.getCities().size());
        Predicate<Country> countriesHavingNoCities = country -> country.getCities().isEmpty();
        List<Country> countries = countryDao.findAllCountries()
                .stream()
                .filter(countriesHavingNoCities.negate())
                .sorted(sortByNumOfCities.reversed())
                .toList();
        countries.forEach(country -> out.printf("%38s %3d%n", country.getName(), country.getCities().size()));
    }

}
