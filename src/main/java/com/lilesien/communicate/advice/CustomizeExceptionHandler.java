package com.lilesien.communicate.advice;

import com.lilesien.communicate.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice()
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request, Throwable ex, Model model){
        if(ex instanceof CustomizeException){
            model.addAttribute("message", ex.getMessage());
        }else {
            model.addAttribute("message", "服务器冒烟了，要不然等会再试试~");
        }
        return new ModelAndView("error");
    }

}
