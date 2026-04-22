package com.betolara1.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.betolara1.dto.ProductDTO;
import com.betolara1.dto.request.SaveProductRequest;
import com.betolara1.dto.request.UpdateProductRequest;
import com.betolara1.exception.NotFoundException;
import com.betolara1.model.Product;
import com.betolara1.repository.ProductRepository;
import com.betolara1.util.DateUtils;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    // Método para buscar todos os produtos
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable){
        Page<Product> product = productRepository.findAll(pageable);

        return product.map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findByName(String name, Pageable pageable){
        Page<Product> product = productRepository.findByNameContainingIgnoreCase(name, pageable);
        if(product.isEmpty()){
            throw new NotFoundException("Nenhum produto encontrado com o nome: " + name);
        }
        return product.map(ProductDTO::new);
    }


    // Método para buscar produtos por tipo
    @Transactional(readOnly = true)
    public Page<ProductDTO> findByTypeProduct(String type, Pageable pageable){
        Page<Product> product = productRepository.findByTypeProduct(type, pageable);

        if(product.isEmpty()){
            throw new NotFoundException("Nenhum produto encontrado com o tipo: " + type);
        }

        return product.map(ProductDTO::new);
    }


    // Método para buscar produtos por ID
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Produto não encontrado com ID: " + id));
        return new ProductDTO(product);
    }


    // Método para buscar produtos por local de colocação
    @Transactional(readOnly = true)
    public Page<ProductDTO> findByLocalToPut(Long idLocalToPut, Pageable pageable){
        Page<Product> product = productRepository.findByIdLocalToPut(idLocalToPut, pageable);

        if(product.isEmpty()){
            throw new NotFoundException("Nenhum produto encontrado com o local de colocação: " + idLocalToPut);
        }

        return product.map(ProductDTO::new);
    }


    // Método para buscar produtos por ID do módulo pai
    @Transactional(readOnly = true)
    public Page<ProductDTO> findByIdModuleFather(Long idModuleFather, Pageable pageable){
        Page<Product> product = productRepository.findByIdModuleFather(idModuleFather, pageable);

        if(product.isEmpty()){
            throw new NotFoundException("Nenhum produto encontrado com o ID do módulo pai: " + idModuleFather);
        }

        return product.map(ProductDTO::new);
    }


    // Método para buscar produtos por ID do módulo filho
    @Transactional(readOnly = true)
    public Page<ProductDTO> findByIdModuleChild(Long idModuleChild, Pageable pageable){
        Page<Product> product = productRepository.findByIdModuleChild(idModuleChild, pageable);

        if(product.isEmpty()){
            throw new NotFoundException("Nenhum produto encontrado com o ID do módulo filho: " + idModuleChild);
        }

        return product.map(ProductDTO::new);
    }


    // Método para buscar produtos por status de atividade
    @Transactional(readOnly = true)
    public Page<ProductDTO> findByIsActive(Boolean isActive, Pageable pageable){
        Page<Product> product = productRepository.findByIsActive(isActive, pageable);

        if(product.isEmpty()){
            throw new NotFoundException("Nenhum produto encontrado com o status de atividade: " + isActive);
        }

        return product.map(ProductDTO::new);
    }


    // Método para buscar produtos por data de criação
    @Transactional(readOnly = true)
    public Page<ProductDTO> getProductByDateCreated(String dateString, Pageable pageable){
        // 1. Converte a String para LocalDate (apenas data)
        LocalDate date = DateUtils.parseDate(dateString);

        // 2. Cria o início do dia (00:00:00) e o fim do dia (23:59:59)
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);

        // 3. Chama o repositório com o intervalo
        Page<Product> product = productRepository.findByDateCreatedBetween(startDay, endDay, pageable);

        // 4. Verifica se algum módulo pai foi encontrado
        if(product.isEmpty()){
            throw new NotFoundException("Nenhum produto encontrado na data: " + dateString);
        }

        // 5. Retorna os módulos pais
        return product.map(ProductDTO::new);
    }


    // Método para buscar produtos por data de atualização
    @Transactional(readOnly = true)
    public Page<ProductDTO> getProductByDateUpdated(String dateString, Pageable pageable){
        // 1. Converte a String para LocalDate (apenas data)
        LocalDate date = DateUtils.parseDate(dateString);

        // 2. Cria o início do dia (00:00:00) e o fim do dia (23:59:59)
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);

        // 3. Chama o repositório com o intervalo
        Page<Product> product = productRepository.findByDateUpdatedBetween(startDay, endDay, pageable);

        // 4. Verifica se algum módulo pai foi encontrado
        if(product.isEmpty()){
            throw new NotFoundException("Nenhum produto encontrado na data: " + dateString);
        }
        
        // 5. Retorna os módulos pais
        return product.map(ProductDTO::new);
    }


    // Método para salvar um produto
    @Transactional
    public Product save(SaveProductRequest request) {
        Product product = new Product();

        product.setName(request.getName());
        product.setTypeProduct(request.getTypeProduct());
        product.setIdLocalToPut(request.getIdLocalToPut());
        product.setIdModuleFather(request.getIdModuleFather());
        product.setIdModuleChild(request.getIdModuleChild());
        product.setIsActive(request.getIsActive());

        return productRepository.save(product);
    }


    // Método para atualizar um produto
    @Transactional
    public Product update(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Produto não encontrado com ID: " + id));

        if(request.getName() != null){
            product.setName(request.getName());
        }
        if(request.getTypeProduct() != null){
            product.setTypeProduct(request.getTypeProduct());
        }
        if(request.getIdLocalToPut() != null){
            product.setIdLocalToPut(request.getIdLocalToPut());
        }
        if(request.getIdModuleFather() != null){
            product.setIdModuleFather(request.getIdModuleFather());
        }
        if(request.getIdModuleChild() != null){
            product.setIdModuleChild(request.getIdModuleChild());
        }
        if(request.getIsActive() != null){
            product.setIsActive(request.getIsActive());
        }

        return productRepository.save(product);
    }


    // Método para deletar um produto
    @Transactional
    public void delete(Long id){
        if(!productRepository.existsById(id)){
            throw new NotFoundException("Produto não encontrado com ID: " + id);
        }
        productRepository.deleteById(id);
    }
}
