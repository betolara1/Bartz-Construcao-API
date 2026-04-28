package com.betolara1.dto.request;  

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProductRequest {
    @Size(min = 2, max = 100, message = "O nome do produto deve ter entre 2 e 100 caracteres.")
    private String name;

    private Long moduleFatherId;

    private String typeProduct;

    private Long localToPutId;

    private Long moduleChildId;

    private Boolean isActive;
}
