package ourfood.service;

import ourfood.domain.Location;
import ourfood.domain.Path;
import ourfood.domain.enums.LocationType;

import java.util.List;

/**
 * Created by njay on 3/10/16.
 */
public interface PathService {

    Path getPath(Long id);

    Location getLocation(Long id);

    List<Location> getLocationsOfType(LocationType type);

    List<Path> getPathsFrom(Location origin);

    List<Path> getPathsTo(Location destination);

    void createPaths();

    void displayAll();
}
