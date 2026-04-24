package com.betolara1.SiteConstrutor.service;

// Importações dos models e DTOs do projeto
import com.betolara1.model.Product;
import com.betolara1.model.LocalToPut;
import com.betolara1.model.ModuleFather;
import com.betolara1.model.ModuleChild;
import com.betolara1.dto.request.SaveProductRequest;
import com.betolara1.dto.request.UpdateProductRequest;
import com.betolara1.dto.response.ProductDTO;
import com.betolara1.exception.NotFoundException;
import com.betolara1.repository.ProductRepository;
import com.betolara1.service.ProductService;

// Importações do JUnit 5 para asserções e anotações de teste
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Importações do Mockito para criar mocks e simular comportamentos
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

// Habilita o uso de Mocks do Mockito nesta classe de teste
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    // Cria um "dublê" (mock) do repositório — NÃO conecta no banco de dados real
    @Mock
    private ProductRepository productRepository;

    // Injeta o mock do repositório dentro do service real que queremos testar
    @InjectMocks
    private ProductService productService;

    // ==================== TESTES DO findAll ====================

    @Test
    void findAll_deveRetornarPaginaDeProductDTO() {
        // PREPARAR: Cria um produto fictício
        Product product = new Product();
        product.setId(1L);
        product.setName("Mesa de Escritório");
        product.setTypeProduct("mesa");
        product.setIsActive(true);

        // Cria uma página contendo apenas esse produto (simulando o resultado do banco)
        Page<Product> page = new PageImpl<>(List.of(product));

        // Cria o objeto de paginação (página 0, com 10 itens por página)
        Pageable pageable = PageRequest.of(0, 10);

        // Ensina o mock a retornar a página quando chamarem findAll
        when(productRepository.findAll(pageable)).thenReturn(page);

        // EXECUTAR: Chama o método findAll do service
        Page<ProductDTO> result = productService.findAll(pageable);

        // VERIFICAR: Confere se retornou 1 item e se o nome está correto
        assertEquals(1, result.getTotalElements()); // Deve ter 1 produto na página
        assertEquals("Mesa de Escritório", result.getContent().get(0).name()); // Nome do 1° item
    }

    // ==================== TESTES DO findById ====================

    @Test
    void findById_deveRetornarProductDTO_quandoIdExistir() {
        // PREPARAR: Cria um produto fictício com dados completos
        Product product = new Product();
        product.setId(1L);
        product.setName("Mesa de Escritório");
        product.setTypeProduct("mesa");
        product.setIsActive(true);

        // Ensina o mock: "quando pedirem o ID 1, retorne este produto"
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // EXECUTAR: Chama o método real do service
        ProductDTO result = productService.findById(1L);

        // VERIFICAR: Confere cada campo retornado
        assertNotNull(result); // Garante que o resultado não é nulo
        assertEquals(1L, result.id()); // Verifica o ID
        assertEquals("Mesa de Escritório", result.name()); // Verifica o nome
        assertEquals("mesa", result.typeProduct()); // Verifica o tipo
        assertEquals(true, result.isActive()); // Verifica se está ativo
    }

    @Test
    void findById_deveLancarNotFoundException_quandoIdNaoExistir() {
        // PREPARAR: Ensina o mock a retornar "vazio" para o ID 999
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // EXECUTAR E VERIFICAR: Garante que a exceção NotFoundException é lançada
        assertThrows(NotFoundException.class, () -> productService.findById(999L));
    }

    // ==================== TESTES DO findByName ====================

    @Test
    void findByName_deveRetornarPagina_quandoNomeExistir() {
        // PREPARAR: Cria um produto com o nome que vamos buscar
        Product product = new Product();
        product.setId(1L);
        product.setName("Mesa de Escritório");

        Page<Product> page = new PageImpl<>(List.of(product));
        Pageable pageable = PageRequest.of(0, 10);

        // Ensina o mock a retornar resultados quando buscarem por "Mesa"
        when(productRepository.findByNameContainingIgnoreCase("Mesa", pageable)).thenReturn(page);

        // EXECUTAR: Busca pelo nome parcial "Mesa"
        Page<ProductDTO> result = productService.findByName("Mesa", pageable);

        // VERIFICAR: Confere se encontrou 1 resultado
        assertEquals(1, result.getTotalElements());
        assertEquals("Mesa de Escritório", result.getContent().get(0).name());
    }

    @Test
    void findByName_deveLancarNotFoundException_quandoNomeNaoExistir() {
        // PREPARAR: Ensina o mock a retornar uma página VAZIA (nenhum resultado)
        Page<Product> emptyPage = new PageImpl<>(Collections.emptyList());
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findByNameContainingIgnoreCase("NaoExiste", pageable)).thenReturn(emptyPage);

        // EXECUTAR E VERIFICAR: Garante que lança exceção para nome inexistente
        assertThrows(NotFoundException.class, () -> productService.findByName("NaoExiste", pageable));
    }

    // ==================== TESTES DO findByTypeProduct ====================

    @Test
    void findByTypeProduct_deveRetornarPagina_quandoTipoExistir() {
        // PREPARAR: Cria produto do tipo "mesa"
        Product product = new Product();
        product.setId(1L);
        product.setTypeProduct("mesa");

        Page<Product> page = new PageImpl<>(List.of(product));
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findByTypeProduct("mesa", pageable)).thenReturn(page);

        // EXECUTAR: Busca pelo tipo "mesa"
        Page<ProductDTO> result = productService.findByTypeProduct("mesa", pageable);

        // VERIFICAR: Confere se o tipo do primeiro resultado está correto
        assertEquals(1, result.getTotalElements());
        assertEquals("mesa", result.getContent().get(0).typeProduct());
    }

    @Test
    void findByTypeProduct_deveLancarNotFoundException_quandoTipoNaoExistir() {
        // PREPARAR: Retorna página vazia para tipo inexistente
        Page<Product> emptyPage = new PageImpl<>(Collections.emptyList());
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findByTypeProduct("inexistente", pageable)).thenReturn(emptyPage);

        // EXECUTAR E VERIFICAR
        assertThrows(NotFoundException.class, () -> productService.findByTypeProduct("inexistente", pageable));
    }

    // ==================== TESTES DO findByLocalToPut ====================

    @Test
    void findByLocalToPut_deveRetornarPagina_quandoLocalExistir() {
        // PREPARAR: Cria produto com local de colocação
        Product product = new Product();
        product.setId(1L);

        Page<Product> page = new PageImpl<>(List.of(product));
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findByLocalToPutId(1L, pageable)).thenReturn(page);

        // EXECUTAR
        Page<ProductDTO> result = productService.findByLocalToPut(1L, pageable);

        // VERIFICAR
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findByLocalToPut_deveLancarNotFoundException_quandoLocalNaoExistir() {
        Page<Product> emptyPage = new PageImpl<>(Collections.emptyList());
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findByLocalToPutId(999L, pageable)).thenReturn(emptyPage);

        assertThrows(NotFoundException.class, () -> productService.findByLocalToPut(999L, pageable));
    }

    // ==================== TESTES DO findByIdModuleFather ====================

    @Test
    void findByIdModuleFather_deveRetornarPagina_quandoModuleFatherExistir() {
        Product product = new Product();
        product.setId(1L);

        Page<Product> page = new PageImpl<>(List.of(product));
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findByModuleFatherId(1L, pageable)).thenReturn(page);

        Page<ProductDTO> result = productService.findByIdModuleFather(1L, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findByIdModuleFather_deveLancarNotFoundException_quandoModuleFatherNaoExistir() {
        Page<Product> emptyPage = new PageImpl<>(Collections.emptyList());
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findByModuleFatherId(999L, pageable)).thenReturn(emptyPage);

        assertThrows(NotFoundException.class, () -> productService.findByIdModuleFather(999L, pageable));
    }

    // ==================== TESTES DO findByIdModuleChild ====================

    @Test
    void findByIdModuleChild_deveRetornarPagina_quandoModuleChildExistir() {
        Product product = new Product();
        product.setId(1L);

        Page<Product> page = new PageImpl<>(List.of(product));
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findByModuleChildId(1L, pageable)).thenReturn(page);

        Page<ProductDTO> result = productService.findByIdModuleChild(1L, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findByIdModuleChild_deveLancarNotFoundException_quandoModuleChildNaoExistir() {
        Page<Product> emptyPage = new PageImpl<>(Collections.emptyList());
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findByModuleChildId(999L, pageable)).thenReturn(emptyPage);

        assertThrows(NotFoundException.class, () -> productService.findByIdModuleChild(999L, pageable));
    }

    // ==================== TESTES DO findByIsActive ====================

    @Test
    void findByIsActive_deveRetornarPagina_quandoExistirProdutosAtivos() {
        Product product = new Product();
        product.setId(1L);
        product.setIsActive(true);

        Page<Product> page = new PageImpl<>(List.of(product));
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findByIsActive(true, pageable)).thenReturn(page);

        Page<ProductDTO> result = productService.findByIsActive(true, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findByIsActive_deveLancarNotFoundException_quandoNaoExistirProdutos() {
        Page<Product> emptyPage = new PageImpl<>(Collections.emptyList());
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findByIsActive(false, pageable)).thenReturn(emptyPage);

        assertThrows(NotFoundException.class, () -> productService.findByIsActive(false, pageable));
    }

    // ==================== TESTES DO save ====================

    @Test
    void save_deveSalvarERetornarProduct() {
        // PREPARAR: Cria o request simulando os dados enviados pelo usuário
        SaveProductRequest request = new SaveProductRequest();
        request.setName("Mesa de Escritório");
        request.setTypeProduct("mesa");
        request.setLocalToPut(new LocalToPut());
        request.setModuleFather(new ModuleFather());
        request.setModuleChild(new ModuleChild());
        request.setIsActive(true);

        // Usa any(Product.class) porque o service cria internamente um NOVO Product,
        // que é uma instância diferente da que criamos no teste.
        // O thenAnswer captura o Product criado pelo service e adiciona um ID,
        // simulando o que o banco faria ao gerar o ID automaticamente.
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0); // Pega o Product que o service criou
            savedProduct.setId(1L); // Simula o ID gerado pelo banco
            return savedProduct; // Retorna o Product com ID
        });

        // EXECUTAR: Chama o método save do service
        Product result = productService.save(request);

        // VERIFICAR: Confere se os dados foram transferidos corretamente
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Mesa de Escritório", result.getName());
        assertEquals("mesa", result.getTypeProduct());
        assertEquals(true, result.getIsActive());
    }

    // ==================== TESTES DO update ====================

    @Test
    void update_deveAtualizarERetornarProduct() {
        // PREPARAR: Cria um produto que "já existe no banco" com valores antigos
        Product produtoExistente = new Product();
        produtoExistente.setId(1L);
        produtoExistente.setName("Mesa Antiga");
        produtoExistente.setTypeProduct("mesa");
        produtoExistente.setIsActive(true);

        // O método update primeiro busca o produto pelo ID (findById)
        when(productRepository.findById(1L)).thenReturn(Optional.of(produtoExistente));

        // Depois salva o produto atualizado no banco
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Cria o request de atualização com os novos valores
        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Mesa Nova"); // Altera o nome

        // EXECUTAR: Chama o método update
        Product result = productService.update(1L, request);

        // VERIFICAR: O nome deve ter sido atualizado, mas o restante permanece igual
        assertEquals(1L, result.getId());
        assertEquals("Mesa Nova", result.getName()); // Valor novo
        assertEquals("mesa", result.getTypeProduct()); // Valor que não foi alterado permanece
        assertEquals(true, result.getIsActive()); // Valor que não foi alterado permanece
    }

    @Test
    void update_deveLancarNotFoundException_quandoIdNaoExistir() {
        // PREPARAR: Ensina o mock a retornar vazio para ID inexistente
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // EXECUTAR E VERIFICAR: Garante que lança exceção ao tentar atualizar
        assertThrows(NotFoundException.class, () -> productService.update(999L, new UpdateProductRequest()));
    }

    // ==================== TESTES DO delete ====================

    @Test
    void delete_deveDeletarComSucesso_quandoIdExistir() {
        // PREPARAR: Ensina o mock a dizer que o ID 1 existe no banco
        when(productRepository.existsById(1L)).thenReturn(true);

        // EXECUTAR: Chama o método delete
        productService.delete(1L);

        // VERIFICAR: Garante que o método deleteById do repositório foi chamado com o ID correto
        verify(productRepository).deleteById(1L);
    }

    @Test
    void delete_deveLancarNotFoundException_quandoIdNaoExistir() {
        // PREPARAR: Ensina o mock a dizer que o ID 999 NÃO existe
        when(productRepository.existsById(999L)).thenReturn(false);

        // EXECUTAR E VERIFICAR: Garante que lança exceção
        assertThrows(NotFoundException.class, () -> productService.delete(999L));
    }
}
