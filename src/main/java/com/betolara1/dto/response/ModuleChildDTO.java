package com.betolara1.dto.response;

import java.time.LocalDateTime;

import com.betolara1.model.ModuleChild;
import com.betolara1.model.ModuleFather;

public record ModuleChildDTO(
    Long id,
    String name,
    ModuleFather moduleFather,
    LocalDateTime dateCreated,
    LocalDateTime dateUpdated){

    public ModuleChildDTO(ModuleChild moduleChild) {
        this(moduleChild.getId(),
        moduleChild.getName(),
        moduleChild.getModuleFather(),
        moduleChild.getDateCreated(),
        moduleChild.getDateUpdated());
    }
}
