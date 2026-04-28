package com.betolara1.dto.response;

import java.time.LocalDateTime;

import com.betolara1.model.Product;

public record ProductDTO(
    Long id,
    String name,
    String typeProduct,

    Long localToPutId,
    Long moduleFatherId,
    Long moduleChildId,

    Boolean isActive,
    LocalDateTime dateCreated,
    LocalDateTime dateUpdated){

    public ProductDTO(Product product) {
        this(product.getId(),
        product.getName(),
        product.getTypeProduct(),

        product.getLocalToPut().getId(),
        product.getModuleFather().getId(),
        product.getModuleChild().getId(),

        product.getIsActive(),
        product.getDateCreated(),
        product.getDateUpdated());
    }
}
