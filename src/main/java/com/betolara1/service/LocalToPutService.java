package com.betolara1.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.betolara1.dto.request.SaveLocalToPutRequest;
import com.betolara1.dto.request.UpdateLocalToPutRequest;
import com.betolara1.dto.response.LocalToPutDTO;
import com.betolara1.exception.NotFoundException;
import com.betolara1.model.LocalToPut;
import com.betolara1.repository.LocalToPutRepository;
import com.betolara1.util.DateUtils;

import org.springframework.transaction.annotation.Transactional;

@Service
public class LocalToPutService {
    
    private final LocalToPutRepository localToPutRepository;
    public LocalToPutService(LocalToPutRepository localToPutRepository) {
        this.localToPutRepository = localToPutRepository;
    }

    @Transactional(readOnly = true)
    public LocalToPutDTO findById(Long id){
        LocalToPut localToPut = localToPutRepository.findById(id).orElseThrow(() -> new NotFoundException("ID não Encontrado"));
        return new LocalToPutDTO(localToPut);
    }

    @Transactional(readOnly = true)
    public LocalToPutDTO findByName(String name){
        LocalToPut localToPut = localToPutRepository.findByName(name).orElseThrow(() -> new NotFoundException("Local não encontrado"));
        return new LocalToPutDTO(localToPut);
    }

    @Transactional(readOnly = true)
    public Page<LocalToPutDTO> findAll(Pageable pageable){
        Page<LocalToPut> localToPut = localToPutRepository.findAll(pageable);
        
        return localToPut.map(LocalToPutDTO::new);
    }

    
    // Método para buscar produtos por data de criação
    @Transactional(readOnly = true)
    public Page<LocalToPutDTO> getLocalToPutsByDateCreated(String dateString, Pageable pageable){
        // 1. Converte a String para LocalDate (apenas data)
        LocalDate date = DateUtils.parseDate(dateString);

        // 2. Cria o início do dia (00:00:00) e o fim do dia (23:59:59)
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);

        // 3. Chama o repositório com o intervalo
        Page<LocalToPut> localToPut = localToPutRepository.findByDateCreatedBetween(startDay, endDay, pageable);

        // 4. Verifica se algum módulo pai foi encontrado
        if(localToPut.isEmpty()){
            throw new NotFoundException("Nenhum tamanho encontrado na data: " + dateString);
        }

        // 5. Retorna os módulos pais
        return localToPut.map(LocalToPutDTO::new);
    }


    // Método para buscar produtos por data de atualização
    @Transactional(readOnly = true)
    public Page<LocalToPutDTO> getLocalToPutsByDateUpdated(String dateString, Pageable pageable){
        // 1. Converte a String para LocalDate (apenas data)
        LocalDate date = DateUtils.parseDate(dateString);

        // 2. Cria o início do dia (00:00:00) e o fim do dia (23:59:59)
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);

        // 3. Chama o repositório com o intervalo
        Page<LocalToPut> localToPut = localToPutRepository.findByDateUpdatedBetween(startDay, endDay, pageable);

        // 4. Verifica se algum módulo pai foi encontrado
        if(localToPut.isEmpty()){
            throw new NotFoundException("Nenhum tamanho encontrado na data: " + dateString);
        }
        
        // 5. Retorna os módulos pais
        return localToPut.map(LocalToPutDTO::new);
    }

    @Transactional
    public LocalToPutDTO save(SaveLocalToPutRequest request){
        LocalToPut localToPut = new LocalToPut();

        localToPut.setName(request.getName());

        return new LocalToPutDTO(localToPutRepository.save(localToPut));
    }

    @Transactional
    public LocalToPutDTO update(Long id, UpdateLocalToPutRequest request){
        LocalToPut localToPut = localToPutRepository.findById(id).orElseThrow(() -> new NotFoundException("Local não encontrado com esse ID:" +id));

        if(request.getName() != null){
            localToPut.setName(request.getName());
        }

        return new LocalToPutDTO(localToPutRepository.save(localToPut));
    }

    @Transactional
    public void delete(Long id){
        if(!localToPutRepository.existsById(id)){
            throw new NotFoundException("Local não encontrado com o ID: " +id);
        }
        localToPutRepository.deleteById(id);
    }
}
