package ourfood.service.repositories;

import org.springframework.data.repository.CrudRepository;
import ourfood.domain.Location;
import ourfood.domain.MarketPricingData;

/**
 * Created by njay on 1/10/16.
 */
public interface MarketPricingDataRepository extends CrudRepository<MarketPricingData, Long> {

    // TODO: Need to understand repo querying better.
    MarketPricingData findById(Long id);

    //MarketPricingData findByLocation(Location location);
}
