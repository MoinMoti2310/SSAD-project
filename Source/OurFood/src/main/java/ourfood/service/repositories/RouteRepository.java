package ourfood.service.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ourfood.domain.Route;
import ourfood.domain.enums.LocationType;

import java.util.List;

/**
 * Created by njay on 30/9/16.
 */
public interface RouteRepository extends CrudRepository<Route, Long> {

    // TODO: Get help in queries.
    //@Query("SELECT r FROM Route r WHERE r.origin.id=?1 AND r.type=?2")
    List<Route> findRoutesFrom(Long locationId, LocationType locationType);

    //@Query("SELECT r FROM Route r WHERE r.destination.id=?1 AND r.type=?2")
    List<Route> findRoutesTo(Long locationId, LocationType locationType);

    Route findById(Long id);
}
