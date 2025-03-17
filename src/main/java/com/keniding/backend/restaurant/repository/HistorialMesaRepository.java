package com.keniding.backend.restaurant.repository;

import com.keniding.backend.restaurant.model.HistorialMesa;
import com.keniding.backend.restaurant.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialMesaRepository extends JpaRepository<HistorialMesa, Long> {

    List<HistorialMesa> findByMesaOrderByFechaHoraDesc(Mesa mesa);
}
