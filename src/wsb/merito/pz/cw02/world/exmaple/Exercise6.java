package wsb.merito.pz.cw02.world.exmaple;


import wsb.merito.pz.cw02.world.dao.InMemoryWorldDao;
import wsb.merito.pz.cw02.world.dao.WorldDao;
import wsb.merito.pz.cw02.world.domain.Country;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;

import static java.lang.System.out;
import static java.util.Comparator.comparingDouble;

/**
 * @author Binnur Kurt (binnur.kurt@gmail.com)
 */
public class Exercise6 {

    public static void main(String[] args) {
        // Sort the countries by their population densities in descending order ignoring zero population countries
        WorldDao worldDao = InMemoryWorldDao.getInstance();
        Collection<Country> countries = worldDao.findAllCountries();
        Comparator<Country> populationDensityComparator = comparingDouble(country -> country.getPopulation() / country.getSurfaceArea());
        Predicate<Country> livesNobody = country -> country.getPopulation() == 0L;
        countries.stream().filter(livesNobody.negate()).sorted(populationDensityComparator.reversed())
                .forEach(out::println);
    }

}
