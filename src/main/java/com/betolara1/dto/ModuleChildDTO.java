package com.betolara1.dto;

import java.time.LocalDateTime;

import com.betolara1.model.ModuleChild;

import lombok.Data;

@Data
public class ModuleChildDTO {
    private Long id;
    private String name;
    private Long idModuleFather;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    public ModuleChildDTO() {}

    public ModuleChildDTO(ModuleChild moduleChild) {
        this.id = moduleChild.getId();
        this.name = moduleChild.getName();
        this.idModuleFather = moduleChild.getIdModuleFather();
        this.dateCreated = moduleChild.getDateCreated();
        this.dateUpdated = moduleChild.getDateUpdated();
    }
}
