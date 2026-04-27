package com.betolara1.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.betolara1.model.Size;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {
    @NonNull
    Page<Size> findAll(@NonNull Pageable pageable);

    @NonNull
    Optional<Size> findByProductId(Long idProduct);

    // Busca tudo que foi criado entre o início do dia (00:00:00) e o fim (23:59:59)
    Page<Size> findByDateCreatedBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    // Busca tudo que foi atualizado entre o início do dia (00:00:00) e o fim (23:59:59)
    Page<Size> findByDateUpdatedBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
