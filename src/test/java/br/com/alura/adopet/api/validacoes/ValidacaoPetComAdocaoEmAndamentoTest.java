package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ValidacaoPetComAdocaoEmAndamentoTest {

    @Mock
    private AdocaoRepository adocaoRepository;

    @InjectMocks
    private ValidacaoPetComAdocaoEmAndamento validador;

    @Mock
    private SolicitacaoAdocaoDto dto;


    @Test
    void deveriaValidarAdocaoDoPet() {

        given(adocaoRepository.existsByPetIdAndStatus(
                dto.idPet(),
                StatusAdocao.AGUARDANDO_AVALIACAO
        )).willReturn(false);

        assertDoesNotThrow(()->validador.validar(dto));
    }


    @Test
    void naoDeveriaValidarAdocaoDoPet() {

        given(adocaoRepository.existsByPetIdAndStatus(
                dto.idPet(),
                StatusAdocao.AGUARDANDO_AVALIACAO)
        ).willReturn(true);

        assertThrows(ValidacaoException.class, () -> validador.validar(dto));
    }

}

