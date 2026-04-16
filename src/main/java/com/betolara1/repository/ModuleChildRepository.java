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

    Page<ModuleChild> findByIdModuleFather(Long id, Pageable pageable);

    Optional<ModuleChild> findByName(String name);

    // Busca tudo que foi criado entre o início do dia (00:00:00) e o fim (23:59:59)
    Page<ModuleChild> findByDateCreatedBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    // Busca tudo que foi atualizado entre o início do dia (00:00:00) e o fim (23:59:59)
    Page<ModuleChild> findByDateUpdatedBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
