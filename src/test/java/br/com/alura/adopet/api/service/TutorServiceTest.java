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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


@ExtendWith(MockitoExtension.class)
class TutorServiceTest {

    @InjectMocks
    private TutorService service;

    @Mock
    private TutorRepository repository;

    @Mock
    private CadastroTutorDto dto;

    @Mock
    private Tutor tutor;

    @Mock
    private AtualizacaoTutorDto atualizacaoTutorDto;

    @Test
    void NaoDeveriaCadastrarTutorTelefoneOuEmailJaCadastrado() {

        given(repository.existsByTelefoneOrEmail(dto.telefone(), dto.email())).willReturn(true);

        assertThrows(ValidacaoException.class, () -> service.cadastrar(dto));
    }

    @Test
    void deveriaCadastrarTutor() {

        given(repository.existsByTelefoneOrEmail(dto.telefone(), dto.email())).willReturn(false);

        assertDoesNotThrow(() -> service.cadastrar(dto));
        then(repository).should().save(new Tutor(dto));
    }

    @Test
    void deveriaAtualizarDadosTutor() {

        given(repository.getReferenceById(atualizacaoTutorDto.id())).willReturn(tutor);

        service.atualizar(atualizacaoTutorDto);

        then(tutor).should().atualizarDados(atualizacaoTutorDto);
    }

}
