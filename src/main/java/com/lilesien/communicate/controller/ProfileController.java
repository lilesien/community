package com.lilesien.communicate.controller;

import com.lilesien.communicate.dto.PaginationDTO;
import com.lilesien.communicate.pojo.User;
import com.lilesien.communicate.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          Model model,
                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                          @RequestParam(value = "size", defaultValue = "3") Integer size,
                          HttpSession session){
        if("questions".equals(action)){
            model.addAttribute("title","questions");
            model.addAttribute("titleName","我的提问");
        }else{
            if("replies".equals(action)){
                model.addAttribute("title","replies");
                model.addAttribute("titleName","最新回复");
            }
        }
        User user = (User) session.getAttribute("user");
        PaginationDTO pageInfo = questionService.list(user.getId() ,page, size);
        model.addAttribute("pageInfo", pageInfo);
        return "profile";
    }
}
