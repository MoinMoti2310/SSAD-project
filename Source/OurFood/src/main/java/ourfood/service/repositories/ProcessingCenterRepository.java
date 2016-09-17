package ourfood.service.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ourfood.domain.ProcessingCenter;

public interface ProcessingCenterRepository extends CrudRepository<ProcessingCenter, Long> {

    List<ProcessingCenter> findAll();
}
