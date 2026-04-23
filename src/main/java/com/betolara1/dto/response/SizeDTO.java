package com.betolara1.dto.response;

import com.betolara1.model.Product;
import com.betolara1.model.Size;

public record SizeDTO (
    Long id,
    Product product,
    Double heightMax,
    Double heightMin,
    Double widthMax,
    Double widthMin,
    Double depthMax,
    Double depthMin){

    public SizeDTO(Size size) {
        this(size.getId(),
        size.getProduct(),
        size.getHeightMax(),
        size.getHeightMin(),
        size.getWidthMax(),
        size.getWidthMin(),
        size.getDepthMax(),
        size.getDepthMin());
    }
}