package com.betolara1.SiteConstrutor.service;

// Importações dos models e DTOs do projeto
import com.betolara1.model.Product;
import com.betolara1.model.Size;
import com.betolara1.dto.request.SaveSizeRequest;
import com.betolara1.dto.request.UpdateSizeRequest;
import com.betolara1.dto.response.SizeDTO;
import com.betolara1.exception.NotFoundException;
import com.betolara1.repository.SizeRepository;
import com.betolara1.service.SizeService;

// Importações do JUnit 5 para asserções e anotações de teste
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Importações do Mockito para criar mocks e simular comportamentos
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Habilita o uso de Mocks do Mockito nesta classe de teste
@ExtendWith(MockitoExtension.class)
public class SizeServiceTest {

    // Cria um "dublê" (mock) do repositório — NÃO conecta no banco de dados real
    @Mock
    private SizeRepository sizeRepository;

    // Injeta o mock do repositório dentro do service real que queremos testar
    @InjectMocks
    private SizeService sizeService;

    // ==================== TESTES DO findById ====================

    @Test
    void findById_deveRetornarSizeDTO_quandoIdExistir() {
        // PREPARAR: Cria um objeto Size fictício simulando um registro do banco
        Size size = new Size();
        size.setId(1L);
        size.setHeightMax(1200.0);
        size.setHeightMin(100.0);
        size.setWidthMax(300.0);
        size.setWidthMin(50.0);
        size.setDepthMax(200.0);
        size.setDepthMin(30.0);

        // Ensina o mock: "quando pedirem o ID 1, retorne este Size"
        when(sizeRepository.findById(1L)).thenReturn(Optional.of(size));

        // EXECUTAR: Chama o método real do service
        SizeDTO result = sizeService.findById(1L);

        // VERIFICAR: Confere se o resultado corresponde ao que preparamos
        assertNotNull(result); // Garante que o resultado não é nulo
        assertEquals(1L, result.id()); // Verifica se o ID está correto
        assertEquals(1200.0, result.heightMax()); // Verifica a altura máxima
        assertEquals(100.0, result.heightMin()); // Verifica a altura mínima
        assertEquals(300.0, result.widthMax()); // Verifica a largura máxima
        assertEquals(50.0, result.widthMin()); // Verifica a largura mínima
        assertEquals(200.0, result.depthMax()); // Verifica a profundidade máxima
        assertEquals(30.0, result.depthMin()); // Verifica a profundidade mínima
    }

    @Test
    void findById_deveLancarNotFoundException_quandoIdNaoExistir() {
        // PREPARAR: Ensina o mock a retornar "vazio" quando pedirem o ID 999
        when(sizeRepository.findById(999L)).thenReturn(Optional.empty());

        // EXECUTAR E VERIFICAR: Garante que uma exceção NotFoundException é lançada
        assertThrows(NotFoundException.class, () -> sizeService.findById(999L));
    }

    // ==================== TESTES DO findByIdProduct ====================

    @Test
    void findByIdProduct_deveRetornarSizeDTO_quandoProductIdExistir() {
        // PREPARAR: Cria um Size associado a um Product
        Size size = new Size();
        size.setId(1L);
        size.setProduct(new Product()); // Define um produto associado

        // Ensina o mock a responder à busca por ID de produto
        when(sizeRepository.findByProductId(1L)).thenReturn(Optional.of(size));

        // EXECUTAR: Chama o método que busca Size pelo ID do produto
        SizeDTO result = sizeService.findByIdProduct(1L);

        // VERIFICAR: Confere se retornou corretamente
        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    void findByIdProduct_deveLancarNotFoundException_quandoProductIdNaoExistir() {
        // PREPARAR: Ensina o mock a retornar vazio para ID de produto inexistente
        when(sizeRepository.findByProductId(999L)).thenReturn(Optional.empty());

        // EXECUTAR E VERIFICAR: Garante que lança NotFoundException
        assertThrows(NotFoundException.class, () -> sizeService.findByIdProduct(999L));
    }

    // ==================== TESTES DO save ====================

    @Test
    void save_deveSalvarERetornarSize() {
        // PREPARAR: Cria o request com os dados que seriam enviados pelo usuário
        SaveSizeRequest request = new SaveSizeRequest();
        request.setProduct(new Product());
        request.setHeightMax(100.0);
        request.setHeightMin(50.0);
        request.setWidthMax(200.0);
        request.setWidthMin(80.0);
        request.setDepthMax(150.0);
        request.setDepthMin(40.0);

        // Usa any(Size.class) porque o service cria um NOVO objeto Size internamente,
        // que é diferente do objeto que criamos aqui no teste.
        // O thenAnswer captura o objeto que o service criou e adiciona um ID nele,
        // simulando o que o banco de dados faria ao gerar o ID automaticamente.
        when(sizeRepository.save(any(Size.class))).thenAnswer(invocation -> {
            Size savedSize = invocation.getArgument(0); // Pega o Size que o service passou
            savedSize.setId(1L); // Simula o ID gerado pelo banco
            return savedSize; // Retorna o Size com ID
        });

        // EXECUTAR: Chama o método save do service
        Size result = sizeService.save(request);

        // VERIFICAR: Confere se os dados foram transferidos corretamente do request para o Size
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100.0, result.getHeightMax());
        assertEquals(50.0, result.getHeightMin());
        assertEquals(200.0, result.getWidthMax());
        assertEquals(80.0, result.getWidthMin());
        assertEquals(150.0, result.getDepthMax());
        assertEquals(40.0, result.getDepthMin());
    }

    // ==================== TESTES DO update ====================

    @Test
    void update_deveAtualizarERetornarSize() {
        // PREPARAR: Cria um Size que "já existe no banco" com valores antigos
        Size sizeExistente = new Size();
        sizeExistente.setId(1L);
        sizeExistente.setHeightMax(500.0);
        sizeExistente.setHeightMin(100.0);

        // O update primeiro busca o Size pelo ID antes de atualizar
        when(sizeRepository.findById(1L)).thenReturn(Optional.of(sizeExistente));

        // Aceita qualquer Size no save e retorna ele de volta
        when(sizeRepository.save(any(Size.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Cria o request de atualização com os novos valores
        UpdateSizeRequest request = new UpdateSizeRequest();
        request.setHeightMax(1000.0); // Altera a altura máxima de 500 para 1000

        // EXECUTAR: Chama o método update
        Size result = sizeService.update(1L, request);

        // VERIFICAR: Confere se o valor foi atualizado
        assertEquals(1L, result.getId());
        assertEquals(1000.0, result.getHeightMax()); // Valor novo
        assertEquals(100.0, result.getHeightMin()); // Valor que não foi alterado permanece igual
    }

    @Test
    void update_deveLancarNotFoundException_quandoIdNaoExistir() {
        // PREPARAR: Ensina o mock a retornar vazio para um ID que não existe
        when(sizeRepository.findById(999L)).thenReturn(Optional.empty());

        // EXECUTAR E VERIFICAR: Garante que lança exceção ao tentar atualizar ID inexistente
        assertThrows(NotFoundException.class, () -> sizeService.update(999L, new UpdateSizeRequest()));
    }

    // ==================== TESTES DO delete ====================

    @Test
    void delete_deveDeletarComSucesso_quandoIdExistir() {
        // PREPARAR: Ensina o mock a dizer que o ID 1 existe
        when(sizeRepository.existsById(1L)).thenReturn(true);

        // EXECUTAR: Chama o método delete do service
        sizeService.delete(1L);

        // VERIFICAR: Garante que o método deleteById do repositório foi realmente chamado
        verify(sizeRepository).deleteById(1L);
    }

    @Test
    void delete_deveLancarNotFoundException_quandoIdNaoExistir() {
        // PREPARAR: Ensina o mock a dizer que o ID 999 NÃO existe
        when(sizeRepository.existsById(999L)).thenReturn(false);

        // EXECUTAR E VERIFICAR: Garante que lança exceção
        assertThrows(NotFoundException.class, () -> sizeService.delete(999L));
    }
}
