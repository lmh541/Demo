package kr.co.divus.home.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.divus.home.dao.DetectionDao;
import kr.co.divus.home.service.DetectionService;
import kr.co.divus.home.util.PhotoUtil;
import kr.co.divus.home.vo.DetectionVO;

@Service
public class DetectionServiceImpl implements DetectionService{
    
    @Autowired
    private PhotoUtil photoUtil;

    @Autowired
    private DetectionDao detectionDao;


    @Override
    public List<DetectionVO> makeAiPhoto(int user_info_idx, JsonNode json) throws IOException {
        // TODO Auto-generated method stub
        List<DetectionVO> res = new ArrayList<DetectionVO>();

        try {
            JsonNode request = json.get("request");
            JsonNode requestFilename = request.get("filename");
            JsonNode requestImage = request.get("image");
            JsonNode response = json.get("response");
            JsonNode results = response.get("results");
            
            for (int i=0; i<requestFilename.size(); i++) {
                String filename = requestFilename.get(i).toString();
                String image = requestImage.get(i).toString();
                filename = photoUtil.base64tofile(image, filename);
                String resultFilename = photoUtil.drawRactAi(filename, results.get(i).get("boxs"));
                String aiVersion = "";
                if (response.get("ai_version") != null) {
                    aiVersion = response.get("ai_version").toString();
                    aiVersion = aiVersion.replace("\"", "");
                }
                    
                int scratch = 0;
                int dent = 0;
                int crack = 0;

                JsonNode boxs = results.get(i).get("boxs");

                for (int j=0; j<boxs.size(); j++) {
                    JsonNode box = boxs.get(j);
                    String type = box.get(4).toString();
                    type = type.replace("\"", "");
                    if (type.equals("scratch") == true) {
                        scratch++;
                    } else if (type.equals("dent") == true){
                        dent++;
                    } else if (type.equals("crack") == true){
                        crack++;
                    }
                }
                DetectionVO vo = new DetectionVO();
                vo.setUser_info_idx(user_info_idx);
                vo.setPath("/photo/");
                vo.setSrc_photo(filename);
                vo.setAi_photo(resultFilename);
                vo.setAi_version(aiVersion);
                vo.setAi_scratch(scratch);
                vo.setAi_dent(dent);
                vo.setAi_crack(crack);
                vo.setAi_json(results.get(i).toString());
                vo.set_delete(false);
                res.add(vo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    @Override
    public List<DetectionVO> makeAnnotationPhoto(JsonNode json) throws IOException {

        List<DetectionVO> res = new ArrayList<DetectionVO>();
        for (JsonNode el : json) {
            String srcPhoto = el.get("filename").toString();
            srcPhoto = srcPhoto.replace("\"", "");
            DetectionVO vo = selectOne(srcPhoto);
            if (vo == null){
                continue;
            }

            String resultFilename = photoUtil.drawRactAnnotation(vo.getSrc_photo(), el.get("regions"));

            int scratch = 0;
            int dent = 0;
            int crack = 0;

            JsonNode regions = el.get("regions");

            for (int j=0; j<regions.size(); j++) {
                String type = regions.get(j).get("region_attributes").get("damage").toString();
                type = type.replace("\"", "");
                if (type.equals("scratch") == true) {
                    scratch++;
                } else if (type.equals("dent") == true){
                    dent++;
                } else if (type.equals("crack") == true){
                    crack++;
                }
            }

            vo.setAn_photo(resultFilename);
            vo.setAn_json(el.toString());
            vo.setAn_scratch(scratch);
            vo.setAn_dent(dent);
            vo.setAn_crack(crack);
            res.add(vo);
        }
        return res;
    }

    @Override
    public void insert(DetectionVO vo) {
        detectionDao.insert(vo);
    }

    @Override
    public DetectionVO selectOne(int idx) {
        // TODO Auto-generated method stub
        return detectionDao.selectOne(idx);
    }

    @Override
    public DetectionVO selectOne(String srcPhoto) {
        // TODO Auto-generated method stub
        return detectionDao.selectOne(srcPhoto);
    }


    // 검색 Paging
    public PageInfo<Object> detecSearchPaging(JSONObject jsonObject, int pageNo, int pageSize){
        PageHelper.startPage(pageNo, pageSize);
        List<Object> list = detectionDao.selectSearchDetec(jsonObject);
        PageInfo<Object> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public void updateResultAno(DetectionVO vo){
        detectionDao.updateResultAno(vo);
    }

    

}
