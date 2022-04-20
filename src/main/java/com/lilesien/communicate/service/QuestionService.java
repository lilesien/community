package com.lilesien.communicate.service;

import com.lilesien.communicate.dto.PaginationDTO;
import com.lilesien.communicate.dto.QuestionDTO;
import com.lilesien.communicate.exception.CustomizeErrorCode;
import com.lilesien.communicate.exception.CustomizeException;
import com.lilesien.communicate.mapper.QuestionMapper;
import com.lilesien.communicate.mapper.UserMapper;
import com.lilesien.communicate.pojo.Question;
import com.lilesien.communicate.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class QuestionService {
    @Autowired
    private UserMapper usermapper;

    @Autowired
    private QuestionMapper questionMapper;

    public PaginationDTO list(Integer page, Integer size){
        //问题的总条数转换为总的页数
        Integer pageCount = (questionMapper.count() + size - 1) / size;
        //处理页面不在范围内时的情况
        page = page < 1 ? 1 : page;
        page = page > pageCount ? pageCount : page;
        int offset = (page - 1) * size;
        //查询指定范围的问题
        List<Question> questionList = questionMapper.list(offset, size);
        //新的问题类的集合
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        //页面的信息
        PaginationDTO paginationDTO = new PaginationDTO();
        //通过查出的问题的创建者ID找到用户，并封装到一个新的问题类中1
        for (Question question : questionList) {
            User user = usermapper.findById(question.getCreator());
            System.out.println(user);
            //创建QuestionDTO，将用户封装到新的问题类中，并将新的问题类添加到集合中
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        //将新的问题类的集合封装到页面类中
        paginationDTO.setQuestionList(questionDTOList);
        //设置当前页面的信息
        paginationDTO.setDetail(page, pageCount, size);
        return paginationDTO;
    }

    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        //问题的总条数转换为总的页数
        Integer pageCount = (questionMapper.count() + size - 1) / size;
        //处理页面不在范围内时的情况
        page = page < 1 ? 1 : page;
        page = page > pageCount ? pageCount : page;
        Integer offset = (page - 1) * size;
        //查询指定范围的问题
        List<Question> questionList = questionMapper.findQuestionByUserId(userId ,offset, size);
        //新的问题类的集合
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        //页面的信息
        PaginationDTO paginationDTO = new PaginationDTO();
        //通过查出的问题的创建者ID找到用户，并封装到一个新的问题类中1
        for (Question question : questionList) {
            User user = usermapper.findById(question.getCreator());
            System.out.println(user);
            //创建QuestionDTO，将用户封装到新的问题类中，并将新的问题类添加到集合中
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        //将新的问题类的集合封装到页面类中
        paginationDTO.setQuestionList(questionDTOList);
        //设置当前页面的信息
        paginationDTO.setDetail(page, pageCount, size);
        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getByQuestionId(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        User user = usermapper.findById(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }

    /**
     *
     * @param question  问题的信息
     */
    public void createOrUpdate(Question question) {
        //如果id为空，表示是新发布的问题，如果不是，则表示是修改问题
        if(question.getId() == null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        }else {
            question.setGmtModified(System.currentTimeMillis());
            Integer update = questionMapper.update(question);
            if(update == 0){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }
}
