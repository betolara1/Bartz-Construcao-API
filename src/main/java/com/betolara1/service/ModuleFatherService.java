package com.betolara1.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.betolara1.dto.ModuleFatherDTO;
import com.betolara1.dto.request.SaveModuleFatherRequest;
import com.betolara1.dto.request.UpdateModuleFatherRequest;
import com.betolara1.exception.NotFoundException;
import com.betolara1.model.ModuleFather;
import com.betolara1.repository.ModuleFatherRepository;

import jakarta.transaction.Transactional;

@Service
public class ModuleFatherService {

    // Injeção de dependência
    private ModuleFatherRepository moduleFatherRepository;
    public ModuleFatherService(ModuleFatherRepository moduleFatherRepository) {
        this.moduleFatherRepository = moduleFatherRepository;
    }

    // Método para buscar todos os módulos pais
    @Transactional // Transação de leitura
    public Page<ModuleFatherDTO> findAll(int page, int size) {
        Page<ModuleFather> moduleFathers = moduleFatherRepository.findAll(PageRequest.of(page, size));

        if (moduleFathers.isEmpty()) {
            throw new NotFoundException("Nenhum módulo pai registrado.");
        }
        return moduleFathers.map(ModuleFatherDTO::new);
    }

    // Método para buscar um módulo pai por ID
    @Transactional // Transação de leitura
    public ModuleFatherDTO getModuleFatherById(Long id){
        ModuleFather moduleFather = moduleFatherRepository.findById(id).orElseThrow(() -> new NotFoundException("Módulo pai não encontrado com ID: " + id));
        return new ModuleFatherDTO(moduleFather);
    }

    // Método para buscar um módulo pai por nome
    @Transactional // Transação de leitura
    public ModuleFatherDTO getModuleFatherByName(String name){
        ModuleFather moduleFather = moduleFatherRepository.findByName(name).orElseThrow(() -> new NotFoundException("Módulo pai não encontrado com nome: " + name));
        return new ModuleFatherDTO(moduleFather);
    }

    // Método para buscar um módulo pai por data de criação
    @Transactional // Transação de leitura
    public ModuleFatherDTO getModuleFatherByDateCreated(LocalDateTime dateCreated){
        ModuleFather moduleFather = moduleFatherRepository.findByDateCreated(dateCreated).orElseThrow(() -> new NotFoundException("Módulo pai não encontrado com data de criação: " + dateCreated));
        return new ModuleFatherDTO(moduleFather);
    }

    // Método para salvar um módulo pai
    @Transactional // Transação de escrita
    public ModuleFather save(SaveModuleFatherRequest request) {
        ModuleFather moduleFather = new ModuleFather();

        moduleFather.setName(request.getName());
        moduleFather.setDateCreated(request.getDateCreated());

        return moduleFatherRepository.save(moduleFather);
    }

    // Método para atualizar um módulo pai
    @Transactional // Transação de escrita
    public ModuleFather update(Long id, UpdateModuleFatherRequest request){
        ModuleFather moduleFather = moduleFatherRepository.findById(id).orElseThrow(() -> new NotFoundException("Módulo pai não encontrado com ID: " + id));

        if(request.getName() != null){
            moduleFather.setName(request.getName());
        }

        if(request.getDateUpdated() != null){
            moduleFather.setDateUpdated(request.getDateUpdated());
        }

        return moduleFatherRepository.save(moduleFather);
    }

    // Método para deletar um módulo pai
    @Transactional // Transação de escrita
    public void delete(Long id) {
        if (!moduleFatherRepository.existsById(id)) {
            throw new NotFoundException("Módulo pai não encontrado com ID: " + id);
        }
        moduleFatherRepository.deleteById(id);
    }
}
