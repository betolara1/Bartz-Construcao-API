package com.betolara1.dto.request;  

import com.betolara1.model.LocalToPut;
import com.betolara1.model.ModuleChild;
import com.betolara1.model.ModuleFather;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProductRequest {
    private Long id;

    @Size(min = 2, max = 100, message = "O nome do produto deve ter entre 2 e 100 caracteres.")
    private String name;

    private ModuleFather moduleFather;

    private String typeProduct;

    private LocalToPut localToPut;

    private ModuleChild moduleChild;

    private Boolean isActive;
}
