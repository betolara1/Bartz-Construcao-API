package com.betolara1.repository;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betolara1.model.ModuleChild;

@Repository
public interface ModuleChildRepository extends JpaRepository<ModuleChild, Long> {
    Page<ModuleChild> findAll(Pageable pageable);
    Page<ModuleChild> findAllModuleFather(Pageable pageable);
}
