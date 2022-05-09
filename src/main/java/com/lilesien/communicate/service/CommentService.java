package com.lilesien.communicate.service;

import com.lilesien.communicate.dto.CommentDTO;
import com.lilesien.communicate.enums.CommentTypeEnum;
import com.lilesien.communicate.enums.NotificationTypeEnum;
import com.lilesien.communicate.enums.NotificationStatusEnum;
import com.lilesien.communicate.exception.CustomizeErrorCode;
import com.lilesien.communicate.exception.CustomizeException;
import com.lilesien.communicate.mapper.CommentMapper;
import com.lilesien.communicate.mapper.NotificationMapper;
import com.lilesien.communicate.mapper.QuestionMapper;
import com.lilesien.communicate.mapper.UserMapper;
import com.lilesien.communicate.pojo.Comment;
import com.lilesien.communicate.pojo.Notification;
import com.lilesien.communicate.pojo.Question;
import com.lilesien.communicate.pojo.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    //开启事务，对于评论插入和问题评论数增加同时成功或同时失败
    @Transactional
    public void insert(Comment comment, User commentUser) {
        //如果类型父类id为null或者0，则表示未选中任何问题或者评论，抛出异常
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        //如果评论的类型为null或者不存在，表示评论的对象丢失
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_ERROR);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            //通过parentId查找评论
            Comment commentFromDb = commentMapper.selectByParentId(comment.getParentId());
            if (commentFromDb == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            //查出一级评论属于哪个问题
            Question question = questionMapper.selectById(commentFromDb.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //回复的评论存在时，将评论插入数据库
            commentMapper.insert(comment);
            //父类评论评论数增加
            commentMapper.incrCommentCountById(1, comment.getParentId());
            //创建通知
            createNotify(comment, commentFromDb.getCommenter(), commentUser.getName(), question.getTitle(), NotificationTypeEnum.REPLAY_COMMENT,question.getId());
        } else {
            //回复问题
            //通过parentId查找问题
            Question question = questionMapper.selectById(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            //问题的评论数增加
            questionMapper.incrCommentById(question);
            //创建通知
            createNotify(comment, question.getCreator(), commentUser.getName(), question.getTitle(), NotificationTypeEnum.REPLAY_QUESTION,question.getId());

        }
    }

    /**
     * @param comment          评论的内容
     * @param receiver         接受通知的人（根据回复问题还是回复评论的得到接收通知的人）
     * @param notifierName
     * @param outerTitle
     * @param notificationTypeEnum 通知的状态
     */
    private void createNotify(Comment comment, Integer receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationTypeEnum, Integer questionId) {
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationTypeEnum.getType());
        //outerId设置为questionId是为了定向到问题
        notification.setOuterId(questionId);
        notification.setNotifier(comment.getCommenter());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    /**
     * 查询出问题的评论，根据（去重后的）评论的commentor去usermapper中查找出userlist
     * 然后根据commentlist中的commentor和userlist中的id进行比较封装在CommentDTO中，
     *
     * @param id
     * @return
     */
    public List<CommentDTO> listByIdAndType(Integer id, Integer type) {

        //查找评论
        List<Comment> commentList = commentMapper.selectCommentsByParentIdAndTypeDesc(id, type);
        if (commentList.size() == 0) {
            return new ArrayList<>();
        }

        //取出重复的评论人的id
        Set<Integer> commentators = commentList.stream().map(comment -> comment.getCommenter()).collect(Collectors.toSet());

        //获取评论人并转换为map
        List<User> userList = new ArrayList<>();
        Iterator<Integer> iterator = commentators.iterator();
        while (iterator.hasNext()) {
            userList.add(userMapper.findById(iterator.next()));
        }
        Map<Integer, User> userMap = userList.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        List<CommentDTO> commentDTOList = commentList.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommenter()));
            return commentDTO;
        }).collect(Collectors.toList());


        return commentDTOList;
    }
}
