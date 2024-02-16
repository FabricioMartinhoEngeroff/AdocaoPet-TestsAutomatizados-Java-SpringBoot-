package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidacaoPetComAdocaoEmAndamentoTest {

    @Mock
    private AdocaoRepository adocaoRepository;

    @InjectMocks
    private ValidacaoPetComAdocaoEmAndamento validacao;

    @Mock
    private Pet pet;

    @Mock
    private SolicitacaoAdocaoDto dto;


    @Test
    void deveriaValidarAdocaoDoPet() {

        Long idPet = 1L;
        BDDMockito.given(dto.idPet()).willReturn(idPet);
        BDDMockito.given(adocaoRepository.existsByPetIdAndStatus(idPet, StatusAdocao.AGUARDANDO_AVALIACAO))
                .willReturn(false);


        Assertions.assertDoesNotThrow(() -> validacao.validar(dto));
    }

    @Test
    void naoDeveriaValidarAdocaoDoPet() {

        Long idPet = 1L;
        BDDMockito.given(dto.idPet()).willReturn(idPet);
        BDDMockito.given(adocaoRepository.existsByPetIdAndStatus(idPet, StatusAdocao.AGUARDANDO_AVALIACAO))
                .willReturn(true);


        Assertions.assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
    }

}