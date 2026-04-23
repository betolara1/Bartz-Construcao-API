package com.betolara1.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "module_child")
public class ModuleChild {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "id_module_father")
    private ModuleFather moduleFather;

    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    @PrePersist
    protected void onCreated(){
        this.dateCreated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.dateUpdated = LocalDateTime.now();
    }
}
