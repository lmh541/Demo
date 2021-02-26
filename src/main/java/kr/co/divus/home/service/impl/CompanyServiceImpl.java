package kr.co.divus.home.service.impl;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.divus.home.dao.CompanyDao;
import kr.co.divus.home.service.CompanyService;
import kr.co.divus.home.vo.CompanyVO;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyDao companyDao;

    @Override
    public List<Object> getCompanyAll() {
        List<Object> list = companyDao.selectCompanyAll();
        return list;
    }

    @Override
    public void insertAdmin(CompanyVO companyVO) {
        this.companyDao.create(companyVO);
    }

    @Override
    public List<CompanyVO> getCompany() {
        List<CompanyVO> list = companyDao.selectCompany();
        return list;
    }

    @Override
    public List<Object> selectCompanyCeo() {
        return companyDao.selectCompanyCeo();
    }

    @Override
    public void deleteCompany(CompanyVO companyVO) {
        this.companyDao.compDelete(companyVO);
    }

    @Override
    public void updateInfo(CompanyVO companyVO) {
        this.companyDao.updateInfo(companyVO);
    }

    @Override
    public List<Object> updateSelect(int idx) {
        return this.companyDao.updateSelect(idx);
    }

    @Override
    public List<Object> userSearch(JSONObject jsonObject){
        return this.companyDao.userSearch(jsonObject);
    }

    @Override
    public List<Object> companySearch(JSONObject jsonObject){
        return this.companyDao.companySearch(jsonObject);
    }

}
