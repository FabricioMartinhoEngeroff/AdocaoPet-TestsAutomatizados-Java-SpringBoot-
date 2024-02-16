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

    private CadastroAbrigoDto dto;

    @Mock
    private Pet pet;

    @Mock
    private Tutor tutor;

    @Mock
    private Abrigo abrigo;

    @Captor
    private ArgumentCaptor<Abrigo> abrigoCaptor;

    @Test
    void deveriaListarTodosOsAbrigos() {

        CadastroAbrigoDto dto1 = new CadastroAbrigoDto("AbrigoJoana", "35457544", "JoanaAbrigo@gmail.com");
        CadastroAbrigoDto dto2 = new CadastroAbrigoDto("AbrigoBob", "35357555", "BobAbrigo@gmail.com");
        Abrigo abrigo1 = new Abrigo(dto1);
        Abrigo abrigo2 = new Abrigo(dto2);
        List<Abrigo> abrigos = Arrays.asList(abrigo1, abrigo2);
        given(abrigoRepository.findAll()).willReturn(abrigos);

        List<AbrigoDto> result = abrigoService.listar();

        assertEquals(abrigos.size(), result.size(), "O número de AbrigoDto deve ser igual ao número de Abrigos");
        for (int i = 0; i < result.size(); i++) {
            AbrigoDto dto = result.get(i);
            Abrigo abrigo = abrigos.get(i);
            assertEquals(abrigo.getNome(), dto.nome(), "O nome do abrigo deve corresponder");

        }
    }

    @Test
    void deveriaCadastrarAbrigo() {
        this.dto = new CadastroAbrigoDto("abrigoJoana", "34547544", "joanaAbrigo@gmail.com");

        given(abrigoRepository.existsByNomeOrTelefoneOrEmail(dto.nome(), dto.telefone(), dto.email())).willReturn(false);

        abrigoService.cadastrar(dto);

        then(abrigoRepository).should().save(abrigoCaptor.capture());
        Abrigo abrigoSalvo = abrigoCaptor.getValue();
        assertEquals(dto.nome(), abrigoSalvo.getNome());
        assertEquals(dto.telefone(), abrigoSalvo.getTelefone());
        assertEquals(dto.email(), abrigoSalvo.getEmail());


    }

    @Test
    void deveriaListarPetsDoAbrigo() {

        Pet pet1 = Mockito.mock(Pet.class);
        Pet pet2 = Mockito.mock(Pet.class);
        List<Pet> pets = Arrays.asList(pet1, pet2);

        when(abrigoRepository.findById(1L)).thenReturn(Optional.of(abrigo));
        when(petRepository.findByAbrigo(abrigo)).thenReturn(pets);

        List<PetDto> result = abrigoService.listarPetsDoAbrigo("1");

        assertEquals(pets.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            PetDto dto = result.get(i);
            Pet pet = pets.get(i);
        }
    }

    @Test
    void deveriaCarregarAbrigoPorId() {

        when(abrigoRepository.findById(1L)).thenReturn(Optional.of(abrigo));

        Abrigo result = abrigoService.carregarAbrigo("1");

        assertEquals(abrigo, result);
    }


    @Test
    void deveriaCarregarAbrigoPorNome() {

        Abrigo abrigo = Mockito.mock(Abrigo.class);

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
