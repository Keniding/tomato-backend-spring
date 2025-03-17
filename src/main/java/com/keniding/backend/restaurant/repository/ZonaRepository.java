package com.keniding.backend.restaurant.repository;

import com.keniding.backend.restaurant.model.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ZonaRepository extends JpaRepository<Zona, Long> {

    Optional<Zona> findByCodigo(String codigo);
}
