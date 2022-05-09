package com.lilesien.communicate.mapper;

import com.lilesien.communicate.pojo.Notification;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotificationMapper {

    @Insert("insert notification(notifier,receiver,outer_id,type,gmt_create,status,notifier_name,outer_title) values(#{notifier},#{receiver},#{outerId},#{type},#{gmtCreate},#{status},#{notifierName},#{outerTitle})")
    void insert(Notification notification);

    @Select("select count(1) from notification where receiver = #{userId}")
    Integer receiverEqualsId(@Param("userId") Integer userId);

    @Select("select * from notification where receiver = #{userId} order by gmt_create desc limit #{offset}, #{size}")
    List<Notification> findNotifiyLimitByUserId(@Param("userId") Integer userId,@Param("offset") Integer offset,@Param("size") Integer size);

    @Select("select count(1) from notification where receiver = #{userId} and status = 0")
    Integer countByIdAndUnRead(@Param("userId") Integer id);

    @Select("select * from notification where id = #{id}")
    Notification selectById(@Param("id") String id);

    @Update("update notification set status = #{status} where id = #{id}")
    void updateStatus(@Param("id") String id, @Param("status") Integer status);

}
