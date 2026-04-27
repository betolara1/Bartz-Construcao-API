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

import com.betolara1.dto.request.SaveLocalToPutRequest;
import com.betolara1.dto.request.UpdateLocalToPutRequest;
import com.betolara1.dto.response.LocalToPutDTO;
import com.betolara1.model.LocalToPut;
import com.betolara1.service.LocalToPutService;
import com.betolara1.util.PaginationUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/local")
public class LocalToPutController {
    private final LocalToPutService localToPutService;

    public LocalToPutController(LocalToPutService localToPutService) {
        this.localToPutService = localToPutService;
    }

    @GetMapping("/all")
    public ResponseEntity<Page<LocalToPutDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreated") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        // Chamada estática: Classe.metodo()
        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, direction);
        return ResponseEntity.ok(localToPutService.findAll(pageable));
    }

    @GetMapping("/id")
    public ResponseEntity<LocalToPutDTO> findById(@RequestParam Long id) {
        return ResponseEntity.ok(localToPutService.findById(id));
    }

    @GetMapping("/name")
    public ResponseEntity<LocalToPutDTO> findByName(@RequestParam String name) {
        return ResponseEntity.ok(localToPutService.findByName(name));
    }

    @GetMapping("/createdDate")
    public ResponseEntity<Page<LocalToPutDTO>> getLocalToPutByDateCreated(@RequestParam String date,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreated") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        // Chamada estática: Classe.metodo()
        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, direction);

        return ResponseEntity.ok(localToPutService.getLocalToPutsByDateCreated(date, pageable));
    }

    @GetMapping("/updatedDate")
    public ResponseEntity<Page<LocalToPutDTO>> getLocalToPutByDateUpdated(@RequestParam String date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreated") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        // Chamada estática: Classe.metodo()
        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, direction);

        return ResponseEntity.ok(localToPutService.getLocalToPutsByDateUpdated(date, pageable));
    }

    @PostMapping
    public ResponseEntity<LocalToPut> save(@Valid @RequestBody SaveLocalToPutRequest request) {
        LocalToPut localToPut = localToPutService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(localToPut);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocalToPut> update(@PathVariable Long id, @Valid @RequestBody UpdateLocalToPutRequest request) {
        LocalToPut localToPut = localToPutService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(localToPut);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocal(@PathVariable Long id) {
        localToPutService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
