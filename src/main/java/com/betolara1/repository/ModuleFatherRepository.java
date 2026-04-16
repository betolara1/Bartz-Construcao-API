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
    @NonNull
    Page<ModuleFather> findAll(@NonNull Pageable pageable);

    Optional<ModuleFather> findByName(String name);

    Optional<ModuleFather> findByDateCreated(LocalDateTime dateCreated);
}
