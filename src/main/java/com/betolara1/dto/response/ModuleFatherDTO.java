package com.betolara1.dto.response;

import java.time.LocalDateTime;

import com.betolara1.model.ModuleFather;

public record ModuleFatherDTO(
    Long id,
    String name,
    LocalDateTime dateCreated,
    LocalDateTime dateUpdated){

    public ModuleFatherDTO(ModuleFather moduleFather) {
        this(moduleFather.getId(),
        moduleFather.getName(),
        moduleFather.getDateCreated(),
        moduleFather.getDateUpdated());
    }
}
