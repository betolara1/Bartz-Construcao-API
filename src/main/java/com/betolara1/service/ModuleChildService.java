package com.betolara1.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.betolara1.dto.ModuleChildDTO;
import com.betolara1.dto.request.SaveModuleChildRequest;
import com.betolara1.dto.request.UpdateModuleChildRequest;
import com.betolara1.exception.NotFoundException;
import com.betolara1.model.ModuleChild;
import com.betolara1.repository.ModuleChildRepository;
import com.betolara1.util.DateUtils;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ModuleChildService {

    // Injeção de dependência
    private ModuleChildRepository moduleChildRepository;
    public ModuleChildService(ModuleChildRepository moduleChildRepository){
        this.moduleChildRepository = moduleChildRepository;
    }


    // Método para buscar todos os módulos filhos
    @Transactional(readOnly = true)
    public Page<ModuleChildDTO> findAll(int page, int size){
        // Busca todos os módulos filhos
        Page<ModuleChild> moduleChild = moduleChildRepository.findAll(PageRequest.of(page, size));

        // Retorna os módulos filhos
        return moduleChild.map(ModuleChildDTO::new); 
    }


    // Método para buscar um módulo filho por ID
    @Transactional(readOnly = true)
    public ModuleChildDTO getModuleChildById(Long id){
        ModuleChild moduleChild = moduleChildRepository.findById(id).orElseThrow(() -> new NotFoundException("Módulo filho não encontrado com ID: " + id));
        return new ModuleChildDTO(moduleChild);
    }


    // Método para buscar um módulo filho por nome
    @Transactional(readOnly = true)
    public ModuleChildDTO getModuleChildByName(String name){
        ModuleChild moduleChild = moduleChildRepository.findByName(name).orElseThrow(() -> new NotFoundException("Módulo filho não encontrado com nome: " + name));
        return new ModuleChildDTO(moduleChild);
    }

    // Método para buscar um módulo filho por ID do módulo pai
    @Transactional(readOnly = true)
    public Page<ModuleChildDTO> getModuleChildByIdModuleFather(Long id, int page, int size){
        Page<ModuleChild> moduleChild = moduleChildRepository.findByIdModuleFather(id, PageRequest.of(page, size));
        
        if(moduleChild.isEmpty()){
            throw new NotFoundException("Nenhum módulo filho encontrado com ID do módulo pai: " + id);
        }
        
        return moduleChild.map(ModuleChildDTO::new);
    }


    // Método para buscar um módulo filho por data de criação
    @Transactional(readOnly = true)
    public Page<ModuleChildDTO> getModuleChildByDateCreated(String dateString, int page, int size){

        // 1. Converte a String para LocalDate (apenas data)
        LocalDate date = DateUtils.parseDate(dateString);

        // 2. Cria o início do dia (00:00:00) e o fim do dia (23:59:59)
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);

        // 3. Chama o repositório com o intervalo
        Page<ModuleChild> moduleChild = moduleChildRepository.findByDateCreatedBetween(startDay, endDay, PageRequest.of(page, size));

        if(moduleChild.isEmpty()){
            throw new NotFoundException("Nenhum módulo filho encontrado com data de criação: " + date);
        }
        
        return moduleChild.map(ModuleChildDTO::new);
    }

    // Método para buscar um módulo pai por data de atualização
    @Transactional(readOnly = true)
    public Page<ModuleChildDTO> getModuleChildByDateUpdated(String dateString, int page, int size){
        // 1. Converte a String para LocalDate (apenas data)
        LocalDate date = DateUtils.parseDate(dateString);

        // 2. Cria o início do dia (00:00:00) e o fim do dia (23:59:59)
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);

        // 3. Chama o repositório com o intervalo
        Page<ModuleChild> moduleChild = moduleChildRepository.findByDateUpdatedBetween(startDay, endDay, PageRequest.of(page, size));

        // 4. Verifica se algum módulo pai foi encontrado
        if(moduleChild.isEmpty()){
            throw new NotFoundException("Nenhum módulo pai encontrado na data: " + dateString);
        }
        
        // 5. Retorna os módulos pais
        return moduleChild.map(ModuleChildDTO::new);
    }


    // Método para salvar um módulo filho
    @Transactional
    public ModuleChildDTO save(SaveModuleChildRequest request) { 
        ModuleChild module = new ModuleChild();

        module.setName(request.getName());
        module.setIdModuleFather(request.getIdModuleFather());

        return new ModuleChildDTO(moduleChildRepository.save(module));
    }


    // Método para atualizar um módulo filho
    @Transactional
    public ModuleChildDTO update(Long id, UpdateModuleChildRequest request) { 
        ModuleChild module = moduleChildRepository.findById(id).orElseThrow(() -> new NotFoundException("Módulo filho não encontrado com ID: " + id));
        
        if(request.getName() != null){
            module.setName(request.getName());
        }
        if(request.getIdModuleFather() != null){
            module.setIdModuleFather(request.getIdModuleFather());
        }

        return new ModuleChildDTO(moduleChildRepository.save(module));
    }


    // Método para deletar um módulo filho
    @Transactional
    public void delete(Long id) {
        if (!moduleChildRepository.existsById(id)) {
            throw new NotFoundException("Módulo filho não encontrado com ID: " + id);
        }
        moduleChildRepository.deleteById(id);
    }
}
