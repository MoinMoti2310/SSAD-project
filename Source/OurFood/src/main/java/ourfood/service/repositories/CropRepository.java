package ourfood.service.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ourfood.domain.Crop;

public interface CropRepository extends CrudRepository<Crop, Long> {
    
    List<Crop> findAll();
}
