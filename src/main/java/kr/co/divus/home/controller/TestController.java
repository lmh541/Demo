package kr.co.divus.home.controller;

import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.divus.home.service.DoctorCarService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import net.sf.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private DoctorCarService doctorCarService;

    // @ApiOperation(value = "Test Detail Page List Post", tags = "test")
    // @PostMapping("/echo/post")
    // public PageInfo<Object> docterCar(@RequestBody JSONObject jsonObject)throws ParseException {
    //     int pageNo = jsonObject.get("pageNo").hashCode();
    //     int pageSize = 10;

    //     PageInfo<Object> list = this.doctorCarService.docCarListPaging(jsonObject, pageNo, pageSize);
    //     System.out.println("List :: " + list.toString());
    //     System.out.println("Detection :: " + this.doctorCarService.docCarListPaging(jsonObject, pageNo, pageSize));
    //     return list;
    // }
    
    @ApiOperation(value = "Test Detail Page List Post", tags = "test")
    @PostMapping("/echo/post")
    public JsonNode docPost(@RequestBody JSONObject jsonObject){
        JsonNode returnNode = null;
        ObjectMapper mapper = new ObjectMapper();
        List<Object> docList = this.doctorCarService.docCarListPaging(jsonObject);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(format);

        returnNode = mapper.convertValue(docList, JsonNode.class);

        return returnNode;
    }

    @ApiOperation(value = "Test Excel", tags = "test")
    @PostMapping("/echo/excel")
    public Workbook excelDown(HttpServletResponse response, @RequestBody JsonNode jsonNode) throws Exception{
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Doctor car");

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
        cell.setCellValue("Differnt");

        for(int i=0; i<jsonNode.size(); i++){
            String jsonStr = mapper.writeValueAsString(jsonNode.get(i));
            Object obj = jsonParser.parse(jsonStr);
            JSONObject jsonObject = (JSONObject)obj;

            row = sheet.createRow(rowNo++);
            for(int j=0; j < 11; j++){

                cell = row.createCell(j);
                cell.setCellStyle(bodyStyle);
                if(j==0){
                    cell.setCellValue(jsonObject.get("reg_date").toString());
                    sheet.autoSizeColumn(j);
                }else if(j==1){
                    cell.setCellValue(jsonObject.get("company").toString());
                    sheet.autoSizeColumn(j);
                }else if(j==2){
                    cell.setCellValue(jsonObject.get("src_photo").toString());
                    sheet.autoSizeColumn(j);
                }else if(j==3){
                    cell.setCellValue(jsonObject.get("ai_photo").toString());
                    sheet.autoSizeColumn(j);
                }else if(j==4){
                    cell.setCellValue(jsonObject.get("ai_photo").toString());
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
                    cell.setCellValue(jsonObject.get("an_scratch").toString());
                    sheet.autoSizeColumn(j);
                }else if(j==9){
                    cell.setCellValue(jsonObject.get("an_dent").toString());
                    sheet.autoSizeColumn(j);
                }else if(j==10){
                    cell.setCellValue(jsonObject.get("an_crack").toString());
                    sheet.autoSizeColumn(j);
                }

                // cell = row.createCell(11);
                // cell.setCellStyle(bodyStyle);
                // 
                // sheet.autoSizeColumn(11);
            }
        }
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename="+ getCurrentTimeStamp() + ".xls");

        wb.write(response.getOutputStream());
        wb.close();

        return wb;
      }

      public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
}
