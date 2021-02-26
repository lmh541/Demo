package kr.co.divus.home.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.divus.home.dao.DoctorCarDao;

@Repository
public class DoctorCarDaoImpl implements DoctorCarDao {
    @Autowired
    private SqlSession sqlSession;

    @Override
    public List<Object> selectSearchDoc(JSONObject jsonObject){
        return this.sqlSession.selectList("selectSearchDoc", jsonObject);
    }
}
