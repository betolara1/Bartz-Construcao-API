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

import com.betolara1.dto.ProductDTO;
import com.betolara1.dto.request.SaveProductRequest;
import com.betolara1.dto.request.UpdateProductRequest;
import com.betolara1.model.Product;
import com.betolara1.service.ProductService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ProductDTO>> findAll(@RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size){
        return ResponseEntity.ok(productService.findAll(page, size));
    }

    @GetMapping("/type")
    public ResponseEntity<Page<ProductDTO>> findByTypeProduct(@RequestParam String type, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size){
        return ResponseEntity.ok(productService.findByTypeProduct(type, page, size));
    }

    @GetMapping("/id")
    public ResponseEntity<ProductDTO> findById(@RequestParam Long id){
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping("/localToPut")
    public ResponseEntity<Page<ProductDTO>> findByLocalToPut(@RequestParam Product.LocalToPut localToPut, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size){
        return ResponseEntity.ok(productService.findByLocalToPut(localToPut, page, size));
    }

    @GetMapping("/idModuleFather")
    public ResponseEntity<Page<ProductDTO>> findByIdModuleFather(@RequestParam Long idModuleFather, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size){
        return ResponseEntity.ok(productService.findByIdModuleFather(idModuleFather, page, size));
    }

    @GetMapping("/idModuleChild")
    public ResponseEntity<Page<ProductDTO>> findByIdModuleChild(@RequestParam Long idModuleChild, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size){
        return ResponseEntity.ok(productService.findByIdModuleChild(idModuleChild, page, size));
    }

    @GetMapping("/isActive")
    public ResponseEntity<Page<ProductDTO>> findByIsActive(@RequestParam Boolean isActive, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size){
        return ResponseEntity.ok(productService.findByIsActive(isActive, page, size));
    }

    @GetMapping("/dateCreated")
    public ResponseEntity<Page<ProductDTO>> getProductByDateCreated(@RequestParam String dateString, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size){
        return ResponseEntity.ok(productService.getProductByDateCreated(dateString, page, size));
    }

    @GetMapping("/dateUpdated")
    public ResponseEntity<Page<ProductDTO>> getProductByDateUpdated(@RequestParam String dateString, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size){
        return ResponseEntity.ok(productService.getProductByDateUpdated(dateString, page, size));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> save(@Valid @RequestBody SaveProductRequest request){
        Product product = productService.save(request);
        ProductDTO productDTO = new ProductDTO(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@Valid @RequestBody UpdateProductRequest request, @PathVariable Long id){
        Product product = productService.update(id, request);
        ProductDTO productDTO = new ProductDTO(product);

        return ResponseEntity.status(HttpStatus.OK).body(productDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Produto deletado com sucesso.");
    }
}
