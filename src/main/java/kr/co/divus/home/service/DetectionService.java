package kr.co.divus.home.service;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.pagehelper.PageInfo;

import org.json.simple.JSONObject;

import kr.co.divus.home.vo.DetectionVO;

public interface DetectionService {
    
    public List<DetectionVO> makeAiPhoto(int user_info_idx, JsonNode json) throws IOException;
    public List<DetectionVO> makeAnnotationPhoto(JsonNode json) throws IOException;

    public void insert(DetectionVO vo);
    public DetectionVO selectOne(int idx);
    public DetectionVO selectOne(String srcPhoto);
    public void updateResultAno(DetectionVO vo);

    //paging
    public PageInfo<Object> detecSearchPaging(JSONObject jsonObject, int pageNo, int pageSize);
}
