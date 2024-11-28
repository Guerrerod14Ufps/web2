package com.example.demo.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entities.Sesion;

public interface SesionRepository extends JpaRepository<Sesion, Long> {

	List<Sesion> findByProgramacionId(Long programacionId);
	
	@Query("SELECT s FROM Sesion s WHERE s.fecha > :fechaLimite ORDER BY s.fecha DESC")
	List<Sesion> findSesionesDespuesDeFecha(@Param("fechaLimite") LocalDate fechaLimite);

}
