package com.betolara1.dto;

import com.betolara1.model.Size;

import lombok.Data;

@Data
public class SizeDTO {
    private Long id;
    private Long idProduct;
    
    private Double heightMax;
    private Double heightMin;
    
    private Double widthMax;
    private Double widthMin;
    
    private Double depthMax;
    private Double depthMin;

    public SizeDTO(){}

    public SizeDTO(Size size) {
        this.id = size.getId();
        this.idProduct = size.getIdProduct();
        this.heightMax = size.getHeightMax();
        this.heightMin = size.getHeightMin();
        this.widthMax = size.getWidthMax();
        this.widthMin = size.getWidthMin();
        this.depthMax = size.getDepthMax();
        this.depthMin = size.getDepthMin();
    }
}
