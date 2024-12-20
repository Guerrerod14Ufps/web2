package com.example.demo.controllers;

import com.example.demo.DTO.ProgramacionDTO;
import com.example.demo.DTO.ProgramacionRequestDTO;
import com.example.demo.entities.Programacion;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.ResourceReferencedByOthersException;
import com.example.demo.services.ProgramacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programacion")
public class ProgramacionController {

    @Autowired
    private ProgramacionService programacionService;

	@GetMapping
	public ResponseEntity<List<ProgramacionDTO>> listarProgramaciones(@RequestParam(required = false) Long tallerId,
			@RequestParam(required = false) Boolean proximas) {
		if (tallerId != null) {
			List<Programacion> programaciones = programacionService.listarProgramacionesPorTaller(tallerId);
			return ResponseEntity.ok(ProgramacionDTO.fromEntity(programaciones));
		} else if (proximas != null) {
			List<Programacion> programaciones = programacionService.listarProgramacionesPosteriores();
			return ResponseEntity.ok(ProgramacionDTO.fromEntity(programaciones));
		} else {
			List<Programacion> programaciones = programacionService.listarProgramaciones();
			return ResponseEntity.ok(ProgramacionDTO.fromEntity(programaciones));
		}
	}

    @GetMapping("/{id}")
    public ResponseEntity<ProgramacionDTO> obtenerProgramacion(@PathVariable Long id) throws ResourceNotFoundException {
        Programacion programacion = programacionService.buscarProgramacion(id);
        return ResponseEntity.ok(ProgramacionDTO.fromEntity(programacion));
    }

    @PostMapping
    public ResponseEntity<?> crearProgramacion(@RequestBody ProgramacionRequestDTO programacionRequestDTO) {
        Programacion programacion = programacionRequestDTO.toEntity();
        programacion = programacionService.crearProgramacion(programacion);
        return ResponseEntity.status(201).body(ProgramacionDTO.fromEntity(programacion));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProgramacionDTO> actualizarProgramacion(@PathVariable Long id, @RequestBody ProgramacionRequestDTO programacionRequestDTO) throws ResourceNotFoundException {
        Programacion programacion = programacionRequestDTO.toEntity();
        programacion.setId(id);
        programacion = programacionService.actualizarProgramacion(programacion);
        return ResponseEntity.ok(ProgramacionDTO.fromEntity(programacion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProgramacionDTO> eliminarProgramacion(@PathVariable Long id) throws ResourceNotFoundException, ResourceReferencedByOthersException {
        Programacion programacion = programacionService.eliminarProgramacion(id);
        return ResponseEntity.ok(ProgramacionDTO.fromEntity(programacion));
    }
    
    
}
