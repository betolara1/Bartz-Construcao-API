package com.betolara1.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.betolara1.dto.ModuleFatherDTO;
import com.betolara1.dto.request.SaveModuleFatherRequest;
import com.betolara1.dto.request.UpdateModuleFatherRequest;
import com.betolara1.exception.NotFoundException;
import com.betolara1.model.ModuleFather;
import com.betolara1.repository.ModuleFatherRepository;
import com.betolara1.util.DateUtils;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ModuleFatherService {

    // Injeção de dependência
    private ModuleFatherRepository moduleFatherRepository;
    public ModuleFatherService(ModuleFatherRepository moduleFatherRepository) {
        this.moduleFatherRepository = moduleFatherRepository;
    }


    // Método para buscar todos os módulos pais
    @Transactional(readOnly = true)
    public Page<ModuleFatherDTO> findAll(int page, int size) {
        Page<ModuleFather> moduleFathers = moduleFatherRepository.findAll(PageRequest.of(page, size));

        return moduleFathers.map(ModuleFatherDTO::new);
    }


    // Método para buscar um módulo pai por ID
    @Transactional(readOnly = true)
    public ModuleFatherDTO getModuleFatherById(Long id){
        ModuleFather moduleFather = moduleFatherRepository.findById(id).orElseThrow(() -> new NotFoundException("Módulo pai não encontrado com ID: " + id));
        return new ModuleFatherDTO(moduleFather);
    }


    // Método para buscar um módulo pai por nome
    @Transactional(readOnly = true)
    public ModuleFatherDTO getModuleFatherByName(String name){
        ModuleFather moduleFather = moduleFatherRepository.findByName(name).orElseThrow(() -> new NotFoundException("Módulo pai não encontrado com nome: " + name));
        return new ModuleFatherDTO(moduleFather);
    }


    // Método para buscar um módulo pai por data de criação
    @Transactional(readOnly = true)
    public Page<ModuleFatherDTO> getModuleFatherByDateCreated(String dateString, int page, int size){
        // 1. Converte a String para LocalDate (apenas data)
        LocalDate date = DateUtils.parseDate(dateString);

        // 2. Cria o início do dia (00:00:00) e o fim do dia (23:59:59)
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);

        // 3. Chama o repositório com o intervalo
        Page<ModuleFather> moduleFather = moduleFatherRepository.findByDateCreatedBetween(startDay, endDay, PageRequest.of(page, size));

        // 4. Verifica se algum módulo pai foi encontrado
        if(moduleFather.isEmpty()){
            throw new NotFoundException("Nenhum módulo pai encontrado na data: " + dateString);
        }

        // 5. Retorna os módulos pais
        return moduleFather.map(ModuleFatherDTO::new);
    }


    // Método para buscar um módulo pai por data de atualização
    @Transactional(readOnly = true)
    public Page<ModuleFatherDTO> getModuleFatherByDateUpdated(String dateString, int page, int size){
        // 1. Converte a String para LocalDate (apenas data)
        LocalDate date = DateUtils.parseDate(dateString);

        // 2. Cria o início do dia (00:00:00) e o fim do dia (23:59:59)
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);

        // 3. Chama o repositório com o intervalo
        Page<ModuleFather> moduleFather = moduleFatherRepository.findByDateUpdatedBetween(startDay, endDay, PageRequest.of(page, size));

        // 4. Verifica se algum módulo pai foi encontrado
        if(moduleFather.isEmpty()){
            throw new NotFoundException("Nenhum módulo pai encontrado na data: " + dateString);
        }
        
        // 5. Retorna os módulos pais
        return moduleFather.map(ModuleFatherDTO::new);
    }


    // Método para salvar um módulo pai
    @Transactional
    public ModuleFather save(SaveModuleFatherRequest request) {
        ModuleFather moduleFather = new ModuleFather();

        moduleFather.setName(request.getName());

        return moduleFatherRepository.save(moduleFather);
    }


    // Método para atualizar um módulo pai
    @Transactional
    public ModuleFather update(Long id, UpdateModuleFatherRequest request){
        ModuleFather moduleFather = moduleFatherRepository.findById(id).orElseThrow(() -> new NotFoundException("Módulo pai não encontrado com ID: " + id));

        if(request.getName() != null){
            moduleFather.setName(request.getName());
        }

        return moduleFatherRepository.save(moduleFather);
    }


    // Método para deletar um módulo pai
    @Transactional
    public void delete(Long id) {
        if (!moduleFatherRepository.existsById(id)) {
            throw new NotFoundException("Módulo pai não encontrado com ID: " + id);
        }
        moduleFatherRepository.deleteById(id);
    }
}
