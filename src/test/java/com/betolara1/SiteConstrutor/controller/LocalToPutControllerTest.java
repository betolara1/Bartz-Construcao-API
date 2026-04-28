package com.betolara1.SiteConstrutor.controller;

// Importações dos models, DTOs e services do projeto
import com.betolara1.controller.LocalToPutController;
import com.betolara1.dto.response.LocalToPutDTO;
import com.betolara1.exception.NotFoundException;
import com.betolara1.model.LocalToPut;
import com.betolara1.service.LocalToPutService;

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

// @WebMvcTest carrega APENAS o LocalToPutController
@WebMvcTest(LocalToPutController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LocalToPutControllerTest {

    // MockMvc simula requisições HTTP
    @Autowired
    private MockMvc mockMvc;

    // "Dublê" do service
    @MockitoBean
    private LocalToPutService localToPutService;

    // ==================== TESTES DO GET /local/id ====================

    @Test
    void findById_deveRetornar200_quandoIdExistir() throws Exception {
        // PREPARAR: Cria um local fictício
        LocalToPut local = new LocalToPut();
        local.setId(1L);
        local.setName("Parede");

        LocalToPutDTO dto = new LocalToPutDTO(local);

        when(localToPutService.findById(1L)).thenReturn(dto);

        // EXECUTAR E VERIFICAR
        mockMvc.perform(get("/locals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Parede"));
    }

    @Test
    void findById_deveRetornar404_quandoIdNaoExistir() throws Exception {
        when(localToPutService.findById(999L))
                .thenThrow(new NotFoundException("ID não Encontrado"));

        mockMvc.perform(get("/locals/999"))
                .andExpect(status().isNotFound());
    }

    // ==================== TESTES DO GET /local/name ====================

    @Test
    void findByName_deveRetornar200_quandoNomeExistir() throws Exception {
        LocalToPut local = new LocalToPut();
        local.setId(1L);
        local.setName("Chão");

        LocalToPutDTO dto = new LocalToPutDTO(local);

        when(localToPutService.findByName("Chão")).thenReturn(dto);

        mockMvc.perform(get("/locals")
                .param("name", "Chão"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Chão"));
    }

    // ==================== TESTES DO GET /local/all ====================

    @Test
    void findAll_deveRetornar200_comListaDeLocais() throws Exception {
        LocalToPut local = new LocalToPut();
        local.setId(1L);
        local.setName("Parede");

        Page<LocalToPutDTO> page = new PageImpl<>(List.of(new LocalToPutDTO(local)));

        when(localToPutService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/locals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Parede"));
    }

    // ==================== TESTES DO POST /local ====================

    @Test
    void save_deveRetornar201_quandoDadosForemValidos() throws Exception {
        // PREPARAR
        LocalToPut local = new LocalToPut();
        local.setId(1L);
        local.setName("Parede");
        // O save do LocalToPutService agora retorna a Entidade LocalToPut
        when(localToPutService.save(any())).thenReturn(local);

        String json = """
                {
                    "name": "Parede"
                }
                """;

        // EXECUTAR E VERIFICAR
        mockMvc.perform(post("/locals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated()) // Espera HTTP 201
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Parede"));
    }

    // ==================== TESTES DO PUT /local/{id} ====================

    @Test
    void update_deveRetornar200_quandoAtualizarComSucesso() throws Exception {
        LocalToPut local = new LocalToPut();
        local.setId(1L);
        local.setName("Chão");

        // O update do LocalToPutService agora retorna a Entidade LocalToPut
        when(localToPutService.update(eq(1L), any())).thenReturn(local);

        String json = """
                {
                    "name": "Chão"
                }
                """;

        mockMvc.perform(put("/locals/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Chão"));
    }

    @Test
    void update_deveRetornar404_quandoIdNaoExistir() throws Exception {
        when(localToPutService.update(eq(999L), any()))
                .thenThrow(new NotFoundException("Local não encontrado"));

        String json = """
                {
                    "name": "Qualquer"
                }
                """;

        mockMvc.perform(put("/locals/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    // ==================== TESTES DO DELETE /local/{id} ====================

    @Test
    void deleteLocal_deveRetornar200_quandoIdExistir() throws Exception {
        doNothing().when(localToPutService).delete(1L);

        mockMvc.perform(delete("/locals/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteLocal_deveRetornar404_quandoIdNaoExistir() throws Exception {
        doThrow(new NotFoundException("Local não encontrado"))
                .when(localToPutService).delete(999L);

        mockMvc.perform(delete("/locals/999"))
                .andExpect(status().isNotFound());
    }
}
