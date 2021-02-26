package kr.co.divus.home.service;

import java.util.List;

import org.json.simple.JSONObject;

import kr.co.divus.home.vo.AdminVO;

public interface AdminService {
    AdminVO getAdminOne(String id);
    List<Object> getAdminAll();
    AdminVO selectMenu(AdminVO adminVO);
    int insertUser(JSONObject jsonObject);
    List<Object> updateInfo(int idx);
    int updateUser(JSONObject jsonObject);
    void userDelete(AdminVO adminVO);
}