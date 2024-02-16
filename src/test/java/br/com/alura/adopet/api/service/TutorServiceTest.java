package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {
    @InjectMocks
    private TutorService tutorService;

    @Mock
    private TutorRepository tutorRepository;

    @Captor
    private ArgumentCaptor<Tutor> tutorCaptor;

    @Test
    void deveriaCadastrarUmTutor() {
        CadastroTutorDto dto = new CadastroTutorDto("Joao", "5135347788", "joao@gmail.com");

        given(tutorRepository.existsByTelefoneOrEmail(dto.telefone(), dto.email())).willReturn(false);

        tutorService.cadastrar(dto);

        then(tutorRepository).should().save(tutorCaptor.capture());
        Tutor tutorSalvo = tutorCaptor.getValue();
        Assertions.assertEquals(dto.nome(), tutorSalvo.getNome());
        Assertions.assertEquals(dto.telefone(), tutorSalvo.getTelefone());
        Assertions.assertEquals(dto.email(), tutorSalvo.getEmail());
    }

    @Test
    void naoDeveriaCadastrarUmTutorComDadosJaCadastrados() {
        CadastroTutorDto dto = new CadastroTutorDto("Joao", "5135347788", "joao@gmail.com");

        given(tutorRepository.existsByTelefoneOrEmail(dto.telefone(), dto.email())).willReturn(true);

        Assertions.assertThrows(ValidacaoException.class, () -> {
            tutorService.cadastrar(dto);
        });
    }

    @Test
    void deveriaAtualizarUmTutor() {
        CadastroTutorDto cadastroDto = new CadastroTutorDto("Joao", "5135347788", "joao@gmail.com");
        Tutor tutor = new Tutor(cadastroDto);

        AtualizacaoTutorDto atualizacaoDto = new AtualizacaoTutorDto(1L, "Joao Atualizado", "5135347789", "joao_atualizado@gmail.com");

        given(tutorRepository.getReferenceById(atualizacaoDto.id())).willReturn(tutor);

        tutorService.atualizar(atualizacaoDto);

        Assertions.assertEquals(atualizacaoDto.nome(), tutor.getNome());
        Assertions.assertEquals(atualizacaoDto.telefone(), tutor.getTelefone());
        Assertions.assertEquals(atualizacaoDto.email(), tutor.getEmail());
    }
}