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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.betolara1.dto.LocalToPutDTO;
import com.betolara1.dto.request.SaveLocalToPutRequest;
import com.betolara1.dto.request.UpdateLocalToPutRequest;
import com.betolara1.service.LocalToPutService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/local")
public class LocalToPutController {
    private final LocalToPutService localToPutService;
    public LocalToPutController(LocalToPutService localToPutService){
        this.localToPutService = localToPutService;
    }

    @GetMapping("/all")
    public ResponseEntity<Page<LocalToPutDTO>> findAll(                                                
                                                    @RequestParam(defaultValue="0") int page, 
                                                    @RequestParam(defaultValue="10") int size,
                                                    @RequestParam(defaultValue = "dateCreated") String sortBy,
                                                    @RequestParam(defaultValue = "desc") String direction){
        
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(localToPutService.findAll(pageable));
    }

    @GetMapping("/id")
    public ResponseEntity<LocalToPutDTO> findById(@RequestParam Long id){
        return ResponseEntity.ok(localToPutService.findById(id));
    }

    @GetMapping("/name")
    public ResponseEntity<LocalToPutDTO> findByName(@RequestParam String name){
        return ResponseEntity.ok(localToPutService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<LocalToPutDTO> save(@Valid @RequestBody SaveLocalToPutRequest request){
        LocalToPutDTO localToPutDTO = localToPutService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(localToPutDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocalToPutDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateLocalToPutRequest request){
        LocalToPutDTO localToPutDTO = localToPutService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(localToPutDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLocal(@PathVariable Long id){
        localToPutService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Local deletado com sucesso");
    }
}
