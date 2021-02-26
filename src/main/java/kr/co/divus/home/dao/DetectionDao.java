package kr.co.divus.home.dao;

import java.util.List;

import org.json.simple.JSONObject;

import kr.co.divus.home.vo.DetectionVO;

public interface DetectionDao {

    void insert(DetectionVO vo);

    DetectionVO selectOne(int idx);
    DetectionVO selectOne(String srcPhoto);
    List<Object> selectSearchDetec(JSONObject jsonObject);

    void updateResultAno(DetectionVO vo);

}
