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

   // JsonNode returnNode = null;

   // HttpSession session = req.getSession(true);
   // UseVO useVO = new UseVO();
   // useVO.setType_func(128);
   // useVO.setUser_info_idx(Integer.parseInt((String)session.getAttribute("user_idx")));
   // UseVO resVO = useService.selectUse(useVO);
   // System.out.println("menu_func:
   // "+session.getAttribute("menu_func")+"\nuser_idx:
   // "+session.getAttribute("user_idx")+"\nresVO.idx: "+resVO.getIdx());
   // if (resVO == null) {
   // res.setStatus(500);
   // return returnNode;
   // }
   // res.setStatus(200);
   // try {
   // post.setEntity(new StringEntity(json));
   // final HttpResponse response = client.execute(post);
   // // JSON 형태 반환값 처리
   // ObjectMapper mapper = new ObjectMapper();
   // returnNode = mapper.readTree(response.getEntity().getContent());
   // } catch (UnsupportedEncodingException e) {
   // res.setStatus(500);
   // e.printStackTrace();
   // } catch (ClientProtocolException e) {
   // res.setStatus(500);
   // e.printStackTrace();
   // } catch (IOException e) {
   // res.setStatus(500);
   // e.printStackTrace();
   // } finally {
   // // clear resources
   // }

   // ObjectMapper mapper = new ObjectMapper();
   // JsonNode node = mapper.readTree("{ \"request\" : " + json + ", \"response\" :
   // " + returnNode.toString() + "}");
   // List<PhotoVO> listVO = photoService.makePhoto(resVO.getIdx(), node);
   // for (PhotoVO photoVO : listVO) {
   // photoService.savePhoto(photoVO);
   // }
   // return returnNode;

   @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
   public @ResponseBody String login(HttpServletResponse res, HttpServletRequest req,
         @RequestBody JSONObject jsonObject) throws Exception {
      String idInfo = jsonObject.get("id").toString();
      String pwInfo = jsonObject.get("pw").toString();

      try {
         AdminVO adminVO = adminService.getAdminOne(idInfo);
         boolean isPasswordMatch = passwordEncoder.matches(pwInfo, adminVO.getPw());

         if (adminVO.getId().equals(idInfo) && isPasswordMatch == true) {

            res.addHeader("Authorization", getToken(idInfo, pwInfo));

            HttpSession session = req.getSession(true);
            session.setAttribute("menu_func", Integer.toString(adminVO.getMenu_func()));
            session.setAttribute("user_idx", Integer.toString(adminVO.getIdx()));
            session.setMaxInactiveInterval(60);

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

   @RequestMapping("/DemoType1")
   public String fileDemo(HttpServletRequest req) {
      HttpSession session = req.getSession();
      if (checkSession((String) session.getAttribute("user_idx"),
            (String) session.getAttribute("menu_func")) == false) {
         session.setAttribute("page", "DemoType1");
         return "redirect:login";
      }
      return "test2";
   }

   @RequestMapping("/DemoType2")
   public String test(HttpServletRequest req, RedirectAttributes rttr) {
      HttpSession session = req.getSession();
      if (checkSession((String) session.getAttribute("user_idx"),
            (String) session.getAttribute("menu_func")) == false) {
         session.setAttribute("page", "DemoType2");
         return "redirect:login";
      }
      return "demoTest4";
   }

   @GetMapping("/textRecognition")
   public String textRec() {
      return "textRec";
   }

   @RequestMapping("/DemoTypeSeg")
   public String seg(HttpServletRequest req, RedirectAttributes rttr) {
      HttpSession session = req.getSession();
      if (checkSession((String) session.getAttribute("user_idx"),
            (String) session.getAttribute("menu_func")) == false) {
         session.setAttribute("page", "DemoTypeSeg");
         return "redirect:login";
      }
      return "demoTestSeg";
   }

   @RequestMapping("/DemoTypePartSeg")
   public String partSeg(HttpServletRequest req, RedirectAttributes rttr) {
      HttpSession session = req.getSession();
      if (checkSession((String) session.getAttribute("user_idx"),
            (String) session.getAttribute("menu_func")) == false) {
         session.setAttribute("page", "DemoTypePartSeg");
         return "redirect:login";
      }

      return "demoTestPartSeg";
   }

   @RequestMapping("/DemoTypeAnpr")
   public String anpr() {
      return "demoTestanpr";
   }

   @RequestMapping("/DemoDamageDetection")
   public String damageDetection(HttpServletRequest req, RedirectAttributes rttr, Model model) {

      // return "demoTest2";
      HttpSession session = req.getSession();
      if (checkSession((String) session.getAttribute("user_idx"),
            (String) session.getAttribute("menu_func")) == false) {
         // session.invalidate();
         session.setAttribute("page", "DemoDamageDetection");

         return "login";
      }
      return "demoTest2";
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

}