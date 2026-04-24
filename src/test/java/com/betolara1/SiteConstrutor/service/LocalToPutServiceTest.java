package com.betolara1.SiteConstrutor.service;

// Importações dos models e DTOs do projeto
import com.betolara1.model.LocalToPut;
import com.betolara1.dto.request.SaveLocalToPutRequest;
import com.betolara1.dto.request.UpdateLocalToPutRequest;
import com.betolara1.dto.response.LocalToPutDTO;
import com.betolara1.exception.NotFoundException;
import com.betolara1.repository.LocalToPutRepository;
import com.betolara1.service.LocalToPutService;

// Importações do JUnit 5 para asserções e anotações de teste
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Importações do Mockito para criar mocks e simular comportamentos
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
public class LocalToPutServiceTest {

    // Cria um "dublê" (mock) do repositório — NÃO conecta no banco de dados real
    @Mock
    private LocalToPutRepository localToPutRepository;

    // Injeta o mock do repositório dentro do service real que queremos testar
    @InjectMocks
    private LocalToPutService localToPutService;

    // ==================== TESTES DO findAll ====================

    @Test
    void findAll_deveRetornarPaginaDeLocalToPutDTO() {
        // PREPARAR: Cria um local fictício
        LocalToPut local = new LocalToPut();
        local.setId(1L);
        local.setName("Parede");

        // Cria uma página contendo este local
        Page<LocalToPut> page = new PageImpl<>(List.of(local));
        Pageable pageable = PageRequest.of(0, 10);

        when(localToPutRepository.findAll(pageable)).thenReturn(page);

        // EXECUTAR
        Page<LocalToPutDTO> result = localToPutService.findAll(pageable);

        // VERIFICAR
        assertEquals(1, result.getTotalElements());
        assertEquals("Parede", result.getContent().get(0).name());
    }

    // ==================== TESTES DO findById ====================

    @Test
    void findById_deveRetornarLocalToPutDTO_quandoIdExistir() {
        // PREPARAR
        LocalToPut local = new LocalToPut();
        local.setId(1L);
        local.setName("Parede");

        when(localToPutRepository.findById(1L)).thenReturn(Optional.of(local));

        // EXECUTAR
        LocalToPutDTO result = localToPutService.findById(1L);

        // VERIFICAR
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Parede", result.name());
    }

    @Test
    void findById_deveLancarNotFoundException_quandoIdNaoExistir() {
        when(localToPutRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> localToPutService.findById(999L));
    }

    // ==================== TESTES DO findByName ====================

    @Test
    void findByName_deveRetornarLocalToPutDTO_quandoNomeExistir() {
        // PREPARAR
        LocalToPut local = new LocalToPut();
        local.setId(1L);
        local.setName("Chão");

        when(localToPutRepository.findByName("Chão")).thenReturn(Optional.of(local));

        // EXECUTAR
        LocalToPutDTO result = localToPutService.findByName("Chão");

        // VERIFICAR
        assertNotNull(result);
        assertEquals("Chão", result.name());
    }

    @Test
    void findByName_deveLancarNotFoundException_quandoNomeNaoExistir() {
        when(localToPutRepository.findByName("NaoExiste")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> localToPutService.findByName("NaoExiste"));
    }

    // ==================== TESTES DO save ====================

    @Test
    void save_deveSalvarERetornarLocalToPutDTO() {
        // PREPARAR: Cria o request com os dados enviados pelo usuário
        SaveLocalToPutRequest request = new SaveLocalToPutRequest();
        request.setName("Parede");

        // Usa any() porque o service cria internamente um NOVO LocalToPut
        when(localToPutRepository.save(any(LocalToPut.class))).thenAnswer(invocation -> {
            LocalToPut saved = invocation.getArgument(0); // Pega o objeto criado pelo service
            saved.setId(1L); // Simula o ID gerado pelo banco
            return saved;
        });

        // EXECUTAR: O service retorna um DTO
        LocalToPutDTO result = localToPutService.save(request);

        // VERIFICAR
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Parede", result.name());
    }

    // ==================== TESTES DO update ====================

    @Test
    void update_deveAtualizarERetornarLocalToPutDTO() {
        // PREPARAR: Cria o local que "já existe no banco"
        LocalToPut existente = new LocalToPut();
        existente.setId(1L);
        existente.setName("Parede");

        // O update primeiro busca o registro pelo ID
        when(localToPutRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(localToPutRepository.save(any(LocalToPut.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Cria o request com o novo nome
        UpdateLocalToPutRequest request = new UpdateLocalToPutRequest();
        request.setName("Chão");

        // EXECUTAR
        LocalToPutDTO result = localToPutService.update(1L, request);

        // VERIFICAR: O nome deve ter sido atualizado
        assertEquals(1L, result.id());
        assertEquals("Chão", result.name());
    }

    @Test
    void update_deveLancarNotFoundException_quandoIdNaoExistir() {
        when(localToPutRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> localToPutService.update(999L, new UpdateLocalToPutRequest()));
    }

    // ==================== TESTES DO delete ====================

    @Test
    void delete_deveDeletarComSucesso_quandoIdExistir() {
        // PREPARAR: Ensina o mock a dizer que o ID 1 existe
        when(localToPutRepository.existsById(1L)).thenReturn(true);

        // EXECUTAR
        localToPutService.delete(1L);

        // VERIFICAR: Garante que deleteById foi realmente chamado
        verify(localToPutRepository).deleteById(1L);
    }

    @Test
    void delete_deveLancarNotFoundException_quandoIdNaoExistir() {
        when(localToPutRepository.existsById(999L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> localToPutService.delete(999L));
    }
}
