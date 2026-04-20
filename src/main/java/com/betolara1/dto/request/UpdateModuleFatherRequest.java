package com.betolara1.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateModuleFatherRequest {
    private Long id;

    @Size(min = 2, max = 100, message = "O nome do módulo pai deve ter entre 2 e 100 caracteres.")
    private String name;
}
