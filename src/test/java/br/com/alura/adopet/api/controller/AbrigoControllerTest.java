package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.AbrigoDto;
import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.dto.PetDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.TipoPet;
import br.com.alura.adopet.api.service.AbrigoService;
import br.com.alura.adopet.api.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AbrigoControllerTest {

    @Autowired
    private AbrigoController abrigoContoller;

    @MockBean
    private AbrigoService abrigoService;

    @MockBean
    private Abrigo abrigo;

    @MockBean
    private PetService petService;
    @Autowired
    private MockMvc mvc;

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoDeListarAbrigos() throws Exception {
        //ACT

        MockHttpServletResponse response = mvc.perform(
                get("/abrigos")
        ).andReturn().getResponse();

        //ASSERT
        assertEquals(200, response.getStatus());
    }

    @Test
    public void deveriaListarAbrigosComSucesso() throws Exception {

        AbrigoDto abrigo1 = new AbrigoDto(1L, "Joana");
        AbrigoDto abrigo2 = new AbrigoDto(2L, "Bob");

        when(abrigoService.listar()).thenReturn(Arrays.asList(abrigo1, abrigo2));

        mvc.perform(get("/abrigos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id': 1, 'nome': 'Joana'}, {'id': 2, 'nome': 'Bob'}]"));
    }

    @Test
    void deveriaDevolverCodigo200ParaRequisitoDeListaPetsDosAbrigosPorNome() throws Exception {

        String nome = "Adocao Joana";

        MockHttpServletResponse response = mvc.perform(
                get("/abrigos/{nome}/pets", nome)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaRequisitoDeListaPetsDosAbrigosPorId() throws Exception {

        String id = "1";

        MockHttpServletResponse response = mvc.perform(
                get("/abrigos/{nome}/pets", id)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    public void deveriaRetornarListaVaziaQuandoNaoHouverAbrigos() throws Exception {

        when(abrigoService.listar()).thenReturn(Collections.emptyList());

        mvc.perform(get("/abrigos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }


    @Test
    void deveriaDevolverCodigo400ParaCadastroDeAbrigoComErro() throws Exception {

        String json = "{}";

        var response = mvc.perform(
                post("/abrigos")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaCadastroDeAbrigo() throws Exception {

        String json = """
                {
                      "nome": "AbrigoJoana",
                    "telefone": "5135457555",
                    "email": "joana@gmail.com"
                }
                """;

        var response = mvc.perform(
                post("/abrigos")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo400ParaCadastroPetNoAbrigo() throws Exception {

        String json = "{}";


        var response = mvc.perform(
                post("/abrigos")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo400ParaCadastroDePetInvalido() throws Exception {
        CadastroPetDto dto = new CadastroPetDto(TipoPet.CACHORRO,  " ", "Labrador" , 3,"amarelo",-2f);

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletResponse response = mvc.perform(
                post("/abrigos/{idOuNome}/pets", "idOuNomeInvalido")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());

    }

    @Test
    void deveriaDevolverCodigo200ParaCadastroPetNoAbrigo() throws Exception {
        CadastroPetDto dto = new CadastroPetDto(TipoPet.CACHORRO, "Rex", "Labrador", 3, "Amarelo", 30.5f);
        String json = """
                {
                "tipo": "CACHORRO",
                "nome": "Rex",
                "raca": "Labrador",
                "idade": 3,
                "cor": "Amarelo",
                "peso": 30.5
                }
                """;

        abrigo.setId("1");

        when(abrigoService.carregarAbrigo("idOuNomeValido")).thenReturn(abrigo);

        Mockito.doNothing().when(petService).cadastrarPet(any(Abrigo.class), any(CadastroPetDto.class));

        var response = mvc.perform(
                post("/abrigos/{idOuNome}/pets", "idOuNomeValido")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoDeListarPetsDoAbrigo() throws Exception {

        String nome = "Miau";

        given(abrigoService.listarPetsDoAbrigo(abrigo.getNome())).willThrow(ValidacaoException.class);

        MockHttpServletResponse response = mvc.perform(
                get("/abrigos/{nome}/pets", nome)
        ).andReturn().getResponse();


        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverListaDePetsParaIdValido() throws Exception {
        long idAbrigo = 1L;
        List<PetDto> pets = Arrays.asList(
                new PetDto(1L, TipoPet.CACHORRO, "Rex", "Labrador", 3),
                new PetDto(2L, TipoPet.GATO, "Miau", "Siames", 2));

        given(abrigoService.listarPetsDoAbrigo(String.valueOf(idAbrigo))).willReturn(pets);

        mvc.perform(get("/abrigos/{id}/pets", idAbrigo))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id': 1, 'tipo': 'CACHORRO', 'nome': 'Rex', 'raca': 'Labrador', 'idade': 3}, {'id': 2, 'tipo': 'GATO', 'nome': 'Miau', 'raca': 'Siames', 'idade': 2}]"));
    }

}

