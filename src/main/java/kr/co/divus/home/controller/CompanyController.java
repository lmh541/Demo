package kr.co.divus.home.controller;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.divus.home.service.CompanyService;
import kr.co.divus.home.vo.CompanyVO;

@Controller
@RequestMapping("/company")
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@PostMapping("/ClientsManagement")
	@ResponseBody
	public List<Object> adminClientsManagement(@RequestBody JSONObject jsonObject){
		if(jsonObject.get("selectedOpt").equals("company")){
			List<Object> list = this.companyService.companySearch(jsonObject);

			return list;
		}else{
			List<Object> list = this.companyService.userSearch(jsonObject);
			return list;
		}
	}

	@GetMapping("/BusinessRegistration")
	public String adminBusinessRegistration(Model model) {
		List<CompanyVO> list = this.companyService.getCompany();

		model.addAttribute("companyList", list);
		return "admin/adminPages/businessRegistration";
	}

	@PostMapping("/BusinessRegistration")
	public String adminBusinessRegistration(@RequestBody CompanyVO companyVO) {
		// -- 업체 등록 --
		this.companyService.insertAdmin(companyVO);
		return "admin/adminPages/businessRegistration";
	}

	@PostMapping("/BusinessDelete")
	@ResponseBody
	public String adminBusinessDelete(@RequestBody List<CompanyVO> companyDel) {
		for (int i = 0; i < companyDel.size(); i++) {
			this.companyService.deleteCompany(companyDel.get(i));
		}
		return "admin/adminPages/businessRegistration";
	}

	@PostMapping("/updateInfo")
	public String updateInfo(@RequestBody CompanyVO companyVO) {

		this.companyService.updateInfo(companyVO);
		return "admin/adminPages/businessRegistration";
	}

	@PostMapping("/updateSelectCompany")
	@ResponseBody
	public JsonNode updateSelectCompany(@RequestBody JSONObject jsonObject) {
		JsonNode returnNode = null;
		ObjectMapper mapper = new ObjectMapper();

		int idx = jsonObject.get("selectedTr").hashCode();

		List<Object> list = this.companyService.updateSelect(idx);

		returnNode = mapper.convertValue(list, JsonNode.class);



		return returnNode;
	}
}
