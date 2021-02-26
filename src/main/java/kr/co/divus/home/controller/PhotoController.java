// package kr.co.divus.home.controller;

// import java.util.List;

// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.databind.SerializationFeature;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
// import com.github.pagehelper.PageInfo;

// import org.json.simple.JSONObject;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.ResponseBody;

// import kr.co.divus.home.service.PhotoService;



// @Controller
// @RequestMapping("/admin")
// public class PhotoController {

//     @Autowired
//     private PhotoService photoService;

//     @GetMapping("/adminAno")
// 	public String adminTest() {
// 		return "adminService/adminAno";
// 	}

//     // @GetMapping("/ano")
//     // @ResponseBody
//     // public PageInfo<Object> ano(@RequestParam(value = "pageNo", defaultValue = "0")int pageNo,
//     //                     @RequestParam(value = "pageSize", defaultValue = "10")int pageSize){
        
//     //     PageInfo<Object> pageInfo = photoService.photoListPaging(pageNo, pageSize);
//     //     return pageInfo;
//     // }

//     @GetMapping("/ano")
//     @ResponseBody
//     public JsonNode ano(){
//         JsonNode returnNode = null;
//         ObjectMapper mapper = new ObjectMapper();
//         List<Object> photoList= this.photoService.selectAllPhoto();
        
//         mapper.registerModule(new JavaTimeModule());
//         mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

//         returnNode = mapper.convertValue(photoList, JsonNode.class);

//         return returnNode;
//     }
//     @PostMapping("/ano")
//     @ResponseBody
//     public JsonNode ano(@RequestBody JSONObject jsonObject){
        
//         JsonNode returnNode = null;
//         ObjectMapper mapper = new ObjectMapper();

//         mapper.registerModule(new JavaTimeModule());
//         mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//         System.out.println(jsonObject.toString());
//         List<Object> searchList = this.photoService.selectSearchPhoto(jsonObject);
//         returnNode = mapper.convertValue(searchList, JsonNode.class);
//         System.out.println("list : "+searchList.toString());
//         System.out.println("node : " + returnNode.toString());

//         return returnNode;
//     }

//     @PostMapping("result_ano")
//     public void resultAno(@RequestBody JSONObject jsonObject){
//         this.photoService.updateResultAno(jsonObject);
//     }

    
// }
