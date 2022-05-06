package com.lilesien.communicate.controller;

import com.lilesien.communicate.dto.FileDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileController {

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO imageUpload(){
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setMessage("测试用例");
        fileDTO.setUrl("/images/loading.gif");
        return fileDTO;
    }
}
