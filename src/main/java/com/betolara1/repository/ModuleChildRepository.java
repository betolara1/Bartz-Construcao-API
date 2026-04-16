package com.betolara1.repository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.betolara1.model.ModuleChild;

@Repository
public interface ModuleChildRepository extends JpaRepository<ModuleChild, Long> {
    @NonNull
    Page<ModuleChild> findAll(Pageable pageable);

    Optional<ModuleChild> findByModuleFatherId(Long id);

    @NonNull
    Page<ModuleChild> findByDateCreated(LocalDateTime dateCreated, Pageable pageable);
    
    @NonNull
    Page<ModuleChild> findByDateUpdated(LocalDateTime dateUpdated, Pageable pageable);
}
