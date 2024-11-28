package com.example.demo.services;

import com.example.demo.entities.TipoDocumento;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.ResourceReferencedByOthersException;
import com.example.demo.repositories.TipoDocumentoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoDocumentoService {

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public TipoDocumento buscarTipoDocumento(Long id) throws ResourceNotFoundException {
        this.showErrorIfNotExist(id);
        Optional<TipoDocumento> tipoDocumento = tipoDocumentoRepository.findById(id);
        return tipoDocumento.get();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    public List<TipoDocumento> listarTipoDocumentos() {
        return tipoDocumentoRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public TipoDocumento crearTipoDocumento(TipoDocumento tipoDocumento) {
        return tipoDocumentoRepository.save(tipoDocumento);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public TipoDocumento actualizarTipoDocumento(TipoDocumento tipoDocumento) throws ResourceNotFoundException {
        this.showErrorIfNotExist(tipoDocumento.getId());
        return tipoDocumentoRepository.save(tipoDocumento);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public TipoDocumento eliminarTipoDocumento(Long id) throws ResourceNotFoundException, ResourceReferencedByOthersException {
        this.showErrorIfNotExist(id);
        Optional<TipoDocumento> tipoDocumento = tipoDocumentoRepository.findById(id);
        
        try {
            tipoDocumentoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceReferencedByOthersException("El tipo de documento no se puede eliminar porque está siendo utilizado en otras entidades.");
        }
        
        return tipoDocumento.get();
    }

    public void showErrorIfNotExist(TipoDocumento tipoDocumento) throws ResourceNotFoundException {
        if (tipoDocumento == null || tipoDocumento.getId() == null) {
            throw new ResourceNotFoundException("El tipo de documento no existe.");
        }
        showErrorIfNotExist(tipoDocumento.getId());
    }

    public void showErrorIfNotExist(Long id) throws ResourceNotFoundException {
        Optional<TipoDocumento> tipoDocumento = tipoDocumentoRepository.findById(id);
        
        if (tipoDocumento.isEmpty()) {
            throw new ResourceNotFoundException("El tipo de documento con id " + id + " no existe.");
        }
    }
}
