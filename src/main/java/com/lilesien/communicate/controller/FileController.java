package com.lilesien.communicate.controller;

import com.lilesien.communicate.dto.FileDTO;
import com.lilesien.communicate.provider.ALiCloudProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;

@Controller
public class FileController {

    @Autowired
    private ALiCloudProvider cloudProvider;

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO imageUpload(HttpServletRequest request){
        MultipartRequest multipartRequest = (MultipartRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        String returnUrl = cloudProvider.upload(file);
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl(returnUrl);
        return fileDTO;
    }
}
