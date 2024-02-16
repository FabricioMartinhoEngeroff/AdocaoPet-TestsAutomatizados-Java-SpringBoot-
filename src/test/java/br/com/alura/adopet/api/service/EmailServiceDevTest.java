package br.com.alura.adopet.api.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintStream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class EmailServiceDevTest {

    @InjectMocks
    private EmailServiceDev emailService;

    @Mock
    private PrintStream outMock;

    @BeforeEach
    void setUp() {
        System.setOut(outMock);
    }

    @AfterEach
    void tearDown() {
        System.setOut(System.out); // Restaurar a saída padrão após o teste
    }

    @Test
    void deveriaEnviarEmail() {

        String destinatario = "test@example.com";
        String assunto = "Test Subject";
        String mensagem = "Test Message";

        emailService.enviarEmail(destinatario, assunto, mensagem);

        verify(outMock, times(4)).println(anyString());
        verify(outMock).println("Enviando email fake");
        verify(outMock).println("Destinatario: " + destinatario);
        verify(outMock).println("Assunto: " + assunto);
        verify(outMock).println("Mensagem: " + mensagem);
    }

}
