package com.betolara1.dto.request;

import com.betolara1.model.Product;

import lombok.Data;

@Data
public class UpdateSizeRequest {
    private Product product;
    
    private Double heightMax;
    private Double heightMin;

    private Double widthMax;
    private Double widthMin;
    
    private Double depthMax;
    private Double depthMin;
}
