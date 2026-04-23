package com.betolara1.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.betolara1.dto.request.SaveModuleChildRequest;
import com.betolara1.dto.request.UpdateModuleChildRequest;
import com.betolara1.dto.response.ModuleChildDTO;
import com.betolara1.service.ModuleChildService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/moduleChild")
public class ModuleChildController {

    private final ModuleChildService moduleChildService;
    public ModuleChildController(ModuleChildService moduleChildService) {
        this.moduleChildService = moduleChildService;
    }


    // Método para buscar todos os módulos filhos
    @GetMapping("/all")
    public ResponseEntity<Page<ModuleChildDTO>> findAll(
                                                        @RequestParam(defaultValue="0") int page, 
                                                        @RequestParam(defaultValue="10") int size,
                                                        @RequestParam(defaultValue = "dateCreated") String sortBy,
                                                        @RequestParam(defaultValue = "desc") String direction){
        
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(moduleChildService.findAll(pageable));
    }


    // Método para buscar um módulo filho por ID
    @GetMapping("/id")
    public ResponseEntity<ModuleChildDTO> getModuleChildById(@RequestParam Long id){
        return ResponseEntity.ok(moduleChildService.getModuleChildById(id));
    }


    // Método para buscar um módulo filho por nome
    @GetMapping("/name")
    public ResponseEntity<ModuleChildDTO> getModuleChildByName(@RequestParam String name){
        return ResponseEntity.ok(moduleChildService.getModuleChildByName(name));
    }


    // Método para buscar um módulo filho por ID do módulo pai
    @GetMapping("/idModuleFather")
    public ResponseEntity<Page<ModuleChildDTO>> getModuleChildByIdModuleFather( @RequestParam Long id, 
                                                                                @RequestParam(defaultValue="0") int page, 
                                                                                @RequestParam(defaultValue="10") int size,
                                                                                @RequestParam(defaultValue = "dateCreated") String sortBy,
                                                                                @RequestParam(defaultValue = "desc") String direction){
        
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(moduleChildService.getModuleChildByIdModuleFather(id, pageable));
    }


    // Método para buscar um módulo filho por data de criação
    @GetMapping("/dateCreated")
    public ResponseEntity<Page<ModuleChildDTO>> getModuleChildByDateCreated(@RequestParam String date, 
                                                                            @RequestParam(defaultValue="0") int page, 
                                                                            @RequestParam(defaultValue="10") int size,
                                                                            @RequestParam(defaultValue = "dateCreated") String sortBy,
                                                                            @RequestParam(defaultValue = "desc") String direction){
        
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        

        return ResponseEntity.ok(moduleChildService.getModuleChildByDateCreated(date, pageable));
    }


    // Método para buscar um módulo filho por data de atualização
    @GetMapping("/dateUpdated")
    public ResponseEntity<Page<ModuleChildDTO>> getModuleChildByDateUpdated(@RequestParam String date, 
                                                                            @RequestParam(defaultValue="0") int page, 
                                                                            @RequestParam(defaultValue="10") int size,
                                                                            @RequestParam(defaultValue = "dateCreated") String sortBy,
                                                                            @RequestParam(defaultValue = "desc") String direction){
        
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        

        return ResponseEntity.ok(moduleChildService.getModuleChildByDateUpdated(date, pageable));
    }


    // Método para criar um módulo filho
    @PostMapping
    public ResponseEntity<ModuleChildDTO> createModuleChild(@Valid @RequestBody SaveModuleChildRequest request){
        ModuleChildDTO moduleChildDTO = moduleChildService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(moduleChildDTO);
    }


    // Método para atualizar um módulo filho
    @PutMapping("/{id}")
    public ResponseEntity<ModuleChildDTO> updateModuleChild(@Valid @RequestBody UpdateModuleChildRequest request, @PathVariable Long id){
        ModuleChildDTO moduleChildDTO = moduleChildService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(moduleChildDTO);
    }


    // Método para deletar um módulo filho
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteModeuleChild(@PathVariable Long id){
        moduleChildService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Módulo filho deletado com sucesso");
    }
}
