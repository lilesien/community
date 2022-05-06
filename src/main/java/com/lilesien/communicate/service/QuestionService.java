package com.lilesien.communicate.service;

import com.lilesien.communicate.dto.PaginationDTO;
import com.lilesien.communicate.dto.QueryDTO;
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
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuestionService {
    @Autowired
    private UserMapper usermapper;

    @Autowired
    private QuestionMapper questionMapper;

    public PaginationDTO<QuestionDTO> list(String search, Integer page, Integer size){
        if(!StringUtils.isEmpty(search)){
            search = Arrays.stream(search.split(" ")).collect(Collectors.joining("|"));
        }else {
            search = null;
        }
        QueryDTO queryDTO = new QueryDTO();
        //问题的总条数转换为总的页数
        Integer pageCount = (questionMapper.count(search) + size - 1) / size;
        if(pageCount == 0){
            page = 1;
        }else{
            //处理页面不在范围内时的情况
            page = page < 1 ? 1 : page;
            page = page > pageCount ? pageCount : page;
        }
        int offset = (page - 1) * size;
        queryDTO.setSize(size);
        queryDTO.setOffset(offset);
        queryDTO.setSearch(search);
        //查询指定范围的问题
        List<Question> questionList = questionMapper.listByGmtcreateDesc(queryDTO);
        //新的问题类的集合
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        //页面的信息
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        //通过查出的问题的创建者ID找到用户，并封装到一个新的问题类中1
        for (Question question : questionList) {
            User user = usermapper.findById(question.getCreator());
            System.out.println("questionService:list(..)" + user);
            //创建QuestionDTO，将用户封装到新的问题类中，并将新的问题类添加到集合中
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        //将新的问题类的集合封装到页面类中
        paginationDTO.setData(questionDTOList);
        //设置当前页面的信息
        paginationDTO.setDetail(page, pageCount, size);
        return paginationDTO;
    }

    public Integer myQuestionCount(Integer userId){
        return questionMapper.myQuestionCount(userId);
    }

    public PaginationDTO<QuestionDTO> list(Integer userId, Integer page, Integer size) {
        //问题的总条数转换为总的页数
        Integer count = questionMapper.myQuestionCount(userId);
        Integer pageCount = (count + size - 1) / size;
        //处理页面不在范围内时的情况
        if(pageCount == 0){
            page = 1;
        }else {
            page = page < 1 ? 1 : page;
            page = page > pageCount ? pageCount : page;
        }
        Integer offset = (page - 1) * size;
        //查询指定范围的问题
        List<Question> questionList = questionMapper.findQuestionByUserId(userId ,offset, size);
        //新的问题类的集合
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        //页面的信息
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        //通过查出的问题的创建者ID找到用户，并封装到一个新的问题类中1
        for (Question question : questionList) {
            User user = usermapper.findById(question.getCreator());
            System.out.println("questionService : list(...) : " + user);
            //创建QuestionDTO，将用户封装到新的问题类中，并将新的问题类添加到集合中
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        //将新的问题类的集合封装到页面类中
        paginationDTO.setData(questionDTOList);
        //设置当前页面的信息
        paginationDTO.setDetail(page, pageCount, size);
        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getByQuestionId(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        //阅读数加1
        questionMapper.incrViewById(id);
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
            //更新失败，问题不存在
            if(update == 0){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    //相关问题
    public List<QuestionDTO> selectRelatedQuestion(QuestionDTO queryDTO) {
        if(StringUtils.isEmpty(queryDTO.getTag())){
            return new ArrayList<>();
        }
        String regexTag = Arrays.stream(queryDTO.getTag().split(",")).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexTag);

        List<Question> questions = questionMapper.selectRelatedQuestion(question);

        List<QuestionDTO> questionDTOList = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());

        return questionDTOList;

    }
}
