package ourfood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ourfood.domain.Location;
import ourfood.domain.Path;
import ourfood.domain.enums.LocationType;
import ourfood.service.repositories.LocationRepository;
import ourfood.service.repositories.PathRepository;

import java.util.List;

/**
 * Created by njay on 3/10/16.
 */

@Component
@Transactional
public class PathServiceImpl implements PathService {

    @Autowired
    private PathRepository pathRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public Path getPath(Long id) {
        Assert.notNull(id, "Path ID must not be null.");
        return pathRepository.findById(id);
    }

    @Override
    public Location getLocation(Long id) {
        Assert.notNull(id, "Location ID must not be null.");
        return locationRepository.findById(id);
    }

    @Override
    public List<Location> getLocationsOfType(LocationType type) {
        Assert.notNull(type, "Type must not be null.");
        return locationRepository.findByType(type);
    }

    @Override
    public List<Path> getPathsFrom(Location origin) {
        Assert.notNull(origin, "Origin must not be null.");
        return pathRepository.findByOrigin(origin);
    }

    @Override
    public List<Path> getPathsTo(Location destination) {
        Assert.notNull(destination, "Destination must not be null.");
        return pathRepository.findByDestination(destination);
    }

    @Override
    public void createPaths() {
        List<Location> farms = getLocationsOfType(LocationType.FARM);
        List<Location> warehouses = getLocationsOfType(LocationType.WAREHOUSE);
        List<Location> markets = getLocationsOfType(LocationType.MARKET);

        // TODO: JsonParser call
    }

    @Override
    public void displayAll() {}
}
