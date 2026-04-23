package com.betolara1.dto.request;

import com.betolara1.model.Product;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SaveSizeRequest {
    private Long id;

    @NotNull(message = "O ID do produto é obrigatório.")
    private Product product;
    
    @NotNull(message = "A altura máxima é obrigatória.")
    private Double heightMax;
    
    @NotNull(message = "A altura mínima é obrigatória.")
    private Double heightMin;
    
    @NotNull(message = "A largura máxima é obrigatória.")
    private Double widthMax;
    
    @NotNull(message = "A largura mínima é obrigatória.")
    private Double widthMin;
    
    @NotNull(message = "A profundidade máxima é obrigatória.")
    private Double depthMax;
    
    @NotNull(message = "A profundidade mínima é obrigatória.")
    private Double depthMin;
}
