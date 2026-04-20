package com.betolara1.controller;

import org.springframework.data.domain.Page;
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

import com.betolara1.dto.SizeDTO;
import com.betolara1.dto.request.SaveSizeRequest;
import com.betolara1.dto.request.UpdateSizeRequest;
import com.betolara1.model.Size;
import com.betolara1.service.SizeService;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/sizes")
public class SizeController {
    private final SizeService sizeService;
    public SizeController(SizeService sizeService) {
        this.sizeService = sizeService;
    }

    @GetMapping("/all")
    public ResponseEntity<Page<SizeDTO>> findAll(@RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size){
        return ResponseEntity.ok(sizeService.findAll(page, size));
    }

    @GetMapping("/id")
    public ResponseEntity<SizeDTO> findById(@RequestParam Long id){
        return ResponseEntity.ok(sizeService.findById(id));
    }

    @GetMapping("/productId")
    public ResponseEntity<SizeDTO> findByProductId(@RequestParam Long productId){
        return ResponseEntity.ok(sizeService.findByProductId(productId));
    }

    @GetMapping("/dateCreated")
    public ResponseEntity<Page<SizeDTO>> getSizeByDateCreated(@RequestParam String dateString, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size){
        return ResponseEntity.ok(sizeService.getSizeByDateCreated(dateString, page, size));
    }

    @GetMapping("/dateUpdated")
    public ResponseEntity<Page<SizeDTO>> getSizeByDateUpdated(@RequestParam String dateString, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size){
        return ResponseEntity.ok(sizeService.getSizeByDateUpdated(dateString, page, size));
    }

    @PostMapping
    public ResponseEntity<SizeDTO> save(@Valid @RequestBody SaveSizeRequest saveSizeRequest){
        Size size = sizeService.save(saveSizeRequest);
        SizeDTO sizeDTO = new SizeDTO(size);
        return ResponseEntity.status(HttpStatus.CREATED).body(sizeDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SizeDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateSizeRequest updateSizeRequest){
        Size size = sizeService.update(id, updateSizeRequest);
        SizeDTO sizeDTO = new SizeDTO(size);
        return ResponseEntity.ok(sizeDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        sizeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
