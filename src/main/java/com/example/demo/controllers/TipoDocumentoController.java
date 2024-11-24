package com.example.demo.controllers;

import com.example.demo.DTO.TipoDocumentoDTO;
import com.example.demo.DTO.TipoDocumentoRequestDTO;
import com.example.demo.entities.TipoDocumento;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.services.TipoDocumentoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipo-documento")
public class TipoDocumentoController {

    private final TipoDocumentoService tipoDocumentoService;

    public TipoDocumentoController(TipoDocumentoService tipoDocumentoService) {
        this.tipoDocumentoService = tipoDocumentoService;
    }

    @GetMapping
    public List<TipoDocumento> listarTipoDocumento() {
        return tipoDocumentoService.listarTipoDocumento();
    }
    
    @GetMapping("/{id}")
    public TipoDocumentoDTO getTipoDocumento(@PathVariable Long id) throws Exception {
        try {
            TipoDocumento tipoDocumento = tipoDocumentoService.buscarTipoDocumento(id);
            return new TipoDocumentoDTO(tipoDocumento.getId(), tipoDocumento.getDescripcion());
        } catch (Exception e) {
            throw new ResourceNotFoundException("El tipo de documento con id " + id + " no existe.");
        }
    }

    @PostMapping
    public ResponseEntity<TipoDocumento> crearTipoDocumento(@RequestBody TipoDocumentoRequestDTO tipoDocumentoRequestDTO) {
        TipoDocumento tipoDocumento = new TipoDocumento();
        tipoDocumento.setDescripcion(tipoDocumentoRequestDTO.getDescripcion());
        return ResponseEntity.ok(tipoDocumentoService.crearTipoDocumento(tipoDocumento));
    }


    @PutMapping("/{id}")
    public ResponseEntity<TipoDocumento> actualizarTipoDocumento(@PathVariable Long id, @RequestBody TipoDocumentoRequestDTO tipoDocumentoRequestDTO) {
        return tipoDocumentoService.actualizarTipoDocumento(id, tipoDocumentoRequestDTO.getDescripcion())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TipoDocumento> eliminarTipoDocumento(@PathVariable Long id) {
        return tipoDocumentoService.eliminarTipoDocumento(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
