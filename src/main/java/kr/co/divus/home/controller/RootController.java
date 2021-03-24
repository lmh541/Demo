package kr.co.divus.home.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.regex.Pattern;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.divus.home.service.AdminService;
import kr.co.divus.home.service.MailService;
import kr.co.divus.home.vo.AdminVO;
import kr.co.divus.home.vo.MailVO;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

@Controller
public class RootController {

   @Autowired
   private MailService mailService;

   @Autowired
   private AdminService adminService;

   @Autowired
   private PasswordEncoder passwordEncoder;

   @RequestMapping("/")
   public String index() {
      return "index";
   }

   @GetMapping("/login")
   public String loginPage() {
      return "/login";
   }
   

   @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
   public @ResponseBody String login(HttpServletResponse res, HttpServletRequest req,
         @RequestBody JSONObject jsonObject) throws Exception {
      String idInfo = jsonObject.get("id").toString();
      String pwInfo = jsonObject.get("pw").toString();

      try {
         AdminVO adminVO = adminService.getAdminOne(idInfo);
         boolean isPasswordMatch = passwordEncoder.matches(pwInfo, adminVO.getPw());

         if (adminVO.getId().equals(idInfo) && isPasswordMatch == true) {
            HttpSession session = req.getSession(true);
            session.setAttribute("menu_func", Integer.toString(adminVO.getMenu_func()));
            session.setAttribute("user_idx", Integer.toString(adminVO.getIdx()));
            // session.setAttribute("page", text);
            session.setMaxInactiveInterval(3600);

            // String page = (String)session.getAttribute("page");/

            if (session.getAttribute("page") == null) {
               return "/";
            } else if(session.getAttribute("page").equals("DamageDetection")){
               return "/DamageDetection";
            } else if(session.getAttribute("page").equals("textRecognition")){
               return "/textRecognition";
            } else if(session.getAttribute("page").equals("vin")){
               return "/vin";
            } else if(session.getAttribute("page").equals("Evaluation")){
               return "/Evaluation";
            } else if(session.getAttribute("page").equals("CarSegment")){
               return "/CarSegment";
            } else if(session.getAttribute("page").equals("CarPartSegment")){
               return "/CarPartSegment";
            } else {
               return "/ANPR";
            }
         }else {
            return "login";
         }
      } catch (NullPointerException e) {
         return "login";
      }
   }

   @GetMapping("/logout")
   public String logout(HttpServletRequest req) {
      HttpSession session = req.getSession(true);
      session.invalidate();
      return "redirect:/";
   }

   @RequestMapping("/DamageDetection")
   public String damageDetection(HttpServletRequest req, RedirectAttributes rttr, Model model) {
      String text = "DamageDetection";
      int session = user_session(req, rttr, text);

      if(session == -1){
         return "login";
      }else{
         return "DamageDetection";
      }
   }

   @RequestMapping("/textRecognition")
   public String textRec(HttpServletRequest req, RedirectAttributes rttr, Model model) {
      String text = "textRecognition";
      int session = user_session(req, rttr, text);

      if(session == -1){
         return "login";
      }else{
         return "textRec";
      }
   }

   @RequestMapping("/vin")
   public String vin(HttpServletRequest req, RedirectAttributes rttr, Model model) {
      String text = "vin";
      int session = user_session(req, rttr, text);

      if(session == -1){
         return "login";
      }else{
         return "vin";
      }
   }

   @RequestMapping("/Evaluation")
   public String test(HttpServletRequest req, RedirectAttributes rttr) {
      String text = "Evaluation";
      int session = user_session(req, rttr, text);

      if(session == -1){
         return "login";
      }else{
         return "Evaluation";
      }
   }

   @RequestMapping("/CarSegment")
   public String seg(HttpServletRequest req, RedirectAttributes rttr) {
      String text = "CarSegment";
      int session = user_session(req, rttr, text);

      if(session == -1){
         return "login";
      }else{
         return "CarSegment";
      }
   }

   @RequestMapping("/CarPartSegment")
   public String partSeg(HttpServletRequest req, RedirectAttributes rttr) {
      String text = "CarPartSegment";
      int session = user_session(req, rttr, text);

      if(session == -1){
         return "login";
      }else{
         return "CarPartSegment";
      }
   }

   @RequestMapping("/ANPR")
   public String anpr(HttpServletRequest req, RedirectAttributes rttr) {
      String text = "ANPR";
      int session = user_session(req, rttr, text);

      if(session == -1){
         return "login";
      }else{
         return "ANPR";
      }
   }

   @RequestMapping("/contact")
   public String contectPage() {
      return "contact";
   }

   @PostMapping("/contact")
   public void sendMail(MailVO mailVO) {
      mailService.mailSender(mailVO);
   }

   private Boolean checkSession(String sessionData, String sessionFunc) {

      Boolean res = false;
      AdminVO adminVO = new AdminVO();

      if (sessionData != null) {
         int func = Integer.parseInt(sessionFunc);
         if ((func & 128) == 128) {

            adminVO.setIdx(Integer.parseInt(sessionData));
            adminVO.setMenu_func(func);
            AdminVO resVO = adminService.selectMenu(adminVO);

            if ((sessionData == null || sessionData.isEmpty() == true) || (sessionFunc == null)
                  || sessionFunc.isEmpty() == true) {
               return res;
            }
            if (resVO != null) {
               res = sessionData.equals(Integer.toString(resVO.getIdx()));
            }
         }
         return res;
      }
      return res;
   }

   public String getToken(String id, String pw) {
      String url = "http://divus.iptime.org:4201/peoplecar/dbtest/usercheck";

      HashMap<String, Object> hash = new HashMap<String, Object>();
      hash.put("userid", id);
      hash.put("password", pw);

      JsonNode returnNode = null;

      JSONObject jsonObject = new JSONObject();
      jsonObject.putAll(hash);

      final HttpClient client = HttpClientBuilder.create().build();
      final HttpPost post = new HttpPost(url);
      try {
         // 전송할 post에 Entity 장착
         post.setEntity(new StringEntity(jsonObject.toString()));
         // Data 전송
         final HttpResponse response = client.execute(post);

         HttpEntity entity = response.getEntity();
         ObjectMapper mapper = new ObjectMapper();
         returnNode = mapper.readTree(response.getEntity().getContent());

      } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
      } catch (ClientProtocolException e) {
         e.printStackTrace();
      } catch (IOException e) {
         // res.setStatus(500);
         e.printStackTrace();
      }
      String res = returnNode.findValue("access_token").toString();
      res = Pattern.compile("\"").matcher(res).replaceAll("");
      return res;
   }

   @GetMapping("/admin")
   public String adminRoot() {
      return "redirect:/admin/Home";
   }

   // Session 유무 검사 : 존재하면 1, 존재하지 않으면 -1
   private int user_session(HttpServletRequest req, RedirectAttributes rttr, String page){
      HttpSession session = req.getSession();

      if (checkSession((String) session.getAttribute("user_idx"),
            (String) session.getAttribute("menu_func")) == false) {
         // session.invalidate();
         session.setAttribute("page", page);

         return -1;
      }

      return 1;
   }

}