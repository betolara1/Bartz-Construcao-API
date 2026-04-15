package com.betolara1.dto;

import java.time.LocalDateTime;

import com.betolara1.model.ModuleFather;

import lombok.Data;

@Data
public class ModuleFatherDTO {
    private Long id;
    private String name;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    public ModuleFatherDTO() {}

    public ModuleFatherDTO(ModuleFather moduleFather) {
        this.id = moduleFather.getId();
        this.name = moduleFather.getName();
        this.dateCreated = moduleFather.getDateCreated();
        this.dateUpdated = moduleFather.getDateUpdated();
    }

    public ModuleFatherDTO orElseThrow(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
    }
}
