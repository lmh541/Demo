package kr.co.divus.home.dao;

import java.util.List;

import org.json.simple.JSONObject;

import kr.co.divus.home.vo.AdminVO;

public interface AdminDao {
    AdminVO selectAdmin(String id);

    List<Object> selectAdminAll();

    int insertUser(JSONObject jsonObject);

    int updateUser(JSONObject jsonObject);

    List<Object> updateInfo(int idx);

    void userDelete(AdminVO adminVO);

    AdminVO selectMenu(AdminVO adminVO);
}
