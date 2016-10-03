package ourfood.service.repositories;

import org.springframework.data.repository.CrudRepository;
import ourfood.domain.Location;
import ourfood.domain.enums.LocationType;

import java.util.List;

/**
 * Created by njay on 3/10/16.
 */
public interface LocationRepository extends CrudRepository<Location, Long> {

    List<Location> findByType(LocationType type);

    Location findById(Long id);

    Location findByLatitudeAndLongitude(Float latitude, Float longitude);

    Location findByPinCodeAndType(int pinCode, LocationType type);
}
