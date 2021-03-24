package kr.co.divus.home.dao;

import java.util.List;
import net.sf.json.JSONObject;

public interface DoctorCarDao {
    List<Object> selectSearchDoc(JSONObject jsonObject);
}
