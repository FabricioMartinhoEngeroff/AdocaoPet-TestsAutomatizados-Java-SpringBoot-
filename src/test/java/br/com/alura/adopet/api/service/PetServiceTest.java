package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.TipoPet;
import br.com.alura.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @InjectMocks
    private PetService petService;

    @Mock
    private PetRepository petRepository;

    @Mock
    private CadastroPetDto cadastroPetDto;

    @Mock
    private Abrigo abrigo;

    @Test
    void deveriaBuscarPetsDisponiveis() {

        petService.cadastrarPet(abrigo, cadastroPetDto);

        then(petRepository).should().save(new Pet(cadastroPetDto, abrigo));
    }

    @Test
    void deveriaCadastrarPet() {

        petService.buscarPetsDisponiveis();

        then(petRepository).should().findAllByAdotadoFalse();
    }
}