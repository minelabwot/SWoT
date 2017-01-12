package com.yyn.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.yyn.model.User;

@Controller
public class FileUploadController {
	@RequestMapping("/upload.do")
	public String handleUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
		User user = (User)request.getSession().getAttribute("userInfo");
		if(null == file) 
			throw new Exception("上传失败,文件为空");
		if(file.getSize()>20000000)
			throw new Exception("上传失败,文件不能超过20M");
		if(file.getSize()>0) {
		            //文件存放路径，上传的文件放在项目的upload文件夹下  
		            String savePath = request.getSession().getServletContext().getRealPath("/WEB-INF/upload/images/userPicture"); 
					//String savePath = "F:/java/online_education/src/main/resources/upload/userPicture";
//		            String realName = file.getOriginalFilename();
		            String realName=user.getName()+".jpg";
		            System.out.println(savePath+"_"+realName);
		            //将文件信息存放到modelMap中,在成功页面读取文件信息  
//		            modelMap.put("fileName", realName);  
//		            modelMap.put("fileSize", file.getSize()/1024+"KB");  
//		            modelMap.put("fileType", file.getContentType());  
//		            modelMap.put("filePath", savePath);  
		            //将上传的文件存放到upload下  
		            file.transferTo(new File(savePath + File.separator + realName));  
		}
		return "redirect:/views/userInteractive/userInformation.jsp";
	}

	public void saveFileFromInputStream(InputStream stream,String filename) throws IOException {
		System.out.println(filename);
        FileOutputStream fs=new FileOutputStream("F:/java/sensor_annotation/src/main/webapp/WEB-INF/images/userPicture/"+ filename); 
        byte[] buffer =new byte[1024*1024]; 
        //int bytesum = 0; 
        int byteread = 0; 
        while ((byteread=stream.read(buffer))!=-1) { 
           //bytesum += byteread; 
           fs.write(buffer,0,byteread); 
           fs.flush(); 
        } 
        fs.close(); 
        stream.close();      
    }       
}

