package kr.co.divus.home.dao;

import java.util.List;

import org.json.simple.JSONObject;

import kr.co.divus.home.vo.CompanyVO;

public interface CompanyDao {
    List<Object> selectCompanyAll();

    List<CompanyVO> selectCompany();

    List<Object> selectCompanyCeo();

    void create(CompanyVO companyVO);

    void compDelete(CompanyVO companyVO);

    void updateInfo(CompanyVO companyVO);

    List<Object> updateSelect(int idx);

    List<Object> userSearch(JSONObject jsonObject);
    List<Object> companySearch(JSONObject jsonObject);

}
