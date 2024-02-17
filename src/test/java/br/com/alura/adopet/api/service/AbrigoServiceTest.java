package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AbrigoDto;
import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.PetDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbrigoServiceTest {

    @InjectMocks
    private AbrigoService abrigoService;

    @Mock
    private AbrigoRepository abrigoRepository;

    @Mock
    private PetRepository petRepository;


    @Mock
    private Abrigo abrigo;

    @Test
    void deveriaChamarListaDeTodosOsAbrigos() {
        //Act
        abrigoService.listar();

        //Assert
        then(abrigoRepository).should().findAll();
    }


    @Test
    void deveriaChamarListaDePetsDoAbrigoAtravesDoNome() {
        //Arrange
        String nome = "Miau";
        given(abrigoRepository.findByNome(nome)).willReturn(Optional.of(abrigo));

        //Act
        abrigoService.listarPetsDoAbrigo(nome);

        //Assert
        then(petRepository).should().findByAbrigo(abrigo);
    }

    @Test
    void deveriaCarregarAbrigoPorId() {

        when(abrigoRepository.findById(1L)).thenReturn(Optional.of(abrigo));

        Abrigo result = abrigoService.carregarAbrigo("1");

        assertEquals(abrigo, result);
    }


    @Test
    void deveriaCarregarAbrigoPorNome() {

        when(abrigoRepository.findByNome("AbrigoJoana")).thenReturn(Optional.of(abrigo));

        Abrigo result = abrigoService.carregarAbrigo("AbrigoJoana");

        assertEquals(abrigo, result);
    }

    @Test
    void deveriaLancarExcecaoQuandoAbrigoNaoEncontrado() {

        when(abrigoRepository.findById(1L)).thenReturn(Optional.empty());
        when(abrigoRepository.findByNome("AbrigoJoana")).thenReturn(Optional.empty());

        assertThrows(ValidacaoException.class, () -> abrigoService.carregarAbrigo("1"));
        assertThrows(ValidacaoException.class, () -> abrigoService.carregarAbrigo("AbrigoJoana"));
    }
}
