package com.lilesien.communicate.controller;

import com.lilesien.communicate.dto.NotificationDTO;
import com.lilesien.communicate.dto.PaginationDTO;
import com.lilesien.communicate.dto.QuestionDTO;
import com.lilesien.communicate.mapper.NotificationMapper;
import com.lilesien.communicate.pojo.User;
import com.lilesien.communicate.service.NotificationService;
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

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          Model model,
                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                          @RequestParam(value = "size", defaultValue = "10") Integer size,
                          HttpSession session){
        User user = (User) session.getAttribute("user");
        model.addAttribute("myQuestion",questionService.myQuestionCount(user.getId()));
        if("questions".equals(action)){
            model.addAttribute("title","questions");
            model.addAttribute("titleName","我的提问");
            PaginationDTO<QuestionDTO> pageInfo = questionService.list(user.getId() ,page, size);
            model.addAttribute("pageInfo", pageInfo);
        }else{
            if("replies".equals(action)){
                model.addAttribute("title","replies");
                model.addAttribute("titleName","最新回复");
                PaginationDTO<NotificationDTO> pageInfo = notificationService.list(user.getId() ,page, size);
                model.addAttribute("pageInfo", pageInfo);
            }
        }
        return "profile";
    }
}
