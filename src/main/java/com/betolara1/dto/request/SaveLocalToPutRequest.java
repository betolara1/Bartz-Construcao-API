package com.betolara1.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SaveLocalToPutRequest {
    @NotBlank(message = "O nome não pode ser vazio")
    private String name;

}
