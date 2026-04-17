package com.betolara1.dto.request;

import java.time.LocalDateTime;

import com.betolara1.model.Product.LocalToPut;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProductRequest {
    private Long id;

    @NotBlank(message = "O nome do produto é obrigatório.")
    @Size(min = 2, max = 100, message = "O nome do produto deve ter entre 2 e 100 caracteres.")
    private String name;

    @NotNull(message = "A data de criação é obrigatória.")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dateUpdated;

    @NotNull(message = "O ID do módulo pai é obrigatório.")
    private Long idModuleFather;

    @NotBlank(message = "O tipo do produto é obrigatório.")
    private String typeProduct;

    @NotNull(message = "O local de colocação é obrigatório.")
    private LocalToPut localToPut;

    @NotNull(message = "O ID do módulo filho é obrigatório.")
    private Long idModuleChild;

    @NotNull(message = "O status de atividade é obrigatório.")
    private Boolean isActive;
}
