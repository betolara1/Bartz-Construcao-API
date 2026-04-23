package com.betolara1.dto.response;

import java.time.LocalDateTime;

import com.betolara1.model.LocalToPut;
import com.betolara1.model.ModuleChild;
import com.betolara1.model.ModuleFather;
import com.betolara1.model.Product;

public record ProductDTO(
    Long id,
    String name,
    String typeProduct,
    LocalToPut localToPut,
    ModuleFather moduleFather,
    ModuleChild moduleChild,
    Boolean isActive,
    LocalDateTime dateCreated,
    LocalDateTime dateUpdated){

    public ProductDTO(Product product) {
        this(product.getId(),
        product.getName(),
        product.getTypeProduct(),
        product.getLocalToPut(),
        product.getModuleFather(),
        product.getModuleChild(),
        product.getIsActive(),
        product.getDateCreated(),
        product.getDateUpdated());
    }
}
