package ourfood.service.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ourfood.domain.Produce;

public interface ProduceRepository extends CrudRepository<Produce, Long> {
    
    List<Produce> findAll();
}
