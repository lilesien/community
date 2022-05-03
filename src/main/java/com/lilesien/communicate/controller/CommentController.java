package com.lilesien.communicate.controller;

import com.lilesien.communicate.dto.CommentCreateDTO;
import com.lilesien.communicate.dto.CommentDTO;
import com.lilesien.communicate.dto.ResultDTO;
import com.lilesien.communicate.enums.CommentTypeEnum;
import com.lilesien.communicate.exception.CustomizeErrorCode;
import com.lilesien.communicate.pojo.Comment;
import com.lilesien.communicate.pojo.User;
import com.lilesien.communicate.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    //@RequestBody主要用来接收前端传递给后端的json字符串
    @PostMapping("/comment")
    @ResponseBody
    public Object post(@RequestBody CommentCreateDTO commentDTO,
                       HttpSession session){
        User user =  (User) session.getAttribute("user");
        System.out.println("comment" + user);
        //用户没有登陆
        if(user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if(commentDTO == null || StringUtils.isEmpty(commentDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO,comment);
        //设置时间
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        //设置评论是谁评论的
        comment.setCommenter(user.getId());
        comment.setCommentCount(0);
        commentService.insert(comment, user);

        System.out.println(comment);
        return ResultDTO.ok();
    }

    @ResponseBody
    @GetMapping("/comment/{id}")
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Integer id){
        List<CommentDTO> commentDTOList = commentService.listByIdAndType(id, CommentTypeEnum.COMMENT.getType());

        return ResultDTO.ok(commentDTOList);
    }
}
