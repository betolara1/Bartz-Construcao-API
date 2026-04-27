package com.betolara1.dto.request;

import com.betolara1.model.LocalToPut;
import com.betolara1.model.ModuleChild;
import com.betolara1.model.ModuleFather;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SaveProductRequest {
    @NotBlank(message = "O nome do produto é obrigatório.")
    @Size(min = 2, max = 100, message = "O nome do produto deve ter entre 2 e 100 caracteres.")
    private String name;

    @NotNull(message = "O ID do módulo pai é obrigatório.")
    private ModuleFather moduleFather;

    @NotBlank(message = "O tipo do produto é obrigatório.")
    private String typeProduct;

    @NotNull(message = "O local de colocação é obrigatório.")
    private LocalToPut localToPut;

    @NotNull(message = "O ID do módulo filho é obrigatório.")
    private ModuleChild moduleChild;

    @NotNull(message = "O status do produto é obrigatório.")
    private Boolean isActive;
}
