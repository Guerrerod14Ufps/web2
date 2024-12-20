package com.example.demo.controllers;

import com.example.demo.DTO.SesionDTO;
import com.example.demo.DTO.SesionRequestDTO;
import com.example.demo.entities.Sesion;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.ResourceReferencedByOthersException;
import com.example.demo.services.SesionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sesion")
public class SesionController {

    @Autowired
    private SesionService sesionService;

    
    @GetMapping
    public ResponseEntity<List<SesionDTO>> listarSesiones(@RequestParam(required = false) Long programacionId,@RequestParam(required = false) Boolean reciente) {
        if (programacionId!=null) {
        	List<Sesion> sesiones = sesionService.listarSesionesPorProgramacion(programacionId);
	        return ResponseEntity.ok(SesionDTO.fromEntity(sesiones));
		} else if(reciente!=null){
			return ResponseEntity.ok(SesionDTO.fromEntity(sesionService.listarSesionesPasadasUnMes()));
		} else {
	    	List<Sesion> sesiones = sesionService.listarSesiones();
	        return ResponseEntity.ok(SesionDTO.fromEntity(sesiones));
		}
    }

    @GetMapping("/{id}")
    public ResponseEntity<SesionDTO> obtenerSesion(@PathVariable Long id) throws ResourceNotFoundException {
        Sesion sesion = sesionService.buscarSesion(id);
        return ResponseEntity.ok(SesionDTO.fromEntity(sesion));
    }

    @PostMapping
    public ResponseEntity<SesionDTO> crearSesion(@RequestBody SesionRequestDTO sesionRequestDTO) throws ResourceNotFoundException {
        Sesion sesion = sesionRequestDTO.toEntity();
        sesion = sesionService.crearSesion(sesion);
        return ResponseEntity.status(201).body(SesionDTO.fromEntity(sesion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SesionDTO> actualizarSesion(@PathVariable Long id, @RequestBody SesionRequestDTO sesionRequestDTO) throws ResourceNotFoundException {
        Sesion sesion = sesionRequestDTO.toEntity();
        sesion.setId(id);
        sesion = sesionService.actualizarSesion(sesion);
        return ResponseEntity.ok(SesionDTO.fromEntity(sesion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SesionDTO> eliminarSesion(@PathVariable Long id) throws ResourceNotFoundException, ResourceReferencedByOthersException {
        Sesion sesion = sesionService.eliminarSesion(id);
        return ResponseEntity.ok(SesionDTO.fromEntity(sesion));
    }
}
