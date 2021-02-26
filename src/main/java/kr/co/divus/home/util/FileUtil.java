package kr.co.divus.home.util;

import java.io.File;

public class FileUtil {
    public boolean FolderExists(String folderPath) {
        boolean res = false;
        File Folder = new File(folderPath);

        // 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
        if (!Folder.exists()) {
            try{
                Folder.mkdir(); //폴더 생성합니다.
                System.out.println("폴더가 생성되었습니다.");
                } 
                catch(Exception e){
                e.getStackTrace();
            }        
            }else {
            System.out.println("이미 폴더가 생성되어 있습니다.");
        }
        return res;
    }

    public void FolderMake(String folderPath) {
        File Folder = new File(folderPath);
        // 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
        if (!Folder.exists()) {
            try{
                Folder.mkdir(); //폴더 생성합니다.
                } 
                catch(Exception e){
                e.getStackTrace();
            }        
            }else {
        }
    }
    
}
