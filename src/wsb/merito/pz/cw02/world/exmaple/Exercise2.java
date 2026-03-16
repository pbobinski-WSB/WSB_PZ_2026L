package wsb.merito.pz.cw02.world.exmaple;

import wsb.merito.pz.cw02.world.dao.CountryDao;
import wsb.merito.pz.cw02.world.dao.InMemoryWorldDao;
import wsb.merito.pz.cw02.world.domain.City;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;




public class Exercise2 {

    public static void main(String[] args) {
        // Find the most populated city of each continent
        CountryDao countryDao = InMemoryWorldDao.getInstance();
        final BiConsumer<String, Optional<City>> printEntry =
                (k,v) -> {
                        City city = v.orElseGet(City::new);
                        System.out.println(k + ": City [ name= " + city.getName() + ", population= " + city.getPopulation() + " ]");
                };
        Collector<City, ?, Map<String, Optional<City>>> groupingHighPopulatedCitiesByContinent = Collectors.groupingBy(city -> countryDao.findCountryByCode(city.getCountryCode()).getContinent(), Collectors.maxBy(Comparator.comparing(City::getPopulation)));
        Map<String, Optional<City>> highPopulatedCitiesByContinent = countryDao.findAllCountries()
                .stream()
                .flatMap(country -> country.getCities().stream())
                .collect(groupingHighPopulatedCitiesByContinent);
        highPopulatedCitiesByContinent.forEach(printEntry);

    }

}
