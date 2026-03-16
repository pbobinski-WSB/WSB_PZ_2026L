package wsb.merito.pz.cw02.world.exmaple;

import wsb.merito.pz.cw02.world.dao.*;
import wsb.merito.pz.cw02.world.domain.*;

import java.util.Objects;
import java.util.Optional;

import static java.lang.System.out;
import static java.util.Comparator.comparing;

/**
 * @author Binnur Kurt (binnur.kurt@gmail.com)
 */
public class Exercise3 {

    public static void main(String[] args) {
        // Find the most populated capital
        CountryDao countryDao = InMemoryWorldDao.getInstance();
        CityDao cityDao = InMemoryWorldDao.getInstance();
        Optional<City> capital = countryDao.findAllCountries()
                .stream()
                .map(Country::getCapital)
                .map(cityDao::findCityById)
                .filter(Objects::nonNull)
                .max(comparing(City::getPopulation));
        capital.ifPresent(out::println);
    }

}
