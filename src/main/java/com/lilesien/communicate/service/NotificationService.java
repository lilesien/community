package com.lilesien.communicate.service;

import com.lilesien.communicate.dto.NotificationDTO;
import com.lilesien.communicate.dto.PaginationDTO;
import com.lilesien.communicate.dto.QuestionDTO;
import com.lilesien.communicate.enums.NotificationStatusEnum;
import com.lilesien.communicate.enums.NotificationTypeEnum;
import com.lilesien.communicate.exception.CustomizeErrorCode;
import com.lilesien.communicate.exception.CustomizeException;
import com.lilesien.communicate.mapper.NotificationMapper;
import com.lilesien.communicate.pojo.Notification;
import com.lilesien.communicate.pojo.Question;
import com.lilesien.communicate.pojo.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    public PaginationDTO<NotificationDTO> list(Integer userId, Integer page, Integer size) {

        //统计总的通知
        Integer count = notificationMapper.receiverEqualsId(userId);

        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        //通知的总条数转换为总的页数
        Integer pageCount = (count + size - 1) / size;
        //处理页面不在范围内时的情况
        page = page < 1 ? 1 : page;
        page = page > pageCount ? pageCount : page;
        Integer offset = (page - 1) * size;
        //页面的信息
        paginationDTO.setDetail(page,pageCount,size);
        //查询对当前用户指定范围内的通知
        List<Notification> notifications = notificationMapper.findNotifiyLimitByUserId(userId ,offset, size);
        if(notifications.size() == 0)   return paginationDTO;
        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            //通过notification的int的type(对什么的通知)获取到string描述，将其封装在notificationDTO中
            notificationDTO.setNotifyType(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOList.add(notificationDTO);
        }
        //将通知的集合封装在paginationDTO中
        paginationDTO.setData(notificationDTOList);

        return paginationDTO;
    }

    public Integer unreadCount(Integer userId) {
        return notificationMapper.countByIdAndUnRead(userId);
    }

    public NotificationDTO read(String id, User user) {
        //从数据库中查出通知,通过id查找通知
        Notification notification = notificationMapper.selectById(id);
        //通知不存在
        if(notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        //通知的接受者和当前用户不同
        if(notification.getReceiver() != user.getId()){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        //设置为已读
        notificationMapper.updateStatus(id, NotificationStatusEnum.READ.getStatus());
        //
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setNotifyType(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
