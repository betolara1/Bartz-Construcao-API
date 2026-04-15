package com.betolara1.service;

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
import jakarta.validation.constraints.NotNull;

@Service
public class ModuleFatherService {

    private ModuleFatherRepository moduleFatherRepository;
    public ModuleFatherService(ModuleFatherRepository moduleFatherRepository) {
        this.moduleFatherRepository = moduleFatherRepository;
    }

    @Transactional
    public Page<ModuleFatherDTO> findAll(int page, int size) {
        Page<ModuleFather> moduleFathers = moduleFatherRepository.findAll(PageRequest.of(page, size));

        if (moduleFathers.isEmpty()) {
            throw new NotFoundException("Nenhum módulo pai registrado.");
        }
        return moduleFathers.map(ModuleFatherDTO::new);
    }

    @Transactional
    public ModuleFatherDTO getModuleFatherById(@NotNull Long id){
        ModuleFather moduleFather = moduleFatherRepository.findById(id).orElseThrow(() -> new NotFoundException("Módulo pai não encontrado com ID: " + id));
        return new ModuleFatherDTO(moduleFather);
    }

    @Transactional
    public ModuleFatherDTO getModuleFatherByName(@NotNull String name){
        ModuleFather moduleFather = moduleFatherRepository.findByName(name).orElseThrow(() -> new NotFoundException("Módulo pai não encontrado com nome: " + name));
        return new ModuleFatherDTO(moduleFather);
    }

    @Transactional
    public ModuleFatherDTO save(SaveModuleFatherRequest request) {
        ModuleFather moduleFather = new ModuleFather();

        moduleFather.setName(request.getName());
        moduleFather.setDateCreated(request.getDateCreated());

        return new ModuleFatherDTO(moduleFatherRepository.save(moduleFather));
    }

    @Transactional
    public ModuleFatherDTO update(@NotNull Long id, UpdateModuleFatherRequest request){
        ModuleFather moduleFather = moduleFatherRepository.findById(id).orElseThrow(() -> new NotFoundException("Módulo pai não encontrado com ID: " + id));

        if(request.getName() != null){
            moduleFather.setName(request.getName());
        }

        if(request.getDateUpdated() != null){
            moduleFather.setDateUpdated(request.getDateUpdated());
        }

        return new ModuleFatherDTO(moduleFatherRepository.save(moduleFather));
    }

    @Transactional
    public void delete(@NotNull Long id) {
        if (!moduleFatherRepository.existsById(id)) {
            throw new NotFoundException("Módulo pai não encontrado com ID: " + id);
        }
        moduleFatherRepository.deleteById(id);
    }
}
