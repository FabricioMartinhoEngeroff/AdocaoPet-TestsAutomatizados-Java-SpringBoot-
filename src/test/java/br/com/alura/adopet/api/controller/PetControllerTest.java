package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.PetDto;
import br.com.alura.adopet.api.model.TipoPet;
import br.com.alura.adopet.api.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PetControllerTest {

    @Autowired
    private PetController petController;

    @MockBean
    private PetService petService;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    public void setUp() {
        PetDto pet1 = new PetDto(1L, TipoPet.CACHORRO, "Rex", "Labrador", 3);
        PetDto pet2 = new PetDto(2L, TipoPet.GATO, "Mia", "siames", 4);

        when(petService.buscarPetsDisponiveis()).thenReturn(Arrays.asList(pet1, pet2));
    }

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoDeListaOsPetsDisponiveis() throws Exception {

        MockHttpServletResponse response = mvc.perform(
                get("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(200,response.getStatus());
    }

    @Test
    public void deveriaListarPetComSucesso() throws Exception {

        mvc.perform(get("/pets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id': 1, 'tipo': 'CACHORRO', 'nome': 'Rex', 'raca': 'Labrador', 'idade': 3}, {'id': 2, 'tipo': 'GATO', 'nome': 'Mia', 'raca': 'siames', 'idade': 4}]"));
    }

    @Test
    public void deveriaRetornarListaVaziaQuandoNaoHouverAbrigos() throws Exception {

        when(petService.buscarPetsDisponiveis()).thenReturn(Collections.emptyList());

        mvc.perform(get("/pets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
