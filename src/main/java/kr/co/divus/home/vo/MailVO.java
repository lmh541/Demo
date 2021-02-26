package kr.co.divus.home.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailVO{
    private String address;
    private String title;
    private String message; 
    private String sendaddress;
}