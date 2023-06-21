package med.helper.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("EmailService")
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;
    private final String adminText = "Добро пожаловать. \n";
    private final String userText = "Добро пожаловать в наше сообщество! Мы искренне рады приветствовать вас как нашего нового пользователя и хотим уверить вас, что всегда готовы поддержать ваши усилия по улучшению качества жизни." +
            " \n"+
            "Важно помнить, что, несмотря на всю полезность и доступность информации в нашем приложении, оно не заменяет профессионального медицинского мнения. Назначение лекарств, их дозировка и режим приема - это вопросы, которые должен решать только ваш лечащий врач.\n" +
            "\n" +
            "Если вы случайно пропустили прием лекарства, не пытайтесь самостоятельно восполнять пропуск или изменять режим приема. Обязательно обратитесь к вашему лечащему врачу для консультации. Он сможет оценить ситуацию, объяснить возможные побочные эффекты и рассказать, как с ними справиться."
            + "\n" + "Наше приложение - это удобный инструмент, помогающий вам быть в курсе своего здоровья, но мы не предоставляем медицинских услуг. Еще раз добро пожаловать и заботьтесь о своем здоровье!" + "\n" ;

    public void sendSimpleMessage(String password, String email, String authority) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply.medhelper.1@gmail.com");
        message.setTo(email);
        message.setSubject("Пароль от личного кабинета Med Helper");
        if (Objects.equals(authority, "ADMIN")) {
            message.setText(adminText + "Ваш пароль: " + password);
        } else
            message.setText(userText + "Ваш пароль: " + password);
        emailSender.send(message);
    }

}
