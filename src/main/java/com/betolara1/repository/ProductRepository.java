package com.betolara1.repository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.betolara1.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @NonNull
    Page<Product> findAll(@NonNull Pageable pageable);

    //Busca pelo nome do produto
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Busca por tipo de produto
    Page<Product> findByTypeProduct(String typeProduct, Pageable pageable);
    
    // Busca por local de colocação
    Page<Product> findByLocalToPutId(Long idLocalToPut, Pageable pageable);
    
    // Busca por ID do módulo pai
    Page<Product> findByModuleFatherId(Long idModuleFather, Pageable pageable);
    
    // Busca por ID do módulo filho
    Page<Product> findByModuleChildId(Long idModuleChild, Pageable pageable);
    
    // Busca por status de atividade
    Page<Product> findByIsActive(Boolean isActive, Pageable pageable);

    // Busca tudo que foi criado entre o início do dia (00:00:00) e o fim (23:59:59)
    Page<Product> findByDateCreatedBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    // Busca tudo que foi atualizado entre o início do dia (00:00:00) e o fim (23:59:59)
    Page<Product> findByDateUpdatedBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);


    /*Page<Product> findByIsActiveAndIdModuleFather(Boolean isActive, Long idModuleFather, Pageable pageable);
    Page<Product> findByIsActiveAndIdModuleChild(Boolean isActive, Long idModuleChild, Pageable pageable);
    Page<Product> findByIsActiveAndIdModuleFatherAndIdModuleChild(Boolean isActive, Long idModuleFather, Long idModuleChild, Pageable pageable);
    Page<Product> findByIsActiveAndLocalToPut(Boolean isActive, Product.LocalToPut localToPut, Pageable pageable);
    Page<Product> findByIsActiveAndLocalToPutAndIdModuleFather(Boolean isActive, Product.LocalToPut localToPut, Long idModuleFather, Pageable pageable);
    Page<Product> findByIsActiveAndLocalToPutAndIdModuleChild(Boolean isActive, Product.LocalToPut localToPut, Long idModuleChild, Pageable pageable);
    Page<Product> findByIsActiveAndLocalToPutAndIdModuleFatherAndIdModuleChild(Boolean isActive, Product.LocalToPut localToPut, Long idModuleFather, Long idModuleChild, Pageable pageable);*/
}
