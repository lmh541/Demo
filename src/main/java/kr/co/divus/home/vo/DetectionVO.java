package kr.co.divus.home.vo;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("detectionVo")
public class DetectionVO {
    private int idx;
    private int user_info_idx;
    private String path;
    private String src_photo;
    private String ai_photo;
    private String ai_json;
    private String an_photo;
    private String an_json;
    private String ai_version;
    private int ai_scratch; 
    private int ai_dent;
    private int ai_crack;

    private Date reg_date;

    private int an_scratch; 
    private int an_dent;
    private int an_crack;

    private Date an_reg_date;

    private boolean is_delete;
    
}
