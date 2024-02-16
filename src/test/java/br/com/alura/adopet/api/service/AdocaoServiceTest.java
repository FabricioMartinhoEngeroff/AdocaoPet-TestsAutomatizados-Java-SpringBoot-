package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.ReprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validacoes.ValidacaoSolicitacaoAdocao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AdocaoServiceTest {

    @InjectMocks
    private AdocaoService adocaoService;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private EmailService emailService;

    @Spy
    private List<ValidacaoSolicitacaoAdocao> validacoes = new ArrayList<>();

    @Mock
    private ValidacaoSolicitacaoAdocao validador1;
    @Mock
    private ValidacaoSolicitacaoAdocao validador2;

    @Mock
    private Pet pet;

    @Mock
    private Tutor tutor;

    @Mock
    private Abrigo abrigo;

    private SolicitacaoAdocaoDto dto;

    private AprovacaoAdocaoDto dto2;

    private ReprovacaoAdocaoDto dto3;
    @Mock
    private Adocao adocao;

    @Captor
    private ArgumentCaptor<Adocao> adocaoCaptor;

    @Test
    void deveriaSalvarAdocaoAoSolicitar() {
        this.dto = new SolicitacaoAdocaoDto(10l, 20l, "motivo qualquer");
        given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);

        adocaoService.solicitar(dto);

        then(adocaoRepository).should().save(adocaoCaptor.capture());
        Adocao adocaoSalva = adocaoCaptor.getValue();
        Assertions.assertEquals(pet, adocaoSalva.getPet());
        Assertions.assertEquals(tutor, adocaoSalva.getTutor());
        Assertions.assertEquals(dto.motivo(), adocaoSalva.getMotivo());
    }

    @Test
    void deveriaChamarValidadoreDeAdocaoAoSolicitar() {
        this.dto = new SolicitacaoAdocaoDto(10l, 20l, "motivo qualquer");
        given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);
        validacoes.add(validador1);
        validacoes.add(validador2);

        adocaoService.solicitar(dto);

        BDDMockito.then(validador1).should().validar(dto);
        BDDMockito.then(validador2).should().validar(dto);
    }

    @Test
    void deveriaAprovarAdocao() {
        this.dto2 = new AprovacaoAdocaoDto(10L);
        given(adocaoRepository.getReferenceById(dto2.idAdocao())).willReturn(adocao);
        given(adocao.getPet()).willReturn(pet);
        given(adocao.getTutor()).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);

        LocalDateTime dataAdocao = LocalDateTime.now();
        given(adocao.getData()).willReturn(dataAdocao);

        adocaoService.aprovar(dto2);

        then(adocao).should().marcarComoAprovada();
    }

    @Test
    void deveriaReprovarAdocao() {
        this.dto3 = new ReprovacaoAdocaoDto(1L, "Tutor tem algo errado na sua Adocão verifique-se");
        given(adocaoRepository.getReferenceById(dto3.idAdocao())).willReturn(adocao);
        given(adocao.getPet()).willReturn(pet);
        given(adocao.getTutor()).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);


        LocalDateTime dataAdocao = LocalDateTime.now();
        given(adocao.getData()).willReturn(dataAdocao);

        adocaoService.reprovar(dto3);

        then(adocao).should().marcarComoReprovada(dto3.justificativa());
    }

}