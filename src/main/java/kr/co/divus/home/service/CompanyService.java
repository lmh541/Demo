package kr.co.divus.home.service;

import java.util.List;

import org.json.simple.JSONObject;

import kr.co.divus.home.vo.CompanyVO;

public interface CompanyService {
    // -- 고객 현황 페이지 : 등록한 업체 List
    List<Object> getCompanyAll();

    // -- 업체등록 페이지 : 등록한 업체 List
    List<CompanyVO> getCompany();

    // -- 등록한 업체의 name, ceo
    List<Object> selectCompanyCeo();

    void insertAdmin(CompanyVO companyVO);

    void deleteCompany(CompanyVO companyVO);

    void updateInfo(CompanyVO companyVO);

    List<Object> updateSelect(int idx);

    List<Object> userSearch(JSONObject jsonObject);
    List<Object> companySearch(JSONObject jsonObject);

}
