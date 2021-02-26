package kr.co.divus.home.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.divus.home.dao.CompanyDao;
import kr.co.divus.home.vo.CompanyVO;

@Repository
public class CompanyDaoImpl implements CompanyDao {
    @Autowired
    private SqlSession sqlSession;

    @Override
    public List<Object> selectCompanyAll() {
        List<Object> list = this.sqlSession.selectList("selectAllCompany");
        
        return list;
    }

    @Override
    public List<Object> selectCompanyCeo() {
        return this.sqlSession.selectList("selectCompanyCeo");
    }

    @Override
    public void create(CompanyVO companyVO) {
        this.sqlSession.insert("insertAllCompany", companyVO);
    }

    @Override
    public List<CompanyVO> selectCompany() {
        List<CompanyVO> list = this.sqlSession.selectList("selectCompany");
        return list;
    }

    @Override
    public void compDelete(CompanyVO companyVO) {
        this.sqlSession.update("deleteCompany", companyVO);
    }

    @Override
    public void updateInfo(CompanyVO companyVO) {
        this.sqlSession.update("updateInfo", companyVO);
    }

    @Override
    public List<Object> updateSelect(int idx) {
        return this.sqlSession.selectList("updateSelect", idx);
        
    }

    @Override
    public List<Object> userSearch(JSONObject jsonObject){
        return this.sqlSession.selectList("userSearch", jsonObject);
    }

    @Override
    public List<Object> companySearch(JSONObject jsonObject){
        return this.sqlSession.selectList("companySearch", jsonObject);
    }

}
