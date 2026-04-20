package com.betolara1.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.betolara1.dto.ModuleFatherDTO;
import com.betolara1.dto.ProductDTO;
import com.betolara1.dto.request.SaveProductRequest;
import com.betolara1.dto.request.UpdateProductRequest;
import com.betolara1.exception.NotFoundException;
import com.betolara1.model.ModuleFather;
import com.betolara1.model.Product;
import com.betolara1.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService {

    private ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    // Método para buscar todos os produtos
    @Transactional
    public Page<ProductDTO> findAll(int page, int size){
        Page<Product> product = productRepository.findAll(PageRequest.of(page, size));

        if(product.isEmpty()){
            throw new NotFoundException("Nenhum produto encontrado.");
        }

        return product.map(ProductDTO::new);
    }


    // Método para buscar produtos por tipo
    @Transactional
    public Page<ProductDTO> findByTypeProduct(String type, int page, int size){
        Page<Product> product = productRepository.findByTypeProduct(type, PageRequest.of(page, size));

        if(product.isEmpty()){
            throw new NotFoundException("Nenhum produto encontrado com o tipo: " + type);
        }

        return product.map(ProductDTO::new);
    }


    // Método para buscar produtos por ID
    @Transactional
    public ProductDTO findById(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Produto não encontrado com ID: " + id));
        return new ProductDTO(product);
    }


    // Método para buscar produtos por local de colocação
    @Transactional
    public Page<ProductDTO> findByLocalToPut(Product.LocalToPut localToPut, int page, int size){
        Page<Product> product = productRepository.findByLocalToPut(localToPut, PageRequest.of(page, size));

        if(product.isEmpty()){
            throw new NotFoundException("Nenhum produto encontrado com o local de colocação: " + localToPut);
        }

        return product.map(ProductDTO::new);
    }


    // Método para buscar produtos por ID do módulo pai
    @Transactional
    public Page<ProductDTO> findByIdModuleFather(Long idModuleFather, int page, int size){
        Page<Product> product = productRepository.findByIdModuleFather(idModuleFather, PageRequest.of(page, size));

        if(product.isEmpty()){
            throw new NotFoundException("Nenhum produto encontrado com o ID do módulo pai: " + idModuleFather);
        }

        return product.map(ProductDTO::new);
    }


    // Método para buscar produtos por ID do módulo filho
    @Transactional
    public Page<ProductDTO> findByIdModuleChild(Long idModuleChild, int page, int size){
        Page<Product> product = productRepository.findByIdModuleChild(idModuleChild, PageRequest.of(page, size));

        if(product.isEmpty()){
            throw new NotFoundException("Nenhum produto encontrado com o ID do módulo filho: " + idModuleChild);
        }

        return product.map(ProductDTO::new);
    }


    // Método para buscar produtos por status de atividade
    @Transactional
    public Page<ProductDTO> findByIsActive(Boolean isActive, int page, int size){
        Page<Product> product = productRepository.findByIsActive(isActive, PageRequest.of(page, size));

        if(product.isEmpty()){
            throw new NotFoundException("Nenhum produto encontrado com o status de atividade: " + isActive);
        }

        return product.map(ProductDTO::new);
    }


    // Método para buscar produtos por data de criação
    @Transactional
    public Page<ProductDTO> getProductByDateCreated(String dateString, int page, int size){
        // 1. Converte a String para LocalDate (apenas data)
        LocalDate date = parseDate(dateString);

        // 2. Cria o início do dia (00:00:00) e o fim do dia (23:59:59)
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);

        // 3. Chama o repositório com o intervalo
        Page<Product> product = productRepository.findByDateCreatedBetween(startDay, endDay, PageRequest.of(page, size));

        // 4. Verifica se algum módulo pai foi encontrado
        if(product.isEmpty()){
            throw new NotFoundException("Nenhum produto encontrado na data: " + dateString);
        }

        // 5. Retorna os módulos pais
        return product.map(ProductDTO::new);
    }


    // Método para buscar produtos por data de atualização
    @Transactional
    public Page<ProductDTO> getProductByDateUpdated(String dateString, int page, int size){
        // 1. Converte a String para LocalDate (apenas data)
        LocalDate date = parseDate(dateString);

        // 2. Cria o início do dia (00:00:00) e o fim do dia (23:59:59)
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);

        // 3. Chama o repositório com o intervalo
        Page<Product> product = productRepository.findByDateUpdatedBetween(startDay, endDay, PageRequest.of(page, size));

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

        if(request.getName() == null){
            throw new NotFoundException("Nome do produto não pode ser nulo");
        }
        if(request.getTypeProduct() == null){
            throw new NotFoundException("Tipo do produto não pode ser nulo");
        }
        if(request.getLocalToPut() == null){
            throw new NotFoundException("Local de colocação do produto não pode ser nulo");
        }
        if(request.getIdModuleFather() == null){
            throw new NotFoundException("ID do módulo pai não pode ser nulo");
        }
        if(request.getIdModuleChild() == null){
            throw new NotFoundException("ID do módulo filho não pode ser nulo");
        }
        if(request.getIsActive() == null){
            throw new NotFoundException("Status de atividade do produto não pode ser nulo");
        }
        if(request.getDateCreated() == null){
            throw new NotFoundException("Data de criação do produto não pode ser nula");
        }

        product.setName(request.getName());
        product.setTypeProduct(request.getTypeProduct());
        product.setLocalToPut(Product.LocalToPut.valueOf(request.getLocalToPut().name()));
        product.setIdModuleFather(request.getIdModuleFather());
        product.setIdModuleChild(request.getIdModuleChild());
        product.setIsActive(request.getIsActive());
        product.setDateCreated(request.getDateCreated());

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
        if(request.getLocalToPut() != null){
            product.setLocalToPut(Product.LocalToPut.valueOf(request.getLocalToPut().name()));
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
        
        product.setDateUpdated(request.getDateUpdated());

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
