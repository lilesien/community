package com.lilesien.communicate.dto;

import com.lilesien.communicate.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {

    private Integer id;
    private Long gmtCreate;
    private Integer status;
    private Integer notifier;
    private String notifierName;
    private String outerTitle;
    private Integer outerId;
    //这个同时是对问题的评论还是对评论的评论
    private String notifyType;
    private Integer type;

}
