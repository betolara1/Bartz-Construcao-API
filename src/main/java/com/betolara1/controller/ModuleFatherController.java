package com.betolara1.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.betolara1.dto.ModuleFatherDTO;
import com.betolara1.service.ModuleFatherService;

@RestController
@RequestMapping("/module-fathers")
public class ModuleFatherController {

    private final ModuleFatherService moduleFatherService;
    public ModuleFatherController(ModuleFatherService moduleFatherService) {
        this.moduleFatherService = moduleFatherService;
    }

    @GetMapping("/allModules")
    public ResponseEntity<Page<ModuleFatherDTO>> findAll(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(moduleFatherService.findAll(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuleFatherDTO> getModuleFatherById(@RequestParam Long id){
        return ResponseEntity.ok(moduleFatherService.getModuleFatherById(id));
    }

    
}
