package kr.co.divus.home.dao;

import java.util.List;

import org.json.simple.JSONObject;

public interface DoctorCarDao {
    List<Object> selectSearchDoc(JSONObject jsonObject);
}
