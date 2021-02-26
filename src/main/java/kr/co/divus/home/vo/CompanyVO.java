package kr.co.divus.home.vo;

import java.time.*;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Alias("companyVO")
public class CompanyVO {
    private int idx;
    private String business_num;
    private String company;
    private String ceo;
    private int count;
    private String name;
    private String tel_main;
    private String tel_sub;
    private String address;   
    private LocalDateTime reg_date;
    private LocalDateTime mod_date;
    private byte is_delete;

    
}
