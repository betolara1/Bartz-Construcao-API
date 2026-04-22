package com.betolara1.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.betolara1.dto.LocalToPutDTO;
import com.betolara1.dto.request.SaveLocalToPutRequest;
import com.betolara1.dto.request.UpdateLocalToPutRequest;
import com.betolara1.exception.NotFoundException;
import com.betolara1.model.LocalToPut;
import com.betolara1.repository.LocalToPutRepository;

import jakarta.transaction.Transactional;

@Service
public class LocalToPutService {
    
    private final LocalToPutRepository localToPutRepository;
    public LocalToPutService(LocalToPutRepository localToPutRepository) {
        this.localToPutRepository = localToPutRepository;
    }

    @Transactional
    public LocalToPutDTO findById(Long id){
        LocalToPut localToPut = localToPutRepository.findById(id).orElseThrow(() -> new NotFoundException("ID não Encontrado"));
        return new LocalToPutDTO(localToPut);
    }

    @Transactional
    public LocalToPutDTO findByName(String name){
        LocalToPut localToPut = localToPutRepository.findByName(name).orElseThrow(() -> new RuntimeException("Local não encontrado"));
        return new LocalToPutDTO(localToPut);
    }

    @Transactional
    public Page<LocalToPutDTO> findAll(int page, int size){
        Page<LocalToPut> localToPut = localToPutRepository.findAll(PageRequest.of(page, size));
        
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
        if(localToPutRepository.existsById(id)){
            throw new NotFoundException("Local não encontrado com o ID: " +id);
        }
        localToPutRepository.deleteById(id);
    }

    // Método para buscar produtos por data de criação
    @Transactional
    public Page<LocalToPutDTO> getLocalToPutsByDateCreated(String dateString, int page, int size){
        // 1. Converte a String para LocalDate (apenas data)
        LocalDate date = parseDate(dateString);

        // 2. Cria o início do dia (00:00:00) e o fim do dia (23:59:59)
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);

        // 3. Chama o repositório com o intervalo
        Page<LocalToPut> localToPut = localToPutRepository.findByDateCreatedBetween(startDay, endDay, PageRequest.of(page, size));

        // 4. Verifica se algum módulo pai foi encontrado
        if(localToPut.isEmpty()){
            throw new NotFoundException("Nenhum tamanho encontrado na data: " + dateString);
        }

        // 5. Retorna os módulos pais
        return localToPut.map(LocalToPutDTO::new);
    }


    // Método para buscar produtos por data de atualização
    @Transactional
    public Page<LocalToPutDTO> getLocalToPutsByDateUpdated(String dateString, int page, int size){
        // 1. Converte a String para LocalDate (apenas data)
        LocalDate date = parseDate(dateString);

        // 2. Cria o início do dia (00:00:00) e o fim do dia (23:59:59)
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);

        // 3. Chama o repositório com o intervalo
        Page<LocalToPut> localToPut = localToPutRepository.findByDateUpdatedBetween(startDay, endDay, PageRequest.of(page, size));

        // 4. Verifica se algum módulo pai foi encontrado
        if(localToPut.isEmpty()){
            throw new NotFoundException("Nenhum tamanho encontrado na data: " + dateString);
        }
        
        // 5. Retorna os módulos pais
        return localToPut.map(LocalToPutDTO::new);
    }


    // Método para converter String para LocalDate
    private LocalDate parseDate(String dateString){
        // Lista de formatos que você quer aceitar
        String[] formats = {"dd/MM/yyyy", "dd-MM-yyyy", "yyyy-MM-dd", "yyyy/MM/dd"};

        for(String format : formats){
            try{
                return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(format));
            }catch(DateTimeParseException e){
                continue;
            }
        }

        throw new IllegalArgumentException("Formato de data inválido: " + dateString);
    }
}
