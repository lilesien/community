package com.lilesien.communicate.controller;

import com.lilesien.communicate.dto.CommentDTO;
import com.lilesien.communicate.dto.QuestionDTO;
import com.lilesien.communicate.service.CommentService;
import com.lilesien.communicate.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id,
                           Model model){

        QuestionDTO question = questionService.getById(id);

        List<CommentDTO> commentDTOList = commentService.listByQuestionId(question.getId());

        model.addAttribute("question", question);
        model.addAttribute("comments",commentDTOList);
        return "question";
    }

}
