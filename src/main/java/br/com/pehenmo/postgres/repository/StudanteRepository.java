package br.com.pehenmo.postgres.repository;

import br.com.pehenmo.postgres.entity.StudanteEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Repository
public interface StudanteRepository extends CrudRepository<StudanteEntity, Integer> {

    Optional<StudanteEntity> findByFirstName(String firstName);

}
