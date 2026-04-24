package com.betolara1.SiteConstrutor.controller;

// Importações dos models, DTOs e services do projeto
import com.betolara1.controller.ModuleFatherController;
import com.betolara1.dto.response.ModuleFatherDTO;
import com.betolara1.exception.NotFoundException;
import com.betolara1.model.ModuleFather;
import com.betolara1.service.ModuleFatherService;

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

// @WebMvcTest carrega APENAS o ModuleFatherController (não sobe toda a aplicação)
@WebMvcTest(ModuleFatherController.class)
public class ModuleFatherControllerTest {

    // MockMvc simula requisições HTTP sem precisar de um servidor real
    @Autowired
    private MockMvc mockMvc;

    // Cria um "dublê" do service para o controller usar
    @MockitoBean
    private ModuleFatherService moduleFatherService;

    // ==================== TESTES DO GET /moduleFather/id ====================

    @Test
    void getModuleFatherById_deveRetornar200_quandoIdExistir() throws Exception {
        // PREPARAR: Cria um módulo pai fictício
        ModuleFather moduleFather = new ModuleFather();
        moduleFather.setId(1L);
        moduleFather.setName("Módulo Cozinha");

        ModuleFatherDTO dto = new ModuleFatherDTO(moduleFather);

        when(moduleFatherService.getModuleFatherById(1L)).thenReturn(dto);

        // EXECUTAR E VERIFICAR: Envia GET e confere a resposta
        mockMvc.perform(get("/moduleFather/id")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Módulo Cozinha"));
    }

    @Test
    void getModuleFatherById_deveRetornar404_quandoIdNaoExistir() throws Exception {
        when(moduleFatherService.getModuleFatherById(999L))
                .thenThrow(new NotFoundException("Módulo pai não encontrado"));

        mockMvc.perform(get("/moduleFather/id")
                .param("id", "999"))
                .andExpect(status().isNotFound());
    }

    // ==================== TESTES DO GET /moduleFather/name ====================

    @Test
    void getModuleFatherByName_deveRetornar200_quandoNomeExistir() throws Exception {
        ModuleFather moduleFather = new ModuleFather();
        moduleFather.setId(1L);
        moduleFather.setName("Módulo Cozinha");

        ModuleFatherDTO dto = new ModuleFatherDTO(moduleFather);

        when(moduleFatherService.getModuleFatherByName("Módulo Cozinha")).thenReturn(dto);

        mockMvc.perform(get("/moduleFather/name")
                .param("name", "Módulo Cozinha"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Módulo Cozinha"));
    }

    // ==================== TESTES DO GET /moduleFather/all ====================

    @Test
    void findAll_deveRetornar200_comListaDeModulos() throws Exception {
        ModuleFather moduleFather = new ModuleFather();
        moduleFather.setId(1L);
        moduleFather.setName("Módulo Cozinha");

        Page<ModuleFatherDTO> page = new PageImpl<>(List.of(new ModuleFatherDTO(moduleFather)));

        when(moduleFatherService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/moduleFather/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Módulo Cozinha"));
    }

    // ==================== TESTES DO POST /moduleFather ====================

    @Test
    void createModuleFather_deveRetornar201_quandoDadosForemValidos() throws Exception {
        // PREPARAR: Cria o módulo que será retornado
        ModuleFather moduleFather = new ModuleFather();
        moduleFather.setId(1L);
        moduleFather.setName("Módulo Novo");

        when(moduleFatherService.save(any())).thenReturn(moduleFather);

        // JSON do corpo da requisição
        String json = """
                {
                    "name": "Módulo Novo"
                }
                """;

        // EXECUTAR E VERIFICAR
        mockMvc.perform(post("/moduleFather")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated()) // Espera HTTP 201
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Módulo Novo"));
    }

    // ==================== TESTES DO PUT /moduleFather/{id} ====================

    @Test
    void updateModuleFather_deveRetornar200_quandoAtualizarComSucesso() throws Exception {
        ModuleFather moduleFather = new ModuleFather();
        moduleFather.setId(1L);
        moduleFather.setName("Módulo Atualizado");

        when(moduleFatherService.update(eq(1L), any())).thenReturn(moduleFather);

        String json = """
                {
                    "name": "Módulo Atualizado"
                }
                """;

        mockMvc.perform(put("/moduleFather/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Módulo Atualizado"));
    }

    @Test
    void updateModuleFather_deveRetornar404_quandoIdNaoExistir() throws Exception {
        when(moduleFatherService.update(eq(999L), any()))
                .thenThrow(new NotFoundException("Módulo pai não encontrado"));

        String json = """
                {
                    "name": "Qualquer"
                }
                """;

        mockMvc.perform(put("/moduleFather/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    // ==================== TESTES DO DELETE /moduleFather/{id} ====================

    @Test
    void deleteModuleFather_deveRetornar200_quandoIdExistir() throws Exception {
        doNothing().when(moduleFatherService).delete(1L);

        mockMvc.perform(delete("/moduleFather/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteModuleFather_deveRetornar404_quandoIdNaoExistir() throws Exception {
        doThrow(new NotFoundException("Módulo pai não encontrado"))
                .when(moduleFatherService).delete(999L);

        mockMvc.perform(delete("/moduleFather/999"))
                .andExpect(status().isNotFound());
    }
}
