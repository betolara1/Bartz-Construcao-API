package com.betolara1.SiteConstrutor.controller;

// Importações dos models, DTOs e services do projeto
import com.betolara1.controller.ModuleChildController;
import com.betolara1.dto.response.ModuleChildDTO;
import com.betolara1.exception.NotFoundException;
import com.betolara1.model.ModuleChild;
import com.betolara1.service.ModuleChildService;

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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// @WebMvcTest carrega APENAS o ModuleChildController
@WebMvcTest(ModuleChildController.class)
public class ModuleChildControllerTest {

    // MockMvc simula requisições HTTP
    @Autowired
    private MockMvc mockMvc;

    // "Dublê" do service
    @MockitoBean
    private ModuleChildService moduleChildService;

    // ==================== TESTES DO GET /moduleChild/id ====================

    @Test
    void getModuleChildById_deveRetornar200_quandoIdExistir() throws Exception {
        // PREPARAR: Cria um módulo filho fictício
        ModuleChild moduleChild = new ModuleChild();
        moduleChild.setId(1L);
        moduleChild.setName("Módulo Pia");

        ModuleChildDTO dto = new ModuleChildDTO(moduleChild);

        when(moduleChildService.getModuleChildById(1L)).thenReturn(dto);

        // EXECUTAR E VERIFICAR
        mockMvc.perform(get("/moduleChild/id")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Módulo Pia"));
    }

    @Test
    void getModuleChildById_deveRetornar404_quandoIdNaoExistir() throws Exception {
        when(moduleChildService.getModuleChildById(999L))
                .thenThrow(new NotFoundException("Módulo filho não encontrado"));

        mockMvc.perform(get("/moduleChild/id")
                .param("id", "999"))
                .andExpect(status().isNotFound());
    }

    // ==================== TESTES DO GET /moduleChild/name ====================

    @Test
    void getModuleChildByName_deveRetornar200_quandoNomeExistir() throws Exception {
        ModuleChild moduleChild = new ModuleChild();
        moduleChild.setId(1L);
        moduleChild.setName("Módulo Pia");

        ModuleChildDTO dto = new ModuleChildDTO(moduleChild);

        when(moduleChildService.getModuleChildByName("Módulo Pia")).thenReturn(dto);

        mockMvc.perform(get("/moduleChild/name")
                .param("name", "Módulo Pia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Módulo Pia"));
    }

    // ==================== TESTES DO GET /moduleChild/all ====================

    @Test
    void findAll_deveRetornar200_comListaDeModulos() throws Exception {
        ModuleChild moduleChild = new ModuleChild();
        moduleChild.setId(1L);
        moduleChild.setName("Módulo Pia");

        Page<ModuleChildDTO> page = new PageImpl<>(List.of(new ModuleChildDTO(moduleChild)));

        when(moduleChildService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/moduleChild/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Módulo Pia"));
    }

    // ==================== TESTES DO POST /moduleChild ====================

    @Test
    void createModuleChild_deveRetornar201_quandoDadosForemValidos() throws Exception {
        // PREPARAR: Cria o DTO que o service retorna
        ModuleChild moduleChild = new ModuleChild();
        moduleChild.setId(1L);
        moduleChild.setName("Módulo Novo");

        ModuleChildDTO dto = new ModuleChildDTO(moduleChild);

        // O save do ModuleChildService retorna um DTO (diferente dos outros)
        when(moduleChildService.save(any())).thenReturn(dto);

        // JSON do corpo da requisição
        String json = """
                {
                    "name": "Módulo Novo",
                    "moduleFather": {"id": 1}
                }
                """;

        // EXECUTAR E VERIFICAR
        mockMvc.perform(post("/moduleChild")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated()) // Espera HTTP 201
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Módulo Novo"));
    }

    // ==================== TESTES DO PUT /moduleChild/{id} ====================

    @Test
    void updateModuleChild_deveRetornar200_quandoAtualizarComSucesso() throws Exception {
        ModuleChild moduleChild = new ModuleChild();
        moduleChild.setId(1L);
        moduleChild.setName("Módulo Atualizado");

        ModuleChildDTO dto = new ModuleChildDTO(moduleChild);

        // O update do ModuleChildService também retorna DTO
        when(moduleChildService.update(eq(1L), any())).thenReturn(dto);

        String json = """
                {
                    "name": "Módulo Atualizado"
                }
                """;

        mockMvc.perform(put("/moduleChild/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Módulo Atualizado"));
    }

    @Test
    void updateModuleChild_deveRetornar404_quandoIdNaoExistir() throws Exception {
        when(moduleChildService.update(eq(999L), any()))
                .thenThrow(new NotFoundException("Módulo filho não encontrado"));

        String json = """
                {
                    "name": "Qualquer"
                }
                """;

        mockMvc.perform(put("/moduleChild/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    // ==================== TESTES DO DELETE /moduleChild/{id} ====================

    @Test
    void deleteModuleChild_deveRetornar200_quandoIdExistir() throws Exception {
        doNothing().when(moduleChildService).delete(1L);

        mockMvc.perform(delete("/moduleChild/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteModuleChild_deveRetornar404_quandoIdNaoExistir() throws Exception {
        doThrow(new NotFoundException("Módulo filho não encontrado"))
                .when(moduleChildService).delete(999L);

        mockMvc.perform(delete("/moduleChild/999"))
                .andExpect(status().isNotFound());
    }
}
