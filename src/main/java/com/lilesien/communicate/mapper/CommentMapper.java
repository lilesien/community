package com.lilesien.communicate.mapper;

import com.lilesien.communicate.pojo.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("insert into comment (parent_id,type,commenter,gmt_create,gmt_modified,content) values(#{parentId},#{type},#{commenter},#{gmtCreate},#{gmtModified},#{content})")
    int insert(Comment comment);

    @Select("select * from comment where id = #{id}")
    Comment selectByParentId(@Param("id") Integer parentId);

    @Select("select * from comment where parent_id = #{parentId} and type = #{type} order by gmt_create desc")
    List<Comment> selectCommentsByParentIdAndTypeDesc(@Param("parentId") Integer id, @Param("type") Integer type);
}
