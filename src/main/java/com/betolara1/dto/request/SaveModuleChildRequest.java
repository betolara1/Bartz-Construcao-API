package com.betolara1.dto.request;

import com.betolara1.model.ModuleFather;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SaveModuleChildRequest {
    private Long id;

    @NotBlank(message = "O nome do módulo filho é obrigatório.")
    @Size(min = 2, max = 100, message = "O nome do módulo filho deve ter entre 2 e 100 caracteres.")
    private String name;

    @NotNull(message = "O ID do módulo pai é obrigatório.")
    private ModuleFather moduleFather;
}
