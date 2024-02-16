package br.com.alura.adopet.api.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class EmailServiceProducaoTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailServiceProducao emailService;

    @Test
    public void testEnviarEmail() {
        String to = "teste@email.com";
        String subject = "Assunto do Teste";
        String message = "Mensagem de teste";

        emailService.enviarEmail(to, subject, message);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("adopet@email.com.br");
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);

        verify(emailSender, times(1)).send(email);
    }
}

