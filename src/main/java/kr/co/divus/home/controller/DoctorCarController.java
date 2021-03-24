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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.divus.home.service.DoctorCarService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/admin")
public class DoctorCarController {

    @Value("${api.excel.path}")
    public String LOCAL_UPLOAD;

    @Autowired
    private DoctorCarService doctorCarService;

    private static String[] cellHeader = {"날짜","업체명","SRC", "AI", "HUMAN", "AI_scratch", "AI_crack", "AI_dent", "AN_scratch", "AN_crack", "AN_dent", "Difference"};


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

        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(format);

        int rowNo = 0;
        
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

        Row rowHeader = sheet.createRow(rowNo++);
        for(int i=0; i<cellHeader.length; i++){
            Cell cell = rowHeader.createCell(i);
            cell.setCellStyle(headStyle);
            cell.setCellValue(cellHeader[i]);
        }
        // header 생성
        
        for(int i=0; i<jsonNode.size(); i++){
            String jsonStr = mapper.writeValueAsString(jsonNode.get(i));
            jsonStr = jsonStr.replace(",", "}, {");
            jsonStr = "[" + jsonStr + "]";

            // Object -> JSON
            JSONArray arr = JSONArray.fromObject(jsonStr);
            Row row = sheet.createRow(rowNo++);
            for(int j=0; j < arr.size(); j++){
                Cell cell = row.createCell(j);
                cell.setCellStyle(bodyStyle);
                JSONObject jsonValue = arr.getJSONObject(j);
                String value = jsonValue.getString(jsonValue.keys().next().toString());
                if(j > 4 || j<2){
                    if(value != null && value != "null"){
                        cell.setCellValue(value);
                    }else{
                        cell.setCellValue("-");
                    }
                }else{
                    cell.setCellValue(value);
                    HSSFHyperlink link = wb.getCreationHelper().createHyperlink(HyperlinkType.URL);
                    link.setAddress("http://" + host + "/photo/" + value);
                    cell.setHyperlink(link);
                }
                sheet.autoSizeColumn(j);
            }
        }

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
