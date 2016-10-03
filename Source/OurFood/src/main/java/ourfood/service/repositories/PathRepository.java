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
    List<Path> findByOrigin(Location origin);

    List<Path> findByDestination(Location destination);

    Path findById(Long id);

    @Query("select * from Path p")
    List<Path> findAll();
}
