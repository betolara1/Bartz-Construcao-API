package com.betolara1.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.betolara1.dto.request.SaveSizeRequest;
import com.betolara1.dto.request.UpdateSizeRequest;
import com.betolara1.dto.response.SizeDTO;
import com.betolara1.exception.NotFoundException;
import com.betolara1.model.Size;
import com.betolara1.repository.SizeRepository;
import com.betolara1.util.DateUtils;

import org.springframework.transaction.annotation.Transactional;

@Service
public class SizeService {
    private final SizeRepository sizeRepository;
    public SizeService(SizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }

    @Transactional(readOnly = true)
    public Page<SizeDTO> findAll(Pageable pageable){
        Page<Size> sizes = sizeRepository.findAll(pageable);

        return sizes.map(SizeDTO::new);
    }

    @Transactional(readOnly = true)
    public SizeDTO findById(Long id){
        Size size = sizeRepository.findById(id).orElseThrow(() -> new NotFoundException("Tamanho não encontrado com ID: " + id));
        return new SizeDTO(size);
    }

    @Transactional(readOnly = true)
    public SizeDTO findByIdProduct(Long idProduct){
        Size size = sizeRepository.findByProductId(idProduct).orElseThrow(() -> new NotFoundException("Tamanho não encontrado com ID do produto: " + idProduct));
        return new SizeDTO(size);
    }

        // Método para buscar produtos por data de criação
    @Transactional(readOnly = true)
    public Page<SizeDTO> getSizeByDateCreated(String dateString, Pageable pageable){
        // 1. Converte a String para LocalDate (apenas data)
        LocalDate date = DateUtils.parseDate(dateString);

        // 2. Cria o início do dia (00:00:00) e o fim do dia (23:59:59)
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);

        // 3. Chama o repositório com o intervalo
        Page<Size> sizes = sizeRepository.findByDateCreatedBetween(startDay, endDay, pageable);

        // 4. Verifica se algum módulo pai foi encontrado
        if(sizes.isEmpty()){
            throw new NotFoundException("Nenhum tamanho encontrado na data: " + dateString);
        }

        // 5. Retorna os módulos pais
        return sizes.map(SizeDTO::new);
    }


    // Método para buscar produtos por data de atualização
    @Transactional(readOnly = true)
    public Page<SizeDTO> getSizeByDateUpdated(String dateString, Pageable pageable){
        // 1. Converte a String para LocalDate (apenas data)
        LocalDate date = DateUtils.parseDate(dateString);

        // 2. Cria o início do dia (00:00:00) e o fim do dia (23:59:59)
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);

        // 3. Chama o repositório com o intervalo
        Page<Size> sizes = sizeRepository.findByDateUpdatedBetween(startDay, endDay, pageable);

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
        size.setProduct(saveSizeRequest.getProduct());
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

        if(updateSizeRequest.getProduct() != null){
            size.setProduct(updateSizeRequest.getProduct());
        }
        if(updateSizeRequest.getHeightMax() != null){
            size.setHeightMax(updateSizeRequest.getHeightMax());
        }
        if(updateSizeRequest.getHeightMin() != null){
            size.setHeightMin(updateSizeRequest.getHeightMin());
        }
        if(updateSizeRequest.getWidthMax() != null){
            size.setWidthMax(updateSizeRequest.getWidthMax());
        }
        if(updateSizeRequest.getWidthMin() != null){
            size.setWidthMin(updateSizeRequest.getWidthMin());
        }
        if(updateSizeRequest.getDepthMax() != null){
            size.setDepthMax(updateSizeRequest.getDepthMax());
        }       
        if(updateSizeRequest.getDepthMin() != null){
            size.setDepthMin(updateSizeRequest.getDepthMin());
        }
        
        return sizeRepository.save(size);
    }

    @Transactional
    public void delete(Long id){
        Size size = sizeRepository.findById(id).orElseThrow(() -> new NotFoundException("Tamanho não encontrado com ID: " + id));
        sizeRepository.delete(size);
    }
}
