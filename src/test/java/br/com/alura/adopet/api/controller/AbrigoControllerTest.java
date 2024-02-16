package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.AbrigoDto;
import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.TipoPet;
import br.com.alura.adopet.api.service.AbrigoService;
import br.com.alura.adopet.api.service.PetService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private PetService petService;
    @Autowired
    private MockMvc mvc;

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
    public void deveriaRetornarListaVaziaQuandoNaoHouverAbrigos() throws Exception {

        when(abrigoService.listar()).thenReturn(Collections.emptyList());

        mvc.perform(get("/abrigos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }


    @Test
void deveriaDevolverCodigo400ParaCadastroComErro() throws Exception {

    String json = "{}";

    var response = mvc.perform(
            post("/abrigos")
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON)
    ).andReturn().getResponse();

    Assertions.assertEquals(400, response.getStatus());
}

@Test
void deveriaDevolverCodigo200ParaCadastroDeAbrigo() throws Exception {

    String json = """
            {
                  "nome": "joana",
                "telefone": "5135457555",
                "email": "joana@gmail.com"
            }
            """;

    var response = mvc.perform(
            post("/abrigos")
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON)
    ).andReturn().getResponse();

    Assertions.assertEquals(200, response.getStatus());
}

@Test
void deveriaDevolverCodigo400ParaCadastroPetComErro() throws Exception {

    String json = "{}";

    var response = mvc.perform(
            post("/abrigos")
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON)
    ).andReturn().getResponse();

    Assertions.assertEquals(400, response.getStatus());
}

@Test
void deveriaDevolverCodigo200ParaCadastroPetComSucesso() throws Exception {
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

    // Crie um abrigo mock
    Abrigo abrigoMock = new Abrigo();
    abrigoMock.setId("1");

    when(abrigoService.carregarAbrigo("idOuNomeValido")).thenReturn(abrigoMock);

    Mockito.doNothing().when(petService).cadastrarPet(Mockito.any(Abrigo.class), Mockito.any(CadastroPetDto.class));

    var response = mvc.perform(
            post("/abrigos/{idOuNome}/pets", "idOuNomeValido")
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON)
    ).andReturn().getResponse();

    Assertions.assertEquals(200, response.getStatus());
}

}