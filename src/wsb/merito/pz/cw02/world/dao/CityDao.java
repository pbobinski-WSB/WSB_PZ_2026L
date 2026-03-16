package wsb.merito.pz.cw02.world.dao;



import wsb.merito.pz.cw02.world.domain.City;

import java.util.List;

/**
 *
 * @author Binnur Kurt (binnur.kurt@gmail.com)
 */
public interface CityDao {
	City findCityById(int id);

	City removeCity(City city);

	City addCity(City city);

	City updateCity(City city);

	List<City> findAllCities();

	List<City> findCitiesByCountryCode(String countryCode);
}
