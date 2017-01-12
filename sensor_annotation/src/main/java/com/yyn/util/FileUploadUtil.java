package com.yyn.util;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
	
	public static void upload(MultipartFile file, String relativePath, String fileName, long fileSize , HttpServletRequest request) {
		try {
			if(null == file) 
				throw new Exception("上传失败,文件为空");
			if(file.getSize() > fileSize)
				throw new Exception("上传失败,文件不能超过"+(fileSize/(1024*1024))+"MB");
			if(file.getSize()>0) {
	            //文件存放路径，上传的文件放在项目的upload文件夹下  
	            String savePath = request.getSession().getServletContext().getRealPath(relativePath); 
	            //将上传的文件存放到upload下  
	            file.transferTo(new File(savePath + File.separator + fileName));
			}
		} catch (Exception e) {
    			e.printStackTrace();
    	}
	}
	
}
