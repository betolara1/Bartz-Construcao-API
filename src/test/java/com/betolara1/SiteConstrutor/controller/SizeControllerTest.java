package com.betolara1.SiteConstrutor.controller;

// Importações dos models, DTOs e services do projeto
import com.betolara1.controller.SizeController;
import com.betolara1.dto.response.SizeDTO;
import com.betolara1.exception.NotFoundException;
import com.betolara1.model.Size;
import com.betolara1.service.SizeService;

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

// @WebMvcTest carrega APENAS o SizeController
@WebMvcTest(SizeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SizeControllerTest {

    // MockMvc simula requisições HTTP
    @Autowired
    private MockMvc mockMvc;

    // "Dublê" do service
    @MockitoBean
    private SizeService sizeService;

    // ==================== TESTES DO GET /sizes/id ====================

    @Test
    void findById_deveRetornar200_quandoIdExistir() throws Exception {
        // PREPARAR: Cria um Size fictício
        Size size = new Size();
        size.setId(1L);
        size.setHeightMax(1200.0);
        size.setHeightMin(100.0);

        SizeDTO dto = new SizeDTO(size);

        when(sizeService.findById(1L)).thenReturn(dto);

        // EXECUTAR E VERIFICAR
        mockMvc.perform(get("/sizes/id")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.heightMax").value(1200.0));
    }

    @Test
    void findById_deveRetornar404_quandoIdNaoExistir() throws Exception {
        when(sizeService.findById(999L))
                .thenThrow(new NotFoundException("Tamanho não encontrado"));

        mockMvc.perform(get("/sizes/id")
                .param("id", "999"))
                .andExpect(status().isNotFound());
    }

    // ==================== TESTES DO GET /sizes/idProduct ====================

    @Test
    void findByIdProduct_deveRetornar200_quandoProductIdExistir() throws Exception {
        Size size = new Size();
        size.setId(1L);

        SizeDTO dto = new SizeDTO(size);

        when(sizeService.findByIdProduct(1L)).thenReturn(dto);

        mockMvc.perform(get("/sizes/idProduct")
                .param("idProduct", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    // ==================== TESTES DO GET /sizes/all ====================

    @Test
    void findAll_deveRetornar200_comListaDeSizes() throws Exception {
        Size size = new Size();
        size.setId(1L);
        size.setHeightMax(500.0);

        Page<SizeDTO> page = new PageImpl<>(List.of(new SizeDTO(size)));

        when(sizeService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/sizes/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].heightMax").value(500.0));
    }

    // ==================== TESTES DO POST /sizes ====================

    @Test
    void save_deveRetornar201_quandoDadosForemValidos() throws Exception {
        // PREPARAR: Cria o Size que será retornado
        Size size = new Size();
        size.setId(1L);
        size.setHeightMax(100.0);
        size.setHeightMin(50.0);
        size.setWidthMax(200.0);
        size.setWidthMin(80.0);
        size.setDepthMax(150.0);
        size.setDepthMin(40.0);

        when(sizeService.save(any())).thenReturn(size);

        // JSON do corpo da requisição
        String json = """
                {
                    "product": {"id": 1},
                    "heightMax": 100.0,
                    "heightMin": 50.0,
                    "widthMax": 200.0,
                    "widthMin": 80.0,
                    "depthMax": 150.0,
                    "depthMin": 40.0
                }
                """;

        // EXECUTAR E VERIFICAR
        mockMvc.perform(post("/sizes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated()) // Espera HTTP 201
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.heightMax").value(100.0));
    }

    // ==================== TESTES DO PUT /sizes/{id} ====================

    @Test
    void update_deveRetornar200_quandoAtualizarComSucesso() throws Exception {
        Size size = new Size();
        size.setId(1L);
        size.setHeightMax(1000.0);

        when(sizeService.update(eq(1L), any())).thenReturn(size);

        String json = """
                {
                    "heightMax": 1000.0
                }
                """;

        mockMvc.perform(put("/sizes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.heightMax").value(1000.0));
    }

    @Test
    void update_deveRetornar404_quandoIdNaoExistir() throws Exception {
        when(sizeService.update(eq(999L), any()))
                .thenThrow(new NotFoundException("Tamanho não encontrado"));

        String json = """
                {
                    "heightMax": 1000.0
                }
                """;

        mockMvc.perform(put("/sizes/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    // ==================== TESTES DO DELETE /sizes/{id} ====================

    @Test
    void delete_deveRetornar204_quandoIdExistir() throws Exception {
        // O SizeController retorna noContent() (204) ao deletar, diferente dos outros
        doNothing().when(sizeService).delete(1L);

        mockMvc.perform(delete("/sizes/1"))
                .andExpect(status().isNoContent()); // Espera HTTP 204
    }

    @Test
    void delete_deveRetornar404_quandoIdNaoExistir() throws Exception {
        doThrow(new NotFoundException("Tamanho não encontrado"))
                .when(sizeService).delete(999L);

        mockMvc.perform(delete("/sizes/999"))
                .andExpect(status().isNotFound());
    }
}
