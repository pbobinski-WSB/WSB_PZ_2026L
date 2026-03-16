package wsb.merito.pz.cw02.world.exmaple;



import wsb.merito.pz.cw02.world.dao.CountryDao;
import wsb.merito.pz.cw02.world.dao.InMemoryWorldDao;
import wsb.merito.pz.cw02.world.domain.City;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;



public class Exercise1 {

    public static void main(String[] args) {
        // Find the highest populated city of each country
        CountryDao countryDao = InMemoryWorldDao.getInstance();
        List<City> highPopulatedCitiesOfCountries = countryDao.findAllCountries()
                .stream()
                .map(country -> country.getCities().stream().max(Comparator.comparing(City::getPopulation)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        highPopulatedCitiesOfCountries.forEach(System.out::println);
    }

}
