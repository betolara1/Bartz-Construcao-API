package com.betolara1.SiteConstrutor.controller;

// Importações dos models, DTOs e services do projeto
import com.betolara1.controller.ProductController;
import com.betolara1.dto.response.ProductDTO;
import com.betolara1.exception.NotFoundException;
import com.betolara1.model.LocalToPut;
import com.betolara1.model.ModuleChild;
import com.betolara1.model.ModuleFather;
import com.betolara1.model.Product;
import com.betolara1.service.ProductService;

// Importações do Spring Test para simular requisições HTTP
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// @WebMvcTest carrega APENAS o controller especificado (não sobe toda a aplicação).
// Isso torna o teste muito mais rápido do que subir o Spring Boot inteiro.
@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    // MockMvc permite simular requisições HTTP (GET, POST, PUT, DELETE)
    // sem precisar de um servidor real rodando.
    @Autowired
    private MockMvc mockMvc;

    // Cria um "dublê" (mock) do service para que o controller use no lugar do service real
    @MockitoBean
    private ProductService productService;

    // ==================== TESTES DO GET /product/id ====================

    @Test
    void findById_deveRetornar200_quandoProdutoExistir() throws Exception {
        // PREPARAR: Cria um produto fictício e configura o mock do service
        Product product = new Product();
        product.setId(1L);
        product.setName("Mesa de Escritório");
        product.setTypeProduct("mesa");
        product.setIsActive(true);
        product.setLocalToPut(new LocalToPut());
        product.setModuleFather(new ModuleFather());
        product.setModuleChild(new ModuleChild());

        // Cria o DTO a partir do produto (é o que o controller retorna)
        ProductDTO dto = new ProductDTO(product);

        // Ensina o mock do service: "quando chamarem findById(1), retorne este DTO"
        when(productService.findById(1L)).thenReturn(dto);

        // EXECUTAR E VERIFICAR: Envia uma requisição GET e verifica a resposta
        mockMvc.perform(get("/product/id")         // Simula GET /product/id
                .param("id", "1"))                  // Adiciona o parâmetro ?id=1
                .andExpect(status().isOk())          // Espera status HTTP 200
                .andExpect(jsonPath("$.id").value(1)) // Verifica se o JSON contém id=1
                .andExpect(jsonPath("$.name").value("Mesa de Escritório")); // Verifica o nome
    }

    @Test
    void findById_deveRetornar404_quandoProdutoNaoExistir() throws Exception {
        // PREPARAR: Ensina o mock a lançar NotFoundException
        when(productService.findById(999L)).thenThrow(new NotFoundException("Produto não encontrado"));

        // EXECUTAR E VERIFICAR: Espera que retorne 404
        mockMvc.perform(get("/product/id")
                .param("id", "999"))
                .andExpect(status().isNotFound()); // Espera status HTTP 404
    }

    // ==================== TESTES DO GET /product/all ====================

    @Test
    void findAll_deveRetornar200_comListaDeProdutos() throws Exception {
        // PREPARAR: Cria um produto e uma página com ele
        Product product = new Product();
        product.setId(1L);
        product.setName("Mesa");
        product.setLocalToPut(new LocalToPut());
        product.setModuleFather(new ModuleFather());
        product.setModuleChild(new ModuleChild());

        Page<ProductDTO> page = new PageImpl<>(List.of(new ProductDTO(product)));

        // Ensina o mock a retornar a página para qualquer Pageable
        when(productService.findAll(any(Pageable.class))).thenReturn(page);

        // EXECUTAR E VERIFICAR
        mockMvc.perform(get("/product/all"))
                .andExpect(status().isOk()) // Espera HTTP 200
                .andExpect(jsonPath("$.content[0].name").value("Mesa")); // Verifica o nome no JSON
    }

    // ==================== TESTES DO POST /product ====================

    @Test
    void save_deveRetornar201_quandoDadosForemValidos() throws Exception {
        // PREPARAR: Cria o produto que será retornado após o salvamento
        Product product = new Product();
        product.setId(1L);
        product.setName("Mesa Nova");
        product.setTypeProduct("mesa");
        product.setIsActive(true);

        // Ensina o mock a retornar o produto salvo
        when(productService.save(any())).thenReturn(product);

        // JSON representando o corpo da requisição POST
        String json = """
                {
                    "name": "Mesa Nova",
                    "typeProduct": "mesa",
                    "localToPut": {"id": 1},
                    "moduleFather": {"id": 1},
                    "moduleChild": {"id": 1},
                    "isActive": true
                }
                """;

        // EXECUTAR E VERIFICAR
        mockMvc.perform(post("/product")                // Simula POST /product
                .contentType(MediaType.APPLICATION_JSON) // Define o tipo do corpo como JSON
                .content(json))                          // Envia o JSON no corpo da requisição
                .andExpect(status().isCreated())          // Espera HTTP 201 (Created)
                .andExpect(jsonPath("$.id").value(1))     // Verifica o ID no JSON de resposta
                .andExpect(jsonPath("$.name").value("Mesa Nova")); // Verifica o nome
    }

    // ==================== TESTES DO PUT /product/{id} ====================

    @Test
    void update_deveRetornar200_quandoAtualizarComSucesso() throws Exception {
        // PREPARAR: Cria o produto atualizado que será retornado
        Product product = new Product();
        product.setId(1L);
        product.setName("Mesa Atualizada");
        product.setTypeProduct("mesa");
        product.setIsActive(true);

        // Ensina o mock a retornar o produto atualizado
        when(productService.update(eq(1L), any())).thenReturn(product);

        // JSON representando a atualização
        String json = """
                {
                    "name": "Mesa Atualizada"
                }
                """;

        // EXECUTAR E VERIFICAR
        mockMvc.perform(put("/product/1")                // Simula PUT /product/1
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())                // Espera HTTP 200
                .andExpect(jsonPath("$.name").value("Mesa Atualizada")); // Verifica o nome atualizado
    }

    @Test
    void update_deveRetornar404_quandoProdutoNaoExistir() throws Exception {
        // PREPARAR: Ensina o mock a lançar exceção para ID inexistente
        when(productService.update(eq(999L), any()))
                .thenThrow(new NotFoundException("Produto não encontrado"));

        String json = """
                {
                    "name": "Qualquer"
                }
                """;

        // EXECUTAR E VERIFICAR
        mockMvc.perform(put("/product/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound()); // Espera HTTP 404
    }

    // ==================== TESTES DO DELETE /product/{id} ====================

    @Test
    void delete_deveRetornar200_quandoProdutoExistir() throws Exception {
        // PREPARAR: Ensina o mock a não fazer nada (delete não retorna valor)
        doNothing().when(productService).delete(1L);

        // EXECUTAR E VERIFICAR
        mockMvc.perform(delete("/product/1"))    // Simula DELETE /product/1
                .andExpect(status().isOk());      // Espera HTTP 200
    }

    @Test
    void delete_deveRetornar404_quandoProdutoNaoExistir() throws Exception {
        // PREPARAR: Ensina o mock a lançar exceção
        doThrow(new NotFoundException("Produto não encontrado")).when(productService).delete(999L);

        // EXECUTAR E VERIFICAR
        mockMvc.perform(delete("/product/999"))
                .andExpect(status().isNotFound()); // Espera HTTP 404
    }
}
