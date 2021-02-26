package kr.co.divus.home.controller;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.divus.home.service.DetectionService;
import kr.co.divus.home.vo.DetectionVO;

@Controller
@RequestMapping("/admin")
public class DetectionController {

    @Autowired
    private DetectionService detectionService;

    @GetMapping("/annotation")
	public String adminTest() {
		return "/admin/adminServicePages/annotation";
	}

    // @GetMapping("/ano")
    // @ResponseBody
    // public PageInfo<Object> anoGet(@RequestBody JSONObject jsonObject){
    // int pageNo = jsonObject.get("pageNo").hashCode();
    // int pageSize = 10;
    // PageInfo<Object> list = this.detectionService.detecSearchPaging(jsonObject, pageNo, pageSize);
    // return list;
    // }

    // 검색 paging
    @PostMapping("/ano")
    @ResponseBody
    public PageInfo<Object> anoPost(@RequestBody JSONObject jsonObject) {
        int pageNo = jsonObject.get("pageNo").hashCode();
        int pageSize = 10;

        PageInfo<Object> list = this.detectionService.detecSearchPaging(jsonObject, pageNo, pageSize);
        System.out.println("List :: " + list.toString());
        System.out.println("Detection :: " + this.detectionService.detecSearchPaging(jsonObject, pageNo, pageSize));
        
        return list;
    }

    @PostMapping("/updateAnnotation")
    @ResponseBody
    public JsonNode updateAnnotation(@RequestBody JsonNode json) throws IOException {
        JsonNode returnNode = null;

        List<DetectionVO> listVo = detectionService.makeAnnotationPhoto(json);
		for (int i=0; i< listVo.size(); i++) {
			detectionService.updateResultAno(listVo.get(i));
		}

        ObjectMapper mapper = new ObjectMapper();
        returnNode = mapper.convertValue(listVo, JsonNode.class);

        return returnNode;
    }

    @PostMapping("/")
    @ResponseBody
    public JsonNode changeAnnotation(@RequestBody JsonNode json){
        JsonNode returnNode = null;

        return returnNode;
    }
}
