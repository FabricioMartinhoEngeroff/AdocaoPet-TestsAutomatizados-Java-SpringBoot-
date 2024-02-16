package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.service.TutorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TutorControllerTest {

    @Autowired
    private TutorController tutorController;

    @MockBean
    private TutorService service;

    @Autowired
    private MockMvc mvc;

    private CadastroTutorDto dto;

    @Test
    void deveriaDevolverCodigo400ParaCadastroComErro() throws Exception {

        String json = "{}";

        var response = mvc.perform(
                post("/tutores")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaCadastroDeAbrigo() throws Exception {

        String json = """
                {
                      "nome": "Ana",
                    "telefone": "5135457555",
                    "email": "ana@gmail.com"
                }
                """;

        var response = mvc.perform(
                post("/tutores")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaRetornarBadRequestQuandoNomeEstiverFaltando() throws Exception {
        String json = """
                {
                    "telefone": "5135457555",
                    "email": "ana@gmail.com"
                }
                """;

        var response = mvc.perform(
                post("/tutores")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaRetornarBadRequestQuandoEmailEstiverInvalido() throws Exception {
        String json = """
                {
                    "nome": "Ana",
                    "telefone": "5135457555",
                    "email": "ana"
                }
                """;

        var response = mvc.perform(
                post("/tutores")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaLancarValidationException() throws Exception {
        // Arrange
        CadastroTutorDto dto = new CadastroTutorDto("Joao", "5134547555", "Joao@gmail.com");
        String json = new ObjectMapper().writeValueAsString(dto);

        doThrow(new ValidacaoException("Error message")).when(service).cadastrar(any(CadastroTutorDto.class));

        // Act and Assert
        mvc.perform(
                        post("/tutores")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest()); // Verifica se a resposta é um Bad Request (400)
    }
}