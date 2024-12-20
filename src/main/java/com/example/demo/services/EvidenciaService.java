package com.example.demo.services;

import com.example.demo.entities.Evidencia;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.ResourceReferencedByOthersException;
import com.example.demo.repositories.EvidenciaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
public class EvidenciaService {

    @Autowired
    private EvidenciaRepository evidenciaRepository;

    @Autowired
    private SesionService sesionService;
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Evidencia buscarEvidencia(Long id) throws ResourceNotFoundException {
        this.showErrorIfNotExist(id);
        Optional<Evidencia> evidencia = evidenciaRepository.findById(id);
        return evidencia.get();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Evidencia> listarEvidencias() {
        return evidenciaRepository.findAll();
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    public Evidencia crearEvidencia(Evidencia evidencia) throws ResourceNotFoundException {
    	
        sesionService.showErrorIfNotExist(evidencia.getSesion());

        return evidenciaRepository.save(evidencia);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Evidencia actualizarEvidencia(Evidencia evidencia) throws ResourceNotFoundException {
        this.showErrorIfNotExist(evidencia);

        sesionService.showErrorIfNotExist(evidencia.getSesion());

        return evidenciaRepository.save(evidencia);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Evidencia eliminarEvidencia(Long id) throws ResourceNotFoundException, ResourceReferencedByOthersException {
        this.showErrorIfNotExist(id);
        Optional<Evidencia> evidencia = evidenciaRepository.findById(id);

        try {
            evidenciaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceReferencedByOthersException("La evidencia no puede eliminarse porque está referenciada por otras entidades.");
        }

        return evidencia.get();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void showErrorIfNotExist(Evidencia evidencia) throws ResourceNotFoundException {
        if (evidencia == null || evidencia.getId() == null) {
            throw new ResourceNotFoundException("La evidencia no existe.");
        }
        showErrorIfNotExist(evidencia.getId());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void showErrorIfNotExist(Long id) throws ResourceNotFoundException {
        Optional<Evidencia> evidencia = evidenciaRepository.findById(id);

        if (evidencia.isEmpty()) {
            throw new ResourceNotFoundException("La evidencia con id " + id + " no existe.");
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
	public List<Evidencia> listarEvidenciasPorSesion(Long sesionId) {
		return evidenciaRepository.findBySesionId(sesionId);
	}
}
