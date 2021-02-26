package kr.co.divus.home.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.divus.home.dao.AdminDao;
import kr.co.divus.home.vo.AdminVO;

@Repository
public class AdminDaoImpl implements AdminDao {
    @Autowired
    private SqlSession sqlSession;

    @Override
    public AdminVO selectAdmin(String id) {
        return this.sqlSession.selectOne("selectAdmin", id);
    }

    @Override
    public List<Object> selectAdminAll() {
        List<Object> list = this.sqlSession.selectList("selectAllAdmin");
        return list;
    }

    @Override
    public int insertUser(JSONObject jsonObject) {
        this.sqlSession.insert("userInsert", jsonObject);
        int idx = jsonObject.get("idx").hashCode();
        return idx;
    }
    
    // -- Update 할 data adminUserRegistration page에 전송
    @Override
    public List<Object> updateInfo(int idx) {
        return this.sqlSession.selectList("updateUserInfo", idx);
    }

    // -- User Information Update
    @Override
    public int updateUser(JSONObject jsonObject) {
        this.sqlSession.update("updateUser", jsonObject);
        int idx = jsonObject.get("idx").hashCode();
       return idx;
    }

    @Override
    public void userDelete(AdminVO adminVO) {
        this.sqlSession.update("userDelete", adminVO);
    }

    @Override
    public AdminVO selectMenu(AdminVO adminVO){
        return this.sqlSession.selectOne("selectMenu", adminVO);
    }
}
