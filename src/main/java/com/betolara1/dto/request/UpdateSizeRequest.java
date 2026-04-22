package com.betolara1.dto.request;

import lombok.Data;

@Data
public class UpdateSizeRequest {
    private Long idProduct;
    
    private Double heightMax;
    private Double heightMin;

    private Double widthMax;
    private Double widthMin;
    
    private Double depthMax;
    private Double depthMin;
}
