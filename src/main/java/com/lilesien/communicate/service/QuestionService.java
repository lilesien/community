package com.lilesien.communicate.service;

import com.lilesien.communicate.dto.QuestionDTO;
import com.lilesien.communicate.mapper.QuestionMapper;
import com.lilesien.communicate.mapper.UserMapper;
import com.lilesien.communicate.pojo.Question;
import com.lilesien.communicate.pojo.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private UserMapper usermapper;

    @Autowired
    private QuestionMapper questionMapper;

    public List<QuestionDTO> list(){
        List<Question> questionList = questionMapper.list();
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questionList) {
            User user = usermapper.findById(question.getCreator());
            System.out.println(user);
            //创建QuestionDTO
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }
}
