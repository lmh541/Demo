package kr.co.divus.home.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.divus.home.dao.DetectionDao;
import kr.co.divus.home.vo.DetectionVO;

@Repository
public class DetectionDaoImpl implements DetectionDao {
    
    @Autowired
    private SqlSession sqlSession;

    @Override
    public void insert(DetectionVO vo) {
        this.sqlSession.insert("insertDetection", vo);
    }

    @Override
    public DetectionVO selectOne(int idx) {
        return this.sqlSession.selectOne("selectDetection", idx);
    }

    @Override
    public DetectionVO selectOne(String srcPhoto) {
        return this.sqlSession.selectOne("selectDetectionSrcPhoto", srcPhoto);
    }



    @Override
    public List<Object> selectSearchDetec(JSONObject jsonObject){
        return this.sqlSession.selectList("selectSearchDetec", jsonObject);
    }


    @Override
    public void updateResultAno(DetectionVO vo) {
        this.sqlSession.update("updateResultAno", vo);
    }



}
