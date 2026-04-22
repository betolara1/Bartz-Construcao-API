package com.betolara1.dto;

import java.time.LocalDateTime;

import com.betolara1.model.LocalToPut;

import lombok.Data;

@Data
public class LocalToPutDTO {
    private Long id;
    private String name;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    public LocalToPutDTO(){}

    public LocalToPutDTO(LocalToPut localToPut){
        this.id = localToPut.getId();
        this.name = localToPut.getName();
        this.dateCreated = localToPut.getDateCreated();
        this.dateUpdated = localToPut.getDateUpdated();
    }
}
