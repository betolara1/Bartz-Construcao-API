package com.betolara1.repository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.betolara1.model.ModuleFather;

@Repository
public interface ModuleFatherRepository extends JpaRepository<ModuleFather, Long> {
    // Busca todos os módulos pais
    @NonNull // Indica que o retorno não pode ser nulo
    Page<ModuleFather> findAll(@NonNull Pageable pageable);

    // Busca um módulo pai por nome
    Optional<ModuleFather> findByName(String name);

    // Busca tudo que foi criado entre o início do dia (00:00:00) e o fim (23:59:59)
    Page<ModuleFather> findByDateCreatedBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    // Busca tudo que foi atualizado entre o início do dia (00:00:00) e o fim (23:59:59)
    Page<ModuleFather> findByDateUpdatedBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
