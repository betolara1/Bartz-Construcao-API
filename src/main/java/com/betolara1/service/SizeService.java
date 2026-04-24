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

import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j // CRIA O LOG 
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
        log.info("Salvando dimensões do produto: ");

        size.setProduct(saveSizeRequest.getProduct());
        size.setHeightMax(saveSizeRequest.getHeightMax());
        size.setHeightMin(saveSizeRequest.getHeightMin());
        size.setWidthMax(saveSizeRequest.getWidthMax());
        size.setWidthMin(saveSizeRequest.getWidthMin());
        size.setDepthMax(saveSizeRequest.getDepthMax());
        size.setDepthMin(saveSizeRequest.getDepthMin());
        
        Size saved = sizeRepository.save(size);
        log.info("Dimensões salvas do produto {}", saved.getId());

        return saved;
    }

    @Transactional
    public Size update(Long id, UpdateSizeRequest request){
        Size size = sizeRepository.findById(id).orElseThrow(() -> new NotFoundException("Tamanho não encontrado com ID: " + id));
        log.info("Alterando as dimensões do produto {} ", id);

        if(request.getProduct() != null){
            size.setProduct(request.getProduct());
        }
        if(request.getHeightMax() != null){
            size.setHeightMax(request.getHeightMax());
        }
        if(request.getHeightMin() != null){
            size.setHeightMin(request.getHeightMin());
        }
        if(request.getWidthMax() != null){
            size.setWidthMax(request.getWidthMax());
        }
        if(request.getWidthMin() != null){
            size.setWidthMin(request.getWidthMin());
        }
        if(request.getDepthMax() != null){
            size.setDepthMax(request.getDepthMax());
        }       
        if(request.getDepthMin() != null){
            size.setDepthMin(request.getDepthMin());
        }
        
        Size updated = sizeRepository.save(size);
        log.info("Dimensões do produto {} alterado.", id);
        
        return updated;
    }

    @Transactional
    public void delete(Long id){
        log.info("Deletando as dimensões do produto {} ", id);

        if(!sizeRepository.existsById(id)){
            throw new NotFoundException("Produto não encontrado com ID: " + id);
        }
        sizeRepository.deleteById(id);
        log.info("Dimensões do produto {} excluidas.", id);
    }
}
