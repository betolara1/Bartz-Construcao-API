package com.betolara1.dto.response;

import java.time.LocalDateTime;

import com.betolara1.model.ModuleChild;
import com.betolara1.model.ModuleFather;

import lombok.Data;

@Data
public class ModuleChildDTO {
    private Long id;
    private String name;
    private ModuleFather moduleFather;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    public ModuleChildDTO() {}

    public ModuleChildDTO(ModuleChild moduleChild) {
        this.id = moduleChild.getId();
        this.name = moduleChild.getName();
        this.moduleFather = moduleChild.getModuleFather();
        this.dateCreated = moduleChild.getDateCreated();
        this.dateUpdated = moduleChild.getDateUpdated();
    }
}
