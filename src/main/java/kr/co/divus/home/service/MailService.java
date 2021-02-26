package kr.co.divus.home.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import kr.co.divus.home.vo.MailVO;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final String FROM_ADDRESS = "flah12@naver.com";

    public void mailSender(MailVO mailVO){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(MailService.FROM_ADDRESS);
        message.setFrom(mailVO.getAddress());
        // System.out.println("SET_FORM = " + mailVO.getAddress());
        message.setSubject(mailVO.getTitle());
        message.setText(mailVO.getMessage());

        mailSender.send(message);
    }
    
}
