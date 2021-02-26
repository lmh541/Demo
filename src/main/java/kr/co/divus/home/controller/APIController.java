package kr.co.divus.home.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.divus.home.service.AdminService;
import kr.co.divus.home.service.DetectionService;
import kr.co.divus.home.vo.AdminVO;
import kr.co.divus.home.vo.DetectionVO;
import lombok.extern.slf4j.Slf4j;



@Controller
@Slf4j
public class APIController {

    @Autowired
    private DetectionService detectionService;

    @Autowired
    private AdminService adminService;


    @PostMapping("/predict")
    @ResponseBody
	public JsonNode predict(@RequestBody String json, HttpServletResponse res, HttpServletRequest req)
            throws IOException {
        String url = "http://divus.iptime.org:4201/peoplecar/predict";

        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(url);
        JsonNode returnNode = null;

        String authTokenHeader = req.getHeader("Authorization");
        if (authTokenHeader.isEmpty() == true) {
            res.setStatus(500);
            return returnNode;
        }
        post.setHeader("Authorization", authTokenHeader);

        HttpSession session = req.getSession(true);
        AdminVO adminVO = new AdminVO();
        adminVO.setMenu_func(128);
        adminVO.setIdx(Integer.parseInt((String)session.getAttribute("user_idx")));
        AdminVO resVO = adminService.selectMenu(adminVO);
        if (resVO == null) {
            res.setStatus(500);
            return returnNode;
        }

        res.setStatus(200);
        try {
            post.setEntity(new StringEntity(json));
            final HttpResponse response = client.execute(post);
            // JSON 형태 반환값 처리
            ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.getEntity().getContent());
        } catch (UnsupportedEncodingException e) {
            res.setStatus(500);
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            res.setStatus(500);
            e.printStackTrace();
        } catch (IOException e) {
            res.setStatus(500);
            e.printStackTrace();
        } finally {
            // clear resources
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{ \"request\" : " + json + ", \"response\" : " + returnNode.toString() + "}");
        List<DetectionVO> listVO = detectionService.makeAiPhoto(resVO.getIdx(), node);
        for (DetectionVO detectionVO : listVO) {
            detectionService.insert(detectionVO);
        }
        return returnNode;
    }

    @PostMapping("/predict-v2")
    @ResponseBody
	public JsonNode predict_v2(@RequestBody String json, HttpServletResponse res, HttpServletRequest req)
            throws IOException {
		String url = "http://divus.iptime.org:4201/peoplecar/predict-logintest";

		final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(url);
        JsonNode returnNode = null;

        String authTokenHeader = req.getHeader("Authorization");
        if (authTokenHeader.isEmpty() == true) {
            res.setStatus(401);
            return returnNode;
        }
        post.setHeader("Authorization", authTokenHeader);

        HttpSession session = req.getSession(true);
        AdminVO adminVO = new AdminVO();
        adminVO.setMenu_func(Integer.parseInt((String)session.getAttribute("menu_func")));
        adminVO.setIdx(Integer.parseInt((String)session.getAttribute("user_idx")));
        AdminVO resVO = adminService.selectMenu(adminVO);
        System.out.println("menu_func: "+session.getAttribute("menu_func")+"\nuser_idx: "+session.getAttribute("user_idx")+"\nresVO.idx: "+resVO.getIdx());
        if (resVO == null) {
            res.setStatus(401);
            return returnNode;
        }
        res.setStatus(200);
        try {
            post.setEntity(new StringEntity(json));
            final HttpResponse response = client.execute(post);
            // JSON 형태 반환값 처리
            ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.getEntity().getContent());
        } catch (UnsupportedEncodingException e) {
            res.setStatus(500);
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            res.setStatus(500);
            e.printStackTrace();
        } catch (IOException e) {
            res.setStatus(500);
            e.printStackTrace();
        } finally {
            // clear resources
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{ \"request\" : " + json + ", \"response\" : " + returnNode.toString() + "}");
        List<DetectionVO> listVO = detectionService.makeAiPhoto(resVO.getIdx(), node);
        for (DetectionVO detectionVO : listVO) {
            detectionService.insert(detectionVO);
        }
        return returnNode;
    }

    @PostMapping("/seg")
    @ResponseBody
	public JsonNode seg(@RequestBody String json, HttpServletResponse res) {
		String url = "http://divus.iptime.org:4201/peoplecar/predict/seg";

		final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(url);
        JsonNode returnNode = null;
        res.setStatus(200);
        try {
            post.setEntity(new StringEntity(json));
            final HttpResponse response = client.execute(post);
            // JSON 형태 반환값 처리
            ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.getEntity().getContent());
        } catch (UnsupportedEncodingException e) {
            res.setStatus(500);
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            res.setStatus(500);
            e.printStackTrace();
        } catch (IOException e) {
            res.setStatus(500);
            e.printStackTrace();
        } finally {
            // clear resources
        }
        return returnNode;
    }

    @PostMapping("/partseg")
    @ResponseBody
	public JsonNode partseg(@RequestBody String json, HttpServletResponse res) {
		String url = "http://divus.iptime.org:4201/peoplecar/predict/partseg";

		final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(url);
        JsonNode returnNode = null;
        res.setStatus(200);
        try {
            post.setEntity(new StringEntity(json));
            final HttpResponse response = client.execute(post);
            // JSON 형태 반환값 처리
            ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.getEntity().getContent());
        } catch (UnsupportedEncodingException e) {
            res.setStatus(500);
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            res.setStatus(500);
            e.printStackTrace();
        } catch (IOException e) {
            res.setStatus(500);
            e.printStackTrace();
        } finally {
            // clear resources
        }
        return returnNode;
    }

    @PostMapping("/anpr")
    @ResponseBody
	public JsonNode anpr(@RequestBody String json, HttpServletResponse res) {
		String url = "http://divus.iptime.org:4201/peoplecar/predict/anpr";

		final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(url);
        JsonNode returnNode = null;
        res.setStatus(200);
        try {
            
            post.setEntity(new StringEntity(json));
            final HttpResponse response = client.execute(post);
            // JSON 형태 반환값 처리
            ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.getEntity().getContent());
        } catch (UnsupportedEncodingException e) {
            res.setStatus(500);
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            res.setStatus(500);
            e.printStackTrace();
        } catch (IOException e) {
            res.setStatus(500);
            e.printStackTrace();
        } finally {
            // clear resources
        }
        return returnNode;
    }

    @PostMapping("/admin")
    @ResponseBody
    public JsonNode login(@RequestHeader HttpHeaders headers, HttpServletResponse res){
        String url = "http://divus.iptime.org:4201/peoplecar/dbtest/usercheck";
        JSONObject jsonObject = new JSONObject();   
        JsonNode returnNode = null;     
        
        HashMap<String, Object> hash = new HashMap<String, Object>();
        hash.put("userid", headers.get("id").get(0));
        hash.put("password", headers.get("pw").get(0));

        jsonObject.putAll(hash);
        
        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(url);
        res.setStatus(200);
        try {
            // 전송할 post에 Entity 장착
            post.setEntity(new StringEntity(jsonObject.toString()));
            // Data 전송
            final HttpResponse response = client.execute(post);

            HttpEntity entity = response.getEntity();
            ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.getEntity().getContent());
            
        } catch (UnsupportedEncodingException e) {
            res.setStatus(500);
            e.printStackTrace();
        } catch(ClientProtocolException e){
            res.setStatus(500);
            e.printStackTrace();
        } catch(IOException e){
            res.setStatus(500);
            e.printStackTrace();
        }
        return returnNode.findValue("access_token");
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
}
