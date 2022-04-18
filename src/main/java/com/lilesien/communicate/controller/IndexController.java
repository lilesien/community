package com.lilesien.communicate.controller;

import com.lilesien.communicate.dto.QuestionDTO;
import com.lilesien.communicate.pojo.User;
import com.lilesien.communicate.mapper.UserMapper;
import com.lilesien.communicate.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String index(HttpServletRequest request, Model model){
        Cookie[] cookies = request.getCookies();
        //从浏览器中判断cookie信息，
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accountId")) {
                    String accountId = cookie.getValue();
                    User user = userMapper.selectByAccountId(accountId);
                    log.info("数据库用户:" + user);
                    System.out.println(user);
                    //如果数据库中存在这个人，就添加到session中
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        List<QuestionDTO> list = questionService.list();
        log.info("question集合:" + list);
        model.addAttribute("questionList", list);
        return "index";
    }
}
