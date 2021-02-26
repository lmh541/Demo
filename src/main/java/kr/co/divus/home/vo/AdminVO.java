package kr.co.divus.home.vo;

import java.time.LocalDateTime;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Alias("adminVO")
public class AdminVO {
    private int idx;
    private int company_info_idx;
    private String name;
    private String id;
    private String pw;
    private String tel;
    private int user_perm;
    private int menu_func;
    private LocalDateTime reg_date;
    private LocalDateTime mod_date;
    private byte is_delete;
    private String tokenValue;
}
