package kr.co.divus.home.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;

import com.fasterxml.jackson.databind.JsonNode;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PhotoUtil {

    @Value("${api.photo.path}")
    public String LOCAL_UPLOAD;

    public String base64tofile(String encoded, String fileName) throws IOException {
        String res = "";
        fileName = fileName.replace("\"", "");
        try
        {
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String strDate = dateFormat.format(date);
    
            String sourceFileNameExtension = FilenameUtils.getExtension(fileName).toLowerCase();
            String destinationFileName = strDate + "_" + RandomStringUtils.randomAlphanumeric(32) + "." + sourceFileNameExtension;

            byte[] decoded = Base64.decodeBase64(encoded);
            File file = new File(LOCAL_UPLOAD +  destinationFileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(decoded);
            fos.close();
            res = destinationFileName;
        } catch (FileNotFoundException e) {

        }

        return res;
    }

    public String drawRactAi(String fileName, JsonNode jsonBox) throws IOException {
        String res = "";
        try
        {
            File imageFile = new File(LOCAL_UPLOAD + fileName);
            BufferedImage srcImg = ImageIO.read(imageFile);

            for (int i=0; i<jsonBox.size(); i++) {
                JsonNode box = jsonBox.get(i);

                Graphics2D graph = srcImg.createGraphics();
                String type = box.get(4).toString();
                type = type.replace("\"", "");
                if (type.equals("scratch") == true) {
                    graph.setColor(Color.RED);
                } else if (type.equals("dent") == true){
                    graph.setColor(new Color(142,69,133));
                } else if (type.equals("crack") == true){
                    graph.setColor(new Color(154,205,50));
                }
                graph.drawRect(box.get(0).asInt(), box.get(1).asInt(), box.get(2).asInt() - box.get(0).asInt(), box.get(3).asInt() - box.get(1).asInt());

                graph.setColor(new Color(120, 120, 120));
                graph.setFont(new Font("TimesRoman", Font.PLAIN, 30));
                double f = box.get(5).asDouble();
                graph.drawString(String.format("%.2f%%", f * 100), box.get(0).asInt(), box.get(1).asInt() - 15);
                graph.dispose();
            }

            String date = FilenameUtils.getBaseName(fileName).substring(0, 14);
            String random = FilenameUtils.getBaseName(fileName).substring(15);
            res = date + "_ai_" + random;

            String sourceFileNameExtension = FilenameUtils.getExtension(fileName).toLowerCase();
            res += "." + sourceFileNameExtension;
            ImageIO.write(srcImg, sourceFileNameExtension, new File(LOCAL_UPLOAD + res));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public String drawRactAnnotation(String fileName, JsonNode json) throws IOException {
        String res = "";
        try
        {
            File imageFile = new File(LOCAL_UPLOAD + fileName);
            BufferedImage srcImg = ImageIO.read(imageFile);

            for (JsonNode el : json) {
                JsonNode shape = el.get("shape_attributes");
                JsonNode region = el.get("region_attributes");

                if (shape != null && region != null) {

                    Graphics2D graph = srcImg.createGraphics();
                    String type = region.get("damage").toString();
                    type = type.replace("\"", "");
                    if (type.equals("scratch") == true) {
                        graph.setColor(Color.RED);
                    } else if (type.equals("dent") == true){
                        graph.setColor(new Color(142,69,133));
                    } else if (type.equals("crack") == true){
                        graph.setColor(new Color(154,205,50));
                    }
                    graph.drawRect(shape.get("x").asInt(), shape.get("y").asInt(), shape.get("width").asInt() , shape.get("height").asInt());
                    graph.dispose();
                }
            }

            String date = FilenameUtils.getBaseName(fileName).substring(0, 14);
            String random = FilenameUtils.getBaseName(fileName).substring(15);
            res = date + "_an_" + random;

            String sourceFileNameExtension = FilenameUtils.getExtension(fileName).toLowerCase();
            res += "." + sourceFileNameExtension;
            ImageIO.write(srcImg, sourceFileNameExtension, new File(LOCAL_UPLOAD + res));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
