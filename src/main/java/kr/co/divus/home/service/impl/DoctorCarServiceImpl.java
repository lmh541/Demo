package kr.co.divus.home.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.divus.home.dao.DoctorCarDao;
import kr.co.divus.home.service.DoctorCarService;
import net.sf.json.JSONObject;


@Service
public class DoctorCarServiceImpl implements DoctorCarService{

    // @Autowired
    // private PhotoUtil photoUtil;

    @Autowired
    private DoctorCarDao doctorCarDao;

    // 검색 Paging/
    @Override
    public List<Object> docCarListPaging(JSONObject jsonObject){
        List<Object> list = doctorCarDao.selectSearchDoc(jsonObject);
        return list;
    }
    
}