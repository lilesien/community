package com.lilesien.communicate.controller;

import com.lilesien.communicate.dto.QuestionDTO;
import com.lilesien.communicate.pojo.Question;
import com.lilesien.communicate.pojo.User;
import com.lilesien.communicate.mapper.QuestionMapper;
import com.lilesien.communicate.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @RequestMapping("/publish/{id}")
    public String publish(@PathVariable(name = "id") Integer id,
                          Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        model.addAttribute("title",questionDTO.getTitle());
        model.addAttribute("description",questionDTO.getDescription());
        model.addAttribute("tag",questionDTO.getTag());
        model.addAttribute("id",id);
        return "publish";
    }

    /**
     *
     * @param title 问题标题
     * @param description   问题描述
     * @param tag   问题标签
     * @param id    问题id
     * @param session
     * @param model
     * @return
     */
    @PostMapping("/publish")
    public String doPublish(@RequestParam("title") String title,
                            @RequestParam("description") String description,
                            @RequestParam("tag") String tag,
                            @RequestParam(value = "id") Integer id,
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
        //设置question的id，如果为空则表示新问题，不为空表示对就问题做出修改
        question.setId(id);
        User user = (User) session.getAttribute("user");
        question.setCreator(user.getId());
        log.info("question:" + question);
//        questionMapper.insert(question);
        questionService.createOrUpdate(id, question);
        log.info("问题插入数据库");
        return "redirect:/";
    }
}
