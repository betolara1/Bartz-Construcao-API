package com.betolara1.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.betolara1.dto.ModuleChildDTO;
import com.betolara1.exception.NotFoundException;
import com.betolara1.model.ModuleChild;
import com.betolara1.repository.ModuleChildRepository;

import jakarta.transaction.Transactional;

@Service
public class ModuleChildService {

    // Injeção de dependência
    private ModuleChildRepository moduleChildRepository;
    public ModuleChildService(ModuleChildRepository moduleChildRepository){
        this.moduleChildRepository = moduleChildRepository;
    }

    // Método para buscar todos os módulos filhos
    @Transactional // Transação de leitura
    public Page<ModuleChildDTO> findAll(int page, int size){
        // Busca todos os módulos filhos
        Page<ModuleChild> moduleChild = moduleChildRepository.findAll(PageRequest.of(page, size));

        // Verifica se algum módulo filho foi encontrado
        if(moduleChild.isEmpty()){
            throw new NotFoundException("Nenhum módulo filho encontrado");
        }

        // Retorna os módulos filhos
        return moduleChild.map(ModuleChildDTO::new); 
    }

    // Método para buscar um módulo filho por ID
    @Transactional // Transação de leitura
    public ModuleChildDTO getModuleChildById(Long id){
        ModuleChild moduleChild = moduleChildRepository.findById(id).orElseThrow(() -> new NotFoundException("Módulo filho não encontrado com ID: " + id));
        return new ModuleChildDTO(moduleChild);
    }

    // Método para buscar um módulo filho por nome
    @Transactional // Transação de leitura
    public ModuleChildDTO getModuleChildByName(String name){
        ModuleChild moduleChild = moduleChildRepository.findByName(name).orElseThrow(() -> new NotFoundException("Módulo filho não encontrado com nome: " + name));
        return new ModuleChildDTO(moduleChild);
    }

    // Método para buscar um módulo filho por data de criação
    @Transactional // Transação de leitura
    public Page<ModuleChildDTO> getModuleChildByDateCreated(LocalDateTime date, int page, int size){
        Page<ModuleChild> moduleChild = moduleChildRepository.findByDateCreated(date, PageRequest.of(page, size));

        if(moduleChild.isEmpty()){
            throw new NotFoundException("Nenhum módulo filho encontrado com data de criação: " + date);
        }
        
        return moduleChild.map(ModuleChildDTO::new);
    }

}
