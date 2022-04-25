package com.lilesien.communicate.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Long id;
    //通过parentId判断是否对问题或者评论进行回复
    private Integer parentId;
    private Integer type;
    private Integer commenter;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
}
