package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ValidacaoTutorComAdocaoEmAndamentoTest {

    @InjectMocks
    private ValidacaoTutorComAdocaoEmAndamento validacao;
    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private Tutor tutor;

    @Mock
    private Adocao adocao;

    @Mock
    private SolicitacaoAdocaoDto dto;


    @Test
    void deveriaValidarTutorComAdocaoEmAndamento(){

        Long idTutor = 1L;
        BDDMockito.given(dto.idTutor()).willReturn(idTutor);
        BDDMockito.given(tutorRepository.getReferenceById(idTutor)).willReturn(tutor);
        BDDMockito.given(adocao.getTutor()).willReturn(tutor);
        BDDMockito.given(adocao.getStatus()).willReturn(StatusAdocao.AGUARDANDO_AVALIACAO);
        BDDMockito.given(adocaoRepository.findAll()).willReturn(List.of(adocao));

        Assertions.assertThrows(ValidacaoException.class, () -> validacao.validar(dto));

    }

    @Test
    void naoDeveriaValidarTutorComAdocaoEmAndamento(){

        Long idTutor = 1L;
        BDDMockito.given(dto.idTutor()).willReturn(idTutor);
        BDDMockito.given(tutorRepository.getReferenceById(idTutor)).willReturn(tutor);
        BDDMockito.given(adocao.getTutor()).willReturn(tutor);
        BDDMockito.given(adocao.getStatus()).willReturn(StatusAdocao.AGUARDANDO_AVALIACAO);
        BDDMockito.given(adocaoRepository.findAll()).willReturn(List.of(adocao));

        ValidacaoException exception = Assertions.assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
        Assertions.assertEquals("Tutor já possui outra adoção aguardando avaliação!", exception.getMessage());
    }

}