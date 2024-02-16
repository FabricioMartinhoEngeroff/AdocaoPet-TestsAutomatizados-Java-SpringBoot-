package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.dto.PetDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.TipoPet;
import br.com.alura.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @InjectMocks
    private PetService petService;

    @Mock
    private PetRepository petRepository;

    @Spy
    private List<Pet> pets = new ArrayList<>();

    @Mock
    private Pet pet1;
    @Mock
    private Pet pet2;

    @Captor
    private ArgumentCaptor<Pet> petCaptor;

    @Test
    void deveriaBuscarPetsDisponiveis() {
        pets.add(pet1);
        pets.add(pet2);

        given(petRepository.findAllByAdotadoFalse()).willReturn(pets);

        List<PetDto> result = petService.buscarPetsDisponiveis();

        then(petRepository).should().findAllByAdotadoFalse();
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void deveriaCadastrarPet() {
        Abrigo abrigo = new Abrigo();
        CadastroPetDto dto = new CadastroPetDto(TipoPet.CACHORRO, "Peludo", "", 7, "Branco", 4.0f);
        Pet pet = new Pet(dto, abrigo);

        petService.cadastrarPet(abrigo, dto);

        then(petRepository).should().save(petCaptor.capture());
        Pet petSalvo = petCaptor.getValue();
        Assertions.assertEquals(pet, petSalvo);
    }
}