package com.lilesien.communicate.advice;

import com.lilesien.communicate.dto.ResultDTO;
import com.lilesien.communicate.exception.CustomizeErrorCode;
import com.lilesien.communicate.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice()
/**
 * 通用异常处理
 */
public class CustomizeExceptionHandler {

    /**
     * 在评论功能模块，对于局部响应的形式存在问题，
     * 这个跳转页面和返回错误信息不能同时存在，通过添加responseBody注解进行解决
     *
    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request, Throwable ex, Model model){
        if(ex instanceof CustomizeException){
            model.addAttribute("message", ex.getMessage());
        }else {
            model.addAttribute("message", "服务器冒烟了，要不然等会再试试~");
        }
        return new ModelAndView("error");
    }
    */

    @ExceptionHandler(Exception.class)
    @ResponseBody
    Object handle(HttpServletRequest request, Throwable ex, Model model, HttpServletResponse response){
        //获取contentType
        String contentType = request.getContentType();
        if("application/json".equals(contentType)){
            //返回JSON形式的数据
            ResultDTO resultDTO = null;
            if(ex instanceof CustomizeException){
                resultDTO = ResultDTO.errorOf((CustomizeException)ex);
            }else{
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            return resultDTO;
        }else {
            //返回错误页面跳转
            if (ex instanceof CustomizeException) {
                model.addAttribute("message", ex.getMessage());
            } else {
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
    }

}
