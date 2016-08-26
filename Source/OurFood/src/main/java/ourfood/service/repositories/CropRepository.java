package ourfood.service.repositories;

import org.springframework.data.repository.CrudRepository;

import ourfood.domain.Crop;

public interface CropRepository extends CrudRepository<Crop, Long> {

}
