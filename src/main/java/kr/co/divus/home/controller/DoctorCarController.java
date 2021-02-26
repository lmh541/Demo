package kr.co.divus.home.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.divus.home.service.DoctorCarService;

@Controller
@RequestMapping("/admin")
public class DoctorCarController {

    @Value("${api.excel.path}")
    public String LOCAL_UPLOAD;

    @Autowired
    private DoctorCarService doctorCarService;


    @GetMapping("/doc")
    public String docGet(){
        return "admin/adminServicePages/doctorcar";
    }

    @PostMapping("/doc")
    @ResponseBody
    public JsonNode docPost(@RequestBody JSONObject jsonObject){
        JsonNode returnNode = null;
        ObjectMapper mapper = new ObjectMapper();
        List<Object> docList = this.doctorCarService.docCarListPaging(jsonObject);

        returnNode = mapper.convertValue(docList, JsonNode.class);

        return returnNode;
    }

    @PostMapping("/excel")
    @ResponseBody
    public String excelDown(HttpServletResponse response, HttpServletRequest request,@RequestBody JsonNode jsonNode) throws Exception{

        String res = "";
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Doctor car");
        String host = request.getHeader("Host");

        Row row = null;
        Cell cell = null;
        int rowNo = 0;
        ObjectMapper mapper = new ObjectMapper();
        JSONParser jsonParser = new JSONParser();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(format);
        
        // table header style
        CellStyle headStyle = wb.createCellStyle();
        // 가는 경계선을 가집니다.
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);
        // 가운데 정렬
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        // 데이터용 경계 스타일 테두리만 지정
        CellStyle bodyStyle = wb.createCellStyle();
        bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);
        // 배경색 설정
        headStyle.setFillForegroundColor(HSSFColorPredefined.YELLOW.getIndex());
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // header 생성
        row = sheet.createRow(rowNo++);
        cell = row.createCell(0);
        cell.setCellStyle(headStyle);
        cell.setCellValue("날짜");

        cell = row.createCell(1);
        cell.setCellStyle(headStyle);
        cell.setCellValue("업체명");

        cell = row.createCell(2);
        cell.setCellStyle(headStyle);
        cell.setCellValue("SRC");

        cell = row.createCell(3);
        cell.setCellStyle(headStyle);
        cell.setCellValue("AI");

        cell = row.createCell(4);
        cell.setCellStyle(headStyle);
        cell.setCellValue("HUMAN");

        cell = row.createCell(5);
        cell.setCellStyle(headStyle);
        cell.setCellValue("AI_scratch");

        cell = row.createCell(6);
        cell.setCellStyle(headStyle);
        cell.setCellValue("AI_crack");

        cell = row.createCell(7);
        cell.setCellStyle(headStyle);
        cell.setCellValue("AI_dent");

        cell = row.createCell(8);
        cell.setCellStyle(headStyle);
        cell.setCellValue("AN_scratch");

        cell = row.createCell(9);
        cell.setCellStyle(headStyle);
        cell.setCellValue("AN_crack");

        cell = row.createCell(10);
        cell.setCellStyle(headStyle);
        cell.setCellValue("AN_dent");

        cell = row.createCell(11);
        cell.setCellStyle(headStyle);
        cell.setCellValue("Difference");

        for(int i=0; i<jsonNode.size(); i++){
            String jsonStr = mapper.writeValueAsString(jsonNode.get(i));
            Object obj = jsonParser.parse(jsonStr);
            JSONObject jsonObject = (JSONObject)obj;
            row = sheet.createRow(rowNo++);
            for(int j=0; j < 12; j++){
                cell = row.createCell(j);
                cell.setCellStyle(bodyStyle);
                if(j==0){
                    cell.setCellValue(jsonObject.get("reg_date").toString());
                    sheet.autoSizeColumn(j);
                }else if(j==1){
                    cell.setCellValue(jsonObject.get("company").toString());
                    sheet.autoSizeColumn(j);
                }else if(j==2){
                    if(jsonObject.get("src_photo").toString().equals("ori.png")){
                        cell.setCellValue(jsonObject.get("src_photo").toString());
                    }else{ 
                        HSSFHyperlink src_link = wb.getCreationHelper().createHyperlink(HyperlinkType.URL);
                        cell.setCellValue(jsonObject.get("src_photo").toString());
                        src_link.setAddress("http://" + host + "/photo/" + jsonObject.get("src_photo").toString());          
                        cell.setHyperlink(src_link);
                    }
                    sheet.autoSizeColumn(j);
                }else if(j==3){
                    if(jsonObject.get("ai_photo").toString().equals("ai.png")){
                        cell.setCellValue(jsonObject.get("ai_photo").toString());
                    }else{
                        HSSFHyperlink ai_link = wb.getCreationHelper().createHyperlink(HyperlinkType.URL);
                        cell.setCellValue(jsonObject.get("ai_photo").toString());
                        ai_link.setAddress("http://" + host + "/photo/" + jsonObject.get("ai_photo").toString());
                        cell.setHyperlink(ai_link);
                        
                    }
                    sheet.autoSizeColumn(j);
                }else if(j==4){
                    if(jsonObject.get("an_photo").toString().equals("ano.png")){
                        cell.setCellValue(jsonObject.get("an_photo").toString());
                    }else{
                        HSSFHyperlink an_link = wb.getCreationHelper().createHyperlink(HyperlinkType.URL);
                        cell.setCellValue(jsonObject.get("an_photo").toString());
                        an_link.setAddress("http://" + host + "/photo/" + jsonObject.get("an_photo").toString());
                        cell.setHyperlink(an_link);
                    }
                    sheet.autoSizeColumn(j);
                }else if(j==5){
                    cell.setCellValue(jsonObject.get("ai_scratch").toString());
                    sheet.autoSizeColumn(j);
                }else if(j==6){
                    cell.setCellValue(jsonObject.get("ai_dent").toString());
                    sheet.autoSizeColumn(j);
                }else if(j==7){
                    cell.setCellValue(jsonObject.get("ai_crack").toString());
                    sheet.autoSizeColumn(j);
                }else if(j==8){
                    if(jsonObject.get(jsonObject.get("an_scratch")) ==null){
                        cell.setCellValue("-");
                    }else{
                        cell.setCellValue(jsonObject.get("an_scratch").toString());
                    }
                    sheet.autoSizeColumn(j);
                }else if(j==9){
                    if(jsonObject.get(jsonObject.get("an_scratch")) ==null){
                        cell.setCellValue("-");
                    }else{
                        cell.setCellValue(jsonObject.get("an_dent").toString());
                    }
                    sheet.autoSizeColumn(j);
                }else if(j==10){
                    if(jsonObject.get(jsonObject.get("an_scratch")) ==null){
                        cell.setCellValue("-");
                    }else{
                        cell.setCellValue(jsonObject.get("an_crack").toString());
                    }
                    sheet.autoSizeColumn(j);
                }else if(j==11){
                    if(jsonObject.get(jsonObject.get("difference")) == null){
                        cell.setCellValue("-");
                    }else{
                        cell.setCellValue(jsonObject.get("difference").toString());
                    }
                    sheet.autoSizeColumn(j);
                }
            }
        }
//        response.setContentType("ms-vnd/excel");
//        response.setHeader("Content-Disposition", "attachment;filename="+ getCurrentTimeStamp() + ".xls");

        String fileName = getCurrentTimeStamp();

        File file = new File(LOCAL_UPLOAD +  fileName + ".xls");
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            wb.write(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(wb!=null) wb.close();
                if(fos!=null) fos.close();

                res = "/excel/" + fileName + ".xls";
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return res;
    }



    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
}
