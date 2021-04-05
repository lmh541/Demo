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
            session.setMaxInactiveInterval(3600);
            

            String page = (String) session.getAttribute("menu_func");

            if (page == null || page.isEmpty() == true) {
               return "/";
            } else {
               return "/";
            }
         } else {
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
      int ret = user_func(req);

      if(ret == -1){
         return "login";
      }

      if((ret & 128) == 128){
         return "DamageDetection";
      }else{
         return "login";
      }
   }

   @RequestMapping("/textRecognition")
   public String textRec(HttpServletRequest req, RedirectAttributes rttr, Model model) {
      int ret = user_func(req);

      if(ret == -1){
         return "login";
      }

      if((ret & 64) == 64){
         return "textRec";
      }else{
         return "login";
      }
   }

   @RequestMapping("/vin")
   public String vin(HttpServletRequest req, RedirectAttributes rttr, Model model) {
      int ret = user_func(req);

      if(ret == -1){
         return "login";
      }

      if((ret & 32) == 32){
         return "vin";
      }else{
         return "login";
      }
   }

   @RequestMapping("/ANPR")
   public String anpr(HttpServletRequest req, RedirectAttributes rttr) {
      int ret = user_func(req);

      if(ret == -1){
         return "login";
      }

      if((ret & 16) == 16){
         return "ANPR";
      }else{
         return "login";
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

      if(sessionData != null && sessionFunc != null){
         int func = Integer.parseInt(sessionFunc);
         adminVO.setIdx(Integer.parseInt(sessionData));
         adminVO.setMenu_func(func);

         AdminVO resVO = adminService.selectMenu(adminVO);

         if(resVO != null){
            res = sessionData.equals(Integer.toString(resVO.getIdx()));
         }
         return res;
      }
      return res;
   }

   private int user_func(HttpServletRequest req){
      HttpSession session = req.getSession();

      if(checkSession((String) session.getAttribute("user_idx"), (String)session.getAttribute("menu_func")) ==false){
         return -1;
      }

      String menu_func = (String) session.getAttribute("menu_func");
      int func = Integer.parseInt(menu_func);
      
      return func;
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


}