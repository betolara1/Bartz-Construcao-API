package com.betolara1.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
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

import com.betolara1.dto.ModuleFatherDTO;
import com.betolara1.dto.request.SaveModuleFatherRequest;
import com.betolara1.dto.request.UpdateModuleFatherRequest;
import com.betolara1.model.ModuleFather;
import com.betolara1.service.ModuleFatherService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/moduleFather")
public class ModuleFatherController {

    private final ModuleFatherService moduleFatherService;
    public ModuleFatherController(ModuleFatherService moduleFatherService) {
        this.moduleFatherService = moduleFatherService;
    }

    @GetMapping("/allModules")
    public ResponseEntity<Page<ModuleFatherDTO>> findAll(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(moduleFatherService.findAll(page, size));
    }

    @GetMapping("/id")
    public ResponseEntity<ModuleFatherDTO> getModuleFatherById(@RequestParam Long id){
        return ResponseEntity.ok(moduleFatherService.getModuleFatherById(id));
    }

    @GetMapping("/name")
    public ResponseEntity<ModuleFatherDTO> getModuleFatherByName(@RequestParam String name){
        return ResponseEntity.ok(moduleFatherService.getModuleFatherByName(name));
    }

    @GetMapping("/dateCreated")
    public ResponseEntity<Page<ModuleFatherDTO>> getModuleFatherByDateCreated(@RequestParam String date, 
                                                                        @RequestParam(defaultValue = "0") int page, 
                                                                        @RequestParam(defaultValue = "10") int size){
        
        return ResponseEntity.ok(moduleFatherService.getModuleFatherByDateCreated(date, page, size));
    }

    @GetMapping("/dateUpdated")
    public ResponseEntity<Page<ModuleFatherDTO>> getModuleFatherByDateUpdated(@RequestParam String date, 
                                                                        @RequestParam(defaultValue = "0") int page, 
                                                                        @RequestParam(defaultValue = "10") int size){
        
        return ResponseEntity.ok(moduleFatherService.getModuleFatherByDateUpdated(date, page, size));
    }

    @PostMapping
    public ResponseEntity<ModuleFatherDTO> createModuleFather(@Valid @RequestBody SaveModuleFatherRequest request){
        ModuleFather moduleFather = moduleFatherService.save(request);
        ModuleFatherDTO moduleFatherDTO = new ModuleFatherDTO(moduleFather);

        return ResponseEntity.status(HttpStatus.CREATED).body(moduleFatherDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModuleFatherDTO> updateModuleFather(@Valid @RequestBody UpdateModuleFatherRequest request, @PathVariable Long id){
        ModuleFather moduleFather = moduleFatherService.update(id, request);
        ModuleFatherDTO moduleFatherDTO = new ModuleFatherDTO(moduleFather);

        return ResponseEntity.status(HttpStatus.OK).body(moduleFatherDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteModeuleFather(@PathVariable Long id){
        moduleFatherService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Módulo pai deletado com sucesso.");
    }
}
