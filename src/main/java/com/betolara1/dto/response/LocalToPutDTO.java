package com.betolara1.dto.response;

import java.time.LocalDateTime;

import com.betolara1.model.LocalToPut;

public record LocalToPutDTO (
    Long id,
    String name,
    LocalDateTime dateCreated,
    LocalDateTime dateUpdated){

    public LocalToPutDTO(LocalToPut localToPut){
        this(localToPut.getId(),
        localToPut.getName(),
        localToPut.getDateCreated(),
        localToPut.getDateUpdated());
    }
}
