package com.betolara1.SiteConstrutor.service;

// Importações dos models e DTOs do projeto
import com.betolara1.model.ModuleFather;
import com.betolara1.dto.request.SaveModuleFatherRequest;
import com.betolara1.dto.request.UpdateModuleFatherRequest;
import com.betolara1.dto.response.ModuleFatherDTO;
import com.betolara1.exception.NotFoundException;
import com.betolara1.repository.ModuleFatherRepository;
import com.betolara1.service.ModuleFatherService;

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
public class ModuleFatherServiceTest {

    // Cria um "dublê" (mock) do repositório — NÃO conecta no banco de dados real
    @Mock
    private ModuleFatherRepository moduleFatherRepository;

    // Injeta o mock do repositório dentro do service real que queremos testar
    @InjectMocks
    private ModuleFatherService moduleFatherService;

    // ==================== TESTES DO findAll ====================

    @Test
    void findAll_deveRetornarPaginaDeModuleFatherDTO() {
        // PREPARAR: Cria um módulo pai fictício
        ModuleFather moduleFather = new ModuleFather();
        moduleFather.setId(1L);
        moduleFather.setName("Módulo Cozinha");

        // Cria uma página com o módulo pai
        Page<ModuleFather> page = new PageImpl<>(List.of(moduleFather));
        Pageable pageable = PageRequest.of(0, 10);

        // Ensina o mock a retornar a página
        when(moduleFatherRepository.findAll(pageable)).thenReturn(page);

        // EXECUTAR
        Page<ModuleFatherDTO> result = moduleFatherService.findAll(pageable);

        // VERIFICAR
        assertEquals(1, result.getTotalElements());
        assertEquals("Módulo Cozinha", result.getContent().get(0).name());
    }

    // ==================== TESTES DO getModuleFatherById ====================

    @Test
    void getModuleFatherById_deveRetornarDTO_quandoIdExistir() {
        // PREPARAR: Cria um módulo pai fictício
        ModuleFather moduleFather = new ModuleFather();
        moduleFather.setId(1L);
        moduleFather.setName("Módulo Cozinha");

        // Ensina o mock: "quando pedirem o ID 1, retorne este módulo"
        when(moduleFatherRepository.findById(1L)).thenReturn(Optional.of(moduleFather));

        // EXECUTAR
        ModuleFatherDTO result = moduleFatherService.getModuleFatherById(1L);

        // VERIFICAR
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Módulo Cozinha", result.name());
    }

    @Test
    void getModuleFatherById_deveLancarNotFoundException_quandoIdNaoExistir() {
        // PREPARAR: Retorna vazio para ID inexistente
        when(moduleFatherRepository.findById(999L)).thenReturn(Optional.empty());

        // EXECUTAR E VERIFICAR
        assertThrows(NotFoundException.class, () -> moduleFatherService.getModuleFatherById(999L));
    }

    // ==================== TESTES DO getModuleFatherByName ====================

    @Test
    void getModuleFatherByName_deveRetornarDTO_quandoNomeExistir() {
        // PREPARAR
        ModuleFather moduleFather = new ModuleFather();
        moduleFather.setId(1L);
        moduleFather.setName("Módulo Cozinha");

        // Ensina o mock: "quando pedirem pelo nome 'Módulo Cozinha', retorne este módulo"
        when(moduleFatherRepository.findByName("Módulo Cozinha")).thenReturn(Optional.of(moduleFather));

        // EXECUTAR
        ModuleFatherDTO result = moduleFatherService.getModuleFatherByName("Módulo Cozinha");

        // VERIFICAR
        assertNotNull(result);
        assertEquals("Módulo Cozinha", result.name());
    }

    @Test
    void getModuleFatherByName_deveLancarNotFoundException_quandoNomeNaoExistir() {
        // PREPARAR: Retorna vazio para nome inexistente
        when(moduleFatherRepository.findByName("NaoExiste")).thenReturn(Optional.empty());

        // EXECUTAR E VERIFICAR
        assertThrows(NotFoundException.class, () -> moduleFatherService.getModuleFatherByName("NaoExiste"));
    }

    // ==================== TESTES DO save ====================

    @Test
    void save_deveSalvarERetornarModuleFather() {
        // PREPARAR: Cria o request com os dados enviados pelo usuário
        SaveModuleFatherRequest request = new SaveModuleFatherRequest();
        request.setName("Módulo Cozinha");

        // Usa any() porque o service cria internamente um NOVO ModuleFather
        when(moduleFatherRepository.save(any(ModuleFather.class))).thenAnswer(invocation -> {
            ModuleFather saved = invocation.getArgument(0); // Pega o objeto criado pelo service
            saved.setId(1L); // Simula o ID gerado pelo banco
            return saved;
        });

        // EXECUTAR
        ModuleFather result = moduleFatherService.save(request);

        // VERIFICAR
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Módulo Cozinha", result.getName());
    }

    // ==================== TESTES DO update ====================

    @Test
    void update_deveAtualizarERetornarModuleFather() {
        // PREPARAR: Cria o módulo pai que "já existe no banco"
        ModuleFather existente = new ModuleFather();
        existente.setId(1L);
        existente.setName("Nome Antigo");

        // O update primeiro busca o registro pelo ID
        when(moduleFatherRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(moduleFatherRepository.save(any(ModuleFather.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Cria o request com o novo nome
        UpdateModuleFatherRequest request = new UpdateModuleFatherRequest();
        request.setName("Nome Novo");

        // EXECUTAR
        ModuleFather result = moduleFatherService.update(1L, request);

        // VERIFICAR: O nome deve ter sido atualizado
        assertEquals(1L, result.getId());
        assertEquals("Nome Novo", result.getName());
    }

    @Test
    void update_deveLancarNotFoundException_quandoIdNaoExistir() {
        // PREPARAR
        when(moduleFatherRepository.findById(999L)).thenReturn(Optional.empty());

        // EXECUTAR E VERIFICAR
        assertThrows(NotFoundException.class, () -> moduleFatherService.update(999L, new UpdateModuleFatherRequest()));
    }

    // ==================== TESTES DO delete ====================

    @Test
    void delete_deveDeletarComSucesso_quandoIdExistir() {
        // PREPARAR: Ensina o mock a dizer que o ID 1 existe
        when(moduleFatherRepository.existsById(1L)).thenReturn(true);

        // EXECUTAR
        moduleFatherService.delete(1L);

        // VERIFICAR: Garante que deleteById foi realmente chamado
        verify(moduleFatherRepository).deleteById(1L);
    }

    @Test
    void delete_deveLancarNotFoundException_quandoIdNaoExistir() {
        // PREPARAR: Ensina o mock a dizer que o ID 999 NÃO existe
        when(moduleFatherRepository.existsById(999L)).thenReturn(false);

        // EXECUTAR E VERIFICAR
        assertThrows(NotFoundException.class, () -> moduleFatherService.delete(999L));
    }
}
