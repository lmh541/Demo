package kr.co.divus.home.service;

import java.util.List;

import org.json.simple.JSONObject;

public interface DoctorCarService {

    //paging
    public List<Object> docCarListPaging(JSONObject jsonObject);
    
}
