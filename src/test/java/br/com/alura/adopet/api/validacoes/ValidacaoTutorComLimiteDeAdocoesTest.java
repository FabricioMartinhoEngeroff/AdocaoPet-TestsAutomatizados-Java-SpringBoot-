package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
@ExtendWith(MockitoExtension.class)
class ValidacaoTutorComLimiteDeAdocoesTest {

    @InjectMocks
    private ValidacaoTutorComLimiteDeAdocoes validacao;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private Tutor tutor;

    @Mock
    private SolicitacaoAdocaoDto dto;

    @BeforeEach
    void setUp() {
        Long idTutor = 1L;
        BDDMockito.given(dto.idTutor()).willReturn(idTutor);
        BDDMockito.given(tutorRepository.getReferenceById(idTutor)).willReturn(tutor);
    }

    @Test
    void deveriaValidarQuandoTutorTemMenosDeCincoAdocoesAprovadas() {
        List<Adocao> adocoes = criarAdocoes(StatusAdocao.APROVADO, 4);
        BDDMockito.given(adocaoRepository.findAll()).willReturn(adocoes);

        Assertions.assertDoesNotThrow(() -> validacao.validar(dto));
    }

    @Test
    void deveriaReprovarQuandoTutorTemCincoOuMaisAdocoesAprovadas() {
        List<Adocao> adocoes = criarAdocoes(StatusAdocao.APROVADO, 5);
        BDDMockito.given(adocaoRepository.findAll()).willReturn(adocoes);

        assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
    }

    private List<Adocao> criarAdocoes(StatusAdocao status, int quantidade) {
        List<Adocao> adocoes = new ArrayList<>();
        for (int i = 0; i < quantidade; i++) {
            Adocao adocao = Mockito.mock(Adocao.class);
            BDDMockito.given(adocao.getTutor()).willReturn(tutor);
            BDDMockito.given(adocao.getStatus()).willReturn(status);
            adocoes.add(adocao);
        }
        return adocoes;
    }
}

