package com.lilesien.communicate.controller;

import com.lilesien.communicate.pojo.Question;
import com.lilesien.communicate.pojo.User;
import com.lilesien.communicate.mapper.QuestionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam("title") String title,
                            @RequestParam("description") String description,
                            @RequestParam("tag") String tag,
                            HttpSession session,
                            Model model){
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        if(StringUtils.isEmpty(title)){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if(StringUtils.isEmpty(description)){
            model.addAttribute("error", "内容不能为空");
            return "publish";
        }
        Question question = new Question();
        question.setTitle(title);
        question.setTag(tag);
        question.setDescription(description);
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        User user = (User) session.getAttribute("user");
        log.info("发布问题的用户：" + user);
        question.setCreator(user.getId());
        questionMapper.insert(question);
        log.info("问题插入数据库");
        return "redirect:/";
    }
}
