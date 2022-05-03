package com.lilesien.communicate.controller;

import com.lilesien.communicate.dto.NotificationDTO;
import com.lilesien.communicate.dto.PaginationDTO;
import com.lilesien.communicate.dto.QuestionDTO;
import com.lilesien.communicate.enums.NotificationTypeEnum;
import com.lilesien.communicate.pojo.Notification;
import com.lilesien.communicate.pojo.User;
import com.lilesien.communicate.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class NotificationContoller {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String notify(@PathVariable(name = "id") String id,
                          Model model,
                          HttpSession session){
        User user = (User) session.getAttribute("user");
        //读取通知
        System.out.println(user);
        NotificationDTO notification = notificationService.read(id, user);
        if(NotificationTypeEnum.REPLAY_COMMENT.getType() == notification.getType() ||
        NotificationTypeEnum.REPLAY_QUESTION.getType() == notification.getType()){
            //outerId为回复的问题的id
            return "redirect:/question/" + notification.getOuterId();
        }
        return "redirect:/";
    }
}
