package kr.co.divus.home.service.impl;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.divus.home.dao.AdminDao;
import kr.co.divus.home.service.AdminService;
import kr.co.divus.home.vo.AdminVO;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminDao adminDao;

    @Override
    public AdminVO getAdminOne(String id) {
        return adminDao.selectAdmin(id);
    }

    @Override
    public List<Object> getAdminAll() {
        List<Object> list = this.adminDao.selectAdminAll();
        return list;
    }

    @Override
    public int insertUser(JSONObject jsonObject) {
        return this.adminDao.insertUser(jsonObject);
    }

    @Override
    public int updateUser(JSONObject jsonObject) {
        return this.adminDao.updateUser(jsonObject);
    }

    @Override
    public List<Object> updateInfo(int idx) {
        return this.adminDao.updateInfo(idx);
    }

    @Override
    public void userDelete(AdminVO adminVO) {
        this.adminDao.userDelete(adminVO);
    }

    @Override
    public AdminVO selectMenu(AdminVO adminVO){
        return this.adminDao.selectMenu(adminVO);
    }
}
