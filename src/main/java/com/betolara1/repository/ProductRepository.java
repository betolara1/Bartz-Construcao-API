package com.betolara1.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betolara1.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);
    Page<Product> findByIsActive(Boolean isActive, Pageable pageable);
    Page<Product> findByLocalToPut(Product.LocalToPut localToPut, Pageable pageable);
    Page<Product> findByIdModuleFather(Long idModuleFather, Pageable pageable);
    Page<Product> findByIdModuleChild(Long idModuleChild, Pageable pageable);
    Page<Product> findByIsActiveAndIdModuleFather(Boolean isActive, Long idModuleFather, Pageable pageable);
    Page<Product> findByIsActiveAndIdModuleChild(Boolean isActive, Long idModuleChild, Pageable pageable);
    Page<Product> findByIsActiveAndIdModuleFatherAndIdModuleChild(Boolean isActive, Long idModuleFather, Long idModuleChild, Pageable pageable);
    Page<Product> findByIsActiveAndLocalToPut(Boolean isActive, Product.LocalToPut localToPut, Pageable pageable);
    Page<Product> findByIsActiveAndLocalToPutAndIdModuleFather(Boolean isActive, Product.LocalToPut localToPut, Long idModuleFather, Pageable pageable);
    Page<Product> findByIsActiveAndLocalToPutAndIdModuleChild(Boolean isActive, Product.LocalToPut localToPut, Long idModuleChild, Pageable pageable);
    Page<Product> findByIsActiveAndLocalToPutAndIdModuleFatherAndIdModuleChild(Boolean isActive, Product.LocalToPut localToPut, Long idModuleFather, Long idModuleChild, Pageable pageable);
}
