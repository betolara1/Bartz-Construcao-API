package com.betolara1.SiteConstrutor.service;

// Importações dos models e DTOs do projeto
import com.betolara1.model.ModuleChild;
import com.betolara1.model.ModuleFather;
import com.betolara1.dto.request.SaveModuleChildRequest;
import com.betolara1.dto.request.UpdateModuleChildRequest;
import com.betolara1.dto.response.ModuleChildDTO;
import com.betolara1.exception.NotFoundException;
import com.betolara1.repository.ModuleChildRepository;
import com.betolara1.service.ModuleChildService;

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
public class ModuleChildServiceTest {

    // Cria um "dublê" (mock) do repositório — NÃO conecta no banco de dados real
    @Mock
    private ModuleChildRepository moduleChildRepository;

    // Injeta o mock do repositório dentro do service real que queremos testar
    @InjectMocks
    private ModuleChildService moduleChildService;

    // ==================== TESTES DO findAll ====================

    @Test
    void findAll_deveRetornarPaginaDeModuleChildDTO() {
        // PREPARAR: Cria um módulo filho fictício
        ModuleChild moduleChild = new ModuleChild();
        moduleChild.setId(1L);
        moduleChild.setName("Módulo Pia");
        moduleChild.setModuleFather(new ModuleFather()); // Define o módulo pai associado

        // Cria uma página com o módulo filho
        Page<ModuleChild> page = new PageImpl<>(List.of(moduleChild));
        Pageable pageable = PageRequest.of(0, 10);

        when(moduleChildRepository.findAll(pageable)).thenReturn(page);

        // EXECUTAR
        Page<ModuleChildDTO> result = moduleChildService.findAll(pageable);

        // VERIFICAR
        assertEquals(1, result.getTotalElements());
        assertEquals("Módulo Pia", result.getContent().get(0).name());
    }

    // ==================== TESTES DO getModuleChildById ====================

    @Test
    void getModuleChildById_deveRetornarDTO_quandoIdExistir() {
        // PREPARAR
        ModuleChild moduleChild = new ModuleChild();
        moduleChild.setId(1L);
        moduleChild.setName("Módulo Pia");

        when(moduleChildRepository.findById(1L)).thenReturn(Optional.of(moduleChild));

        // EXECUTAR
        ModuleChildDTO result = moduleChildService.getModuleChildById(1L);

        // VERIFICAR
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Módulo Pia", result.name());
    }

    @Test
    void getModuleChildById_deveLancarNotFoundException_quandoIdNaoExistir() {
        when(moduleChildRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> moduleChildService.getModuleChildById(999L));
    }

    // ==================== TESTES DO getModuleChildByName ====================

    @Test
    void getModuleChildByName_deveRetornarDTO_quandoNomeExistir() {
        // PREPARAR
        ModuleChild moduleChild = new ModuleChild();
        moduleChild.setId(1L);
        moduleChild.setName("Módulo Pia");

        when(moduleChildRepository.findByName("Módulo Pia")).thenReturn(Optional.of(moduleChild));

        // EXECUTAR
        ModuleChildDTO result = moduleChildService.getModuleChildByName("Módulo Pia");

        // VERIFICAR
        assertNotNull(result);
        assertEquals("Módulo Pia", result.name());
    }

    @Test
    void getModuleChildByName_deveLancarNotFoundException_quandoNomeNaoExistir() {
        when(moduleChildRepository.findByName("NaoExiste")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> moduleChildService.getModuleChildByName("NaoExiste"));
    }

    // ==================== TESTES DO getModuleChildByIdModuleFather ====================

    @Test
    void getModuleChildByIdModuleFather_deveRetornarPagina_quandoExistirFilhos() {
        // PREPARAR: Cria módulo filho associado a um módulo pai
        ModuleChild moduleChild = new ModuleChild();
        moduleChild.setId(1L);
        moduleChild.setName("Módulo Pia");

        Page<ModuleChild> page = new PageImpl<>(List.of(moduleChild));
        Pageable pageable = PageRequest.of(0, 10);

        when(moduleChildRepository.findByModuleFatherId(1L, pageable)).thenReturn(page);

        // EXECUTAR
        Page<ModuleChildDTO> result = moduleChildService.getModuleChildByIdModuleFather(1L, pageable);

        // VERIFICAR
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getModuleChildByIdModuleFather_deveLancarNotFoundException_quandoNaoExistirFilhos() {
        Page<ModuleChild> emptyPage = new PageImpl<>(Collections.emptyList());
        Pageable pageable = PageRequest.of(0, 10);

        when(moduleChildRepository.findByModuleFatherId(999L, pageable)).thenReturn(emptyPage);

        assertThrows(NotFoundException.class,
                () -> moduleChildService.getModuleChildByIdModuleFather(999L, pageable));
    }

    // ==================== TESTES DO save ====================

    @Test
    void save_deveSalvarERetornarModuleChildDTO() {
        // PREPARAR: Cria o request com os dados enviados pelo usuário
        SaveModuleChildRequest request = new SaveModuleChildRequest();
        request.setName("Módulo Pia");
        request.setModuleFather(new ModuleFather()); // Define qual módulo pai está associado

        // Usa any() porque o service cria internamente um NOVO ModuleChild
        when(moduleChildRepository.save(any(ModuleChild.class))).thenAnswer(invocation -> {
            ModuleChild saved = invocation.getArgument(0); // Pega o objeto criado pelo service
            saved.setId(1L); // Simula o ID gerado pelo banco
            return saved;
        });

        // EXECUTAR: O service retorna a entidade ModuleChild agora
        ModuleChild result = moduleChildService.save(request);

        // VERIFICAR
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Módulo Pia", result.getName());
    }

    // ==================== TESTES DO update ====================

    @Test
    void update_deveAtualizarERetornarModuleChildDTO() {
        // PREPARAR: Cria o módulo filho que "já existe no banco"
        ModuleChild existente = new ModuleChild();
        existente.setId(1L);
        existente.setName("Nome Antigo");

        // O update primeiro busca o registro pelo ID
        when(moduleChildRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(moduleChildRepository.save(any(ModuleChild.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Cria o request com o novo nome
        UpdateModuleChildRequest request = new UpdateModuleChildRequest();
        request.setName("Nome Novo");

        // EXECUTAR
        ModuleChild result = moduleChildService.update(1L, request);

        // VERIFICAR: O nome deve ter sido atualizado
        assertEquals(1L, result.getId());
        assertEquals("Nome Novo", result.getName());
    }

    @Test
    void update_deveLancarNotFoundException_quandoIdNaoExistir() {
        when(moduleChildRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> moduleChildService.update(999L, new UpdateModuleChildRequest()));
    }

    // ==================== TESTES DO delete ====================

    @Test
    void delete_deveDeletarComSucesso_quandoIdExistir() {
        // PREPARAR: Ensina o mock a dizer que o ID 1 existe
        when(moduleChildRepository.existsById(1L)).thenReturn(true);

        // EXECUTAR
        moduleChildService.delete(1L);

        // VERIFICAR: Garante que deleteById foi realmente chamado
        verify(moduleChildRepository).deleteById(1L);
    }

    @Test
    void delete_deveLancarNotFoundException_quandoIdNaoExistir() {
        when(moduleChildRepository.existsById(999L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> moduleChildService.delete(999L));
    }
}
