package com.betolara1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betolara1.model.Size;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {
    
}
