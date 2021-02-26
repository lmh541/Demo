package kr.co.divus.home.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.divus.home.service.AdminService;
import kr.co.divus.home.service.CompanyService;
import kr.co.divus.home.vo.AdminVO;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private AdminService adminSerivce;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/")
	public String adminRoot(){
		return "redirect:/admin/Home";
	}

	@GetMapping("/Home")
	public String adminClientsManagement(Model model){
		List<Object> list = this.companyService.getCompanyAll();
		model.addAttribute("companyAllList", list);
		return "admin/adminPages/clientsManagement";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/admin/Home";
	}

	@GetMapping("/login")
	public String adminPage() {
		return "admin/adminPages/loginPage";
	}

	@PostMapping("/loginAction")
	public @ResponseBody String adminPage(@RequestBody JSONObject jsonObject) {
		String idInfo = jsonObject.get("id").toString();
		String pwInfo = jsonObject.get("pw").toString();
		
		try {
			AdminVO adminVO = this.adminSerivce.getAdminOne(idInfo);
			boolean isPasswordMatch = passwordEncoder.matches(pwInfo, adminVO.getPw());

			if (adminVO.getId().equals(idInfo)
					&& isPasswordMatch == true) {
				System.out.println("성공");
				return "admin/Home";
			} else if(adminVO.getId().equals(idInfo) && isPasswordMatch == false){
				System.out.println("비밀번호를 다시 입력해주세요");
				return "admin/adminPages/loginPage";
			}else{
				System.out.println("실패");
				return "admin/adminPages/loginPage";
			}
		} catch (NullPointerException e) {
			System.out.println("가입하지 않은 아이디입니다.");
			return "admin/adminPages/loginPage";
		} finally {

		}
	}

	@GetMapping("/UserRegistration")
	public String adminUserRegistration(Model model) {
		// 등록된 사용자 List
		List<Object> list = this.adminSerivce.getAdminAll();
		// List<Object> companyList = this.companyService.selectCompanyCeo();

		// model.addAttribute("companyList", companyList);
		model.addAttribute("adminList", list);
		return "admin/adminPages/userRegistration";
	}

	@PostMapping("/UserRegistration")
	public String adminUserRegistration(@RequestBody List<String> list) {

		// this.adminSerivce.insertAdmin(jsonObject);
		return "admin/adminPages/userRegistration";
	}

	// -- 사용자 등록 --
	@PostMapping("/userInsert")
	public String userInsert(@RequestBody JSONObject jsonObject) {
		String user_perm = jsonObject.get("user_perm").toString();
		String menu_func = jsonObject.get("menu_func").toString();

		int perm = 0;
		int func = 0;
	
		String[] array = menu_func.split(", ");
		
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals("dd")) {
				func += 128;
			} else if (array[i].equals("cs")) {
				func += 64;
			} else if (array[i].equals("cps")) {
				func += 32;
			} else {
				func += 16;
			}
		}
		jsonObject.put("menu_func", func);
		

		if (user_perm.equals("manager")) {
			jsonObject.put("user_perm", perm);
		} else if (user_perm.equals("user")) {
			perm = 128;
			jsonObject.put("user_perm", perm);
		} else {
			perm = 64;
			jsonObject.put("user_perm", perm);
		}
		String encodingPW = passwordEncoder.encode(jsonObject.get("pw").toString());
		jsonObject.put("pw", encodingPW);
	
		this.adminSerivce.insertUser(jsonObject);

		return "admin/adminPages/userRegistration";
	}

	@PostMapping("/CompanyCeo")
	@ResponseBody
	public List<Object> adminCompanyCeo() {
		List<Object> list = this.companyService.selectCompanyCeo();
		return list;
	}

	// -- Update 할 정보 adminUserRegistration page에 전송
	@PostMapping("/updateUserInfo")
	@ResponseBody
	public JsonNode updateInfo(@RequestBody JSONObject jsonObject) {
		JsonNode returnNode = null;
		ObjectMapper mapper = new ObjectMapper();
		int idx = jsonObject.get("selectedTr").hashCode();

		List<Object> list = this.adminSerivce.updateInfo(idx);

		returnNode = mapper.convertValue(list, JsonNode.class);

		return returnNode;
	}

	// -- User Information Update --
	@PostMapping("/updateUser")
	@ResponseBody
	public String updateUser(@RequestBody JSONObject jsonObject) {

		System.out.println("Update Object :: " + jsonObject.toString());
		String user_perm = jsonObject.get("user_perm").toString();
		String menu_func = jsonObject.get("menu_func").toString();

		int perm = 0;
		int func = 0;
		String[] array = menu_func.split(", ");

		for (int i = 0; i < array.length; i++) {
			if (array[i].equals("dd")) {
				func += 128;
			} else if (array[i].equals("cs")) {
				func += 64;
			} else if (array[i].equals("cps")) {
				func += 32;
			} else {
				func += 16;
			}
		}

		jsonObject.put("menu_func", func);

		if (user_perm.equals("manager")) {
			jsonObject.put("user_perm", perm);
		} else if (user_perm.equals("user")) {
			perm = 128;
			jsonObject.put("user_perm", perm);
		} else {
			perm = 64;
			jsonObject.put("user_perm", perm);
		}
		String encodingPW = passwordEncoder.encode(jsonObject.get("pw").toString());
		jsonObject.put("pw", encodingPW);
		this.adminSerivce.updateUser(jsonObject);

		return "admin/adminPages/userRegistration";
	}

	@PostMapping("/userDelete")
	@ResponseBody
	public String userDelete(@RequestBody List<AdminVO> userDel) {
		for (int i = 0; i < userDel.size(); i++) {
			this.adminSerivce.userDelete(userDel.get(i));
		}
		return "admin/adminPages/userRegistration";
	}

}
