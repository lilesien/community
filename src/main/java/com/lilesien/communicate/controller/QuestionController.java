package com.lilesien.communicate.controller;

import com.lilesien.communicate.dto.QuestionDTO;
import com.lilesien.communicate.mapper.QuestionMapper;
import com.lilesien.communicate.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id,
                           Model model){

        QuestionDTO question = questionService.getById(id);

        model.addAttribute("question", question);

        return "question";
    }

}
