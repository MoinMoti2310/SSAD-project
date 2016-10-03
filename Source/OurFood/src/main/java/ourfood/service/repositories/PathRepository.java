package ourfood.service.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ourfood.domain.Location;
import ourfood.domain.Path;
import ourfood.domain.enums.LocationType;

import java.util.List;

/**
 * Created by njay on 30/9/16.
 */
public interface PathRepository extends CrudRepository<Path, Long> {

    // TODO: Get help in queries.
    List<Path> findAllByOrigin(Location origin);

    List<Path> findAllByDestination(Location destination);

    Path findById(Long id);

    List<Path> findAll();
}
