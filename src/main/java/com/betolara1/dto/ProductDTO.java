package com.betolara1.dto;

import java.time.LocalDateTime;

import com.betolara1.model.Product;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String type_product;
    private Product.LocalToPut localToPut;
    private Long idModuleFather;
    private Long idModuleChild;
    private Boolean isActive;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    public ProductDTO(){}

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.type_product = product.getType_product();
        this.localToPut = product.getLocalToPut();
        this.idModuleFather = product.getIdModuleFather();
        this.idModuleChild = product.getIdModuleChild();
        this.isActive = product.getIsActive();
        this.dateCreated = product.getDateCreated();
        this.dateUpdated = product.getDateUpdated();
    }
}
