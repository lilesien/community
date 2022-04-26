package com.lilesien.communicate.dto;

import com.lilesien.communicate.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    //通过parentId判断是否对问题或者评论进行回复
    private Integer parentId;
    private Integer type;
    private Integer commenter;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private Integer commentCount;
    private User user;
}
