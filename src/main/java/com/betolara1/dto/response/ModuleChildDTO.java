package com.betolara1.dto.response;

import java.time.LocalDateTime;

import com.betolara1.model.ModuleChild;

public record ModuleChildDTO(
    Long id,
    String name,

    Long moduleFatherId,

    LocalDateTime dateCreated,
    LocalDateTime dateUpdated){

    public ModuleChildDTO(ModuleChild moduleChild) {
        this(moduleChild.getId(),
        moduleChild.getName(),
        moduleChild.getModuleFather().getId(),
        moduleChild.getDateCreated(),
        moduleChild.getDateUpdated());
    }
}
