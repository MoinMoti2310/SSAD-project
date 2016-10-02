package ourfood.service.repositories;

import org.springframework.data.repository.CrudRepository;
import ourfood.domain.Path;

/**
 * Created by njay on 30/9/16.
 */
public interface PathRepository extends CrudRepository<Path, Long> {

    // TODO: Get help in queries.
    //@Query("SELECT r FROM Path r WHERE r.origin.id=?1 AND r.type=?2")
    //List<Path> findRoutesFrom(Long locationId, LocationType locationType);

    //@Query("SELECT r FROM Path r WHERE r.destination.id=?1 AND r.type=?2")
    //List<Path> findRoutesTo(Long locationId, LocationType locationType);

    Path findById(Long id);
}
