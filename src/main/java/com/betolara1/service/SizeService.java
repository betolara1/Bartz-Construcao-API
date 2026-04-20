package com.betolara1.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.betolara1.dto.SizeDTO;
import com.betolara1.dto.request.SaveSizeRequest;
import com.betolara1.dto.request.UpdateSizeRequest;
import com.betolara1.exception.NotFoundException;
import com.betolara1.model.Size;
import com.betolara1.repository.SizeRepository;

import jakarta.transaction.Transactional;

@Service
public class SizeService {
    private final SizeRepository sizeRepository;
    public SizeService(SizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }

    @Transactional
    public Page<SizeDTO> findAll(int page, int size){
        Page<Size> sizes = sizeRepository.findAll(PageRequest.of(page, size));
        if(sizes.isEmpty()){
            throw new NotFoundException("Nenhum tamanho encontrado.");
        }

        return sizes.map(SizeDTO::new);
    }

    @Transactional
    public SizeDTO findById(Long id){
        Size size = sizeRepository.findById(id).orElseThrow(() -> new NotFoundException("Tamanho não encontrado com ID: " + id));
        return new SizeDTO(size);
    }

    @Transactional
    public SizeDTO findByProductId(Long productId){
        Size size = sizeRepository.findByProductId(productId).orElseThrow(() -> new NotFoundException("Tamanho não encontrado com ID do produto: " + productId));
        return new SizeDTO(size);
    }

        // Método para buscar produtos por data de criação
    @Transactional
    public Page<SizeDTO> getSizeByDateCreated(String dateString, int page, int size){
        // 1. Converte a String para LocalDate (apenas data)
        LocalDate date = parseDate(dateString);

        // 2. Cria o início do dia (00:00:00) e o fim do dia (23:59:59)
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);

        // 3. Chama o repositório com o intervalo
        Page<Size> sizes = sizeRepository.findByDateCreatedBetween(startDay, endDay, PageRequest.of(page, size));

        // 4. Verifica se algum módulo pai foi encontrado
        if(sizes.isEmpty()){
            throw new NotFoundException("Nenhum tamanho encontrado na data: " + dateString);
        }

        // 5. Retorna os módulos pais
        return sizes.map(SizeDTO::new);
    }


    // Método para buscar produtos por data de atualização
    @Transactional
    public Page<SizeDTO> getSizeByDateUpdated(String dateString, int page, int size){
        // 1. Converte a String para LocalDate (apenas data)
        LocalDate date = parseDate(dateString);

        // 2. Cria o início do dia (00:00:00) e o fim do dia (23:59:59)
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);

        // 3. Chama o repositório com o intervalo
        Page<Size> sizes = sizeRepository.findByDateUpdatedBetween(startDay, endDay, PageRequest.of(page, size));

        // 4. Verifica se algum módulo pai foi encontrado
        if(sizes.isEmpty()){
            throw new NotFoundException("Nenhum tamanho encontrado na data: " + dateString);
        }
        
        // 5. Retorna os módulos pais
        return sizes.map(SizeDTO::new);
    }

    @Transactional
    public Size save(SaveSizeRequest saveSizeRequest){
        Size size = new Size();
        size.setProductId(saveSizeRequest.getProductId());
        size.setHeightMax(saveSizeRequest.getHeightMax());
        size.setHeightMin(saveSizeRequest.getHeightMin());
        size.setWidthMax(saveSizeRequest.getWidthMax());
        size.setWidthMin(saveSizeRequest.getWidthMin());
        size.setDepthMax(saveSizeRequest.getDepthMax());
        size.setDepthMin(saveSizeRequest.getDepthMin());
        return sizeRepository.save(size);
    }

    @Transactional
    public Size update(Long id, UpdateSizeRequest updateSizeRequest){
        Size size = sizeRepository.findById(id).orElseThrow(() -> new NotFoundException("Tamanho não encontrado com ID: " + id));
        size.setProductId(updateSizeRequest.getProductId());
        size.setHeightMax(updateSizeRequest.getHeightMax());
        size.setHeightMin(updateSizeRequest.getHeightMin());
        size.setWidthMax(updateSizeRequest.getWidthMax());
        size.setWidthMin(updateSizeRequest.getWidthMin());
        size.setDepthMax(updateSizeRequest.getDepthMax());
        size.setDepthMin(updateSizeRequest.getDepthMin());
        return sizeRepository.save(size);
    }

    @Transactional
    public void delete(Long id){
        Size size = sizeRepository.findById(id).orElseThrow(() -> new NotFoundException("Tamanho não encontrado com ID: " + id));
        sizeRepository.delete(size);
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
