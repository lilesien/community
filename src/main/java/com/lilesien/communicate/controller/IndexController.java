package com.lilesien.communicate.controller;

import com.lilesien.communicate.dto.PaginationDTO;
import com.lilesien.communicate.dto.QuestionDTO;
import com.lilesien.communicate.pojo.User;
import com.lilesien.communicate.mapper.UserMapper;
import com.lilesien.communicate.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@Slf4j
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @RequestMapping({"/","/index"})
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "3") Integer size,
                        @RequestParam(value = "search", required = false) String search){
//        List<QuestionDTO> list = questionService.list();//分页前的question集合
        PaginationDTO<QuestionDTO> pagination = questionService.list(search, page, size);
/*        log.info("question集合:" + list);
        model.addAttribute("questionList", list);*/
        log.info("当前页面详情:" + pagination);
        model.addAttribute("pageInfo", pagination);
        model.addAttribute("search",search);
        return "index";
    }
}
