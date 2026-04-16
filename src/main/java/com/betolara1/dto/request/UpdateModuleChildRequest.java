package com.betolara1.dto.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateModuleChildRequest {
    private Long id;

    @NotBlank(message = "O nome do módulo filho é obrigatório.")
    @Size(min = 2, max = 100, message = "O nome do módulo filho deve ter entre 2 e 100 caracteres.")
    private String name;

    @NotNull(message = "A data de atualização é obrigatória.")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dateUpdated;

    @NotNull(message = "O ID do módulo pai é obrigatório.")
    private Long idModuleFather;
}
