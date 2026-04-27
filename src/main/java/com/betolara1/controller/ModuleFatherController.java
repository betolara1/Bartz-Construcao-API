package com.betolara1.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.betolara1.dto.request.SaveModuleFatherRequest;
import com.betolara1.dto.request.UpdateModuleFatherRequest;
import com.betolara1.dto.response.ModuleFatherDTO;
import com.betolara1.model.ModuleFather;
import com.betolara1.service.ModuleFatherService;
import com.betolara1.util.PaginationUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/moduleFather")
public class ModuleFatherController {

    private final ModuleFatherService moduleFatherService;
    public ModuleFatherController(ModuleFatherService moduleFatherService) {
        this.moduleFatherService = moduleFatherService;
    }

    // Método para buscar todos os módulos pais
    @GetMapping("/all")
    public ResponseEntity<Page<ModuleFatherDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreated") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        // Chamada estática: Classe.metodo()
        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, direction);
        return ResponseEntity.ok(moduleFatherService.findAll(pageable));
    }

    // Método para buscar um módulo pai por ID
    @GetMapping("/id")
    public ResponseEntity<ModuleFatherDTO> getModuleFatherById(@RequestParam Long id) {
        return ResponseEntity.ok(moduleFatherService.getModuleFatherById(id));
    }

    // Método para buscar um módulo pai por nome
    @GetMapping("/name")
    public ResponseEntity<ModuleFatherDTO> getModuleFatherByName(@RequestParam String name) {
        return ResponseEntity.ok(moduleFatherService.getModuleFatherByName(name));
    }

    // Método para buscar um módulo pai por data de criação
    @GetMapping("/dateCreated")
    public ResponseEntity<Page<ModuleFatherDTO>> getModuleFatherByDateCreated(@RequestParam String date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreated") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        // Chamada estática: Classe.metodo()
        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, direction);
        return ResponseEntity.ok(moduleFatherService.getModuleFatherByDateCreated(date, pageable));
    }

    // Método para buscar um módulo pai por data de atualização
    @GetMapping("/dateUpdated")
    public ResponseEntity<Page<ModuleFatherDTO>> getModuleFatherByDateUpdated(@RequestParam String date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreated") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        // Chamada estática: Classe.metodo()
        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, direction);
        return ResponseEntity.ok(moduleFatherService.getModuleFatherByDateUpdated(date, pageable));
    }

    // Método para criar um módulo pai
    @PostMapping
    public ResponseEntity<ModuleFatherDTO> createModuleFather(@Valid @RequestBody SaveModuleFatherRequest request) {
        ModuleFather moduleFather = moduleFatherService.save(request);
        ModuleFatherDTO moduleFatherDTO = new ModuleFatherDTO(moduleFather);

        return ResponseEntity.status(HttpStatus.CREATED).body(moduleFatherDTO);
    }

    // Método para atualizar um módulo pai
    @PutMapping("/{id}")
    public ResponseEntity<ModuleFatherDTO> updateModuleFather(@Valid @RequestBody UpdateModuleFatherRequest request,
            @PathVariable Long id) {
        ModuleFather moduleFather = moduleFatherService.update(id, request);
        ModuleFatherDTO moduleFatherDTO = new ModuleFatherDTO(moduleFather);

        return ResponseEntity.status(HttpStatus.OK).body(moduleFatherDTO);
    }

    // Método para deletar um módulo pai
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModeuleFather(@PathVariable Long id) {
        moduleFatherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
