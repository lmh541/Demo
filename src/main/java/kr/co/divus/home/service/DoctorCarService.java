package kr.co.divus.home.service;

import java.util.List;

import net.sf.json.JSONObject;

public interface DoctorCarService {

    //paging
    public List<Object> docCarListPaging(JSONObject jsonObject);
    
}
