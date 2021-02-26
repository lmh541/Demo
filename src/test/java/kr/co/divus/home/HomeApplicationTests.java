package kr.co.divus.home;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.co.divus.home.dao.AdminDao;
import kr.co.divus.home.service.DetectionService;
import kr.co.divus.home.service.MailService;
import kr.co.divus.home.vo.*;

@SpringBootTest
class HomeApplicationTests {

	@Autowired
	private MailService mailService;

	@Autowired
	private AdminDao adminDao;


	@Autowired
	private DetectionService detectionService;
	
	@Test
	void test(){
		System.out.println(1);
	}

	// @Test
	// void mailTest() {
	// 	MailVO vo = new MailVO();
	// 	vo.setTitle("test");
	// 	vo.setAddress("flah12@naver.com");
	// 	vo.setMessage("너무 추워요. 참고로 test 입니다.");

	// 	mailService.mailSender(vo);
	// }
	/*

	@Test
	void adminTest() {
		AdminVO findAdmin = adminDao.selectAdmin("admin");
		System.out.println("FindAdmin : " + findAdmin);
	}

	@Test
	void photoTest() throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(new File("C:\\Logs\\Photo\\annotation.json"));

		List<DetectionVO> list = detectionService.makeAnnotationPhoto(node);
//		for (int i=0; i< list.size(); i++) {
//			detectionService.update(list.get(i));
//		}
		
	}
*/
}
