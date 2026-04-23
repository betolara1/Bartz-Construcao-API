package com.betolara1.dto.response;

import java.time.LocalDateTime;

import com.betolara1.model.LocalToPut;
import com.betolara1.model.ModuleChild;
import com.betolara1.model.ModuleFather;
import com.betolara1.model.Product;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String typeProduct;
    private LocalToPut localToPut;
    private ModuleFather moduleFather;
    private ModuleChild moduleChild;
    private Boolean isActive;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    public ProductDTO(){}

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.typeProduct = product.getTypeProduct();
        this.localToPut = product.getLocalToPut();
        this.moduleFather = product.getModuleFather();
        this.moduleChild = product.getModuleChild();
        this.isActive = product.getIsActive();
        this.dateCreated = product.getDateCreated();
        this.dateUpdated = product.getDateUpdated();
    }
}
