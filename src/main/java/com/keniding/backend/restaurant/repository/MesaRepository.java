package com.keniding.backend.restaurant.repository;

import com.keniding.backend.restaurant.model.Mesa;
import com.keniding.backend.restaurant.model.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {

    List<Mesa> findByEstado(Mesa.EstadoMesa estado);

    List<Mesa> findByUbicacion(Mesa.UbicacionMesa ubicacion);

    List<Mesa> findByZona(Zona zona);

    Optional<Mesa> findByNumero(String numero);

    @Query("SELECT m FROM Mesa m WHERE (:estado IS NULL OR m.estado = :estado) AND (:capacidad IS NULL OR m.capacidad = :capacidad)")
    List<Mesa> findByFilters(Mesa.EstadoMesa estado, Integer capacidad);

    @Query("SELECT COUNT(m) FROM Mesa m WHERE m.estado = :estado")
    long countByEstado(Mesa.EstadoMesa estado);
}
