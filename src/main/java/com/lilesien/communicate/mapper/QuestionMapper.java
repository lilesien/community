package com.lilesien.communicate.mapper;

import com.lilesien.communicate.dto.QueryDTO;
import com.lilesien.communicate.dto.QuestionDTO;
import com.lilesien.communicate.pojo.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void insert(Question question);

    @Select("select * from question limit #{offset}, #{size}")
    List<Question> list(@Param("offset") Integer offset, @Param("size") Integer size);

    Integer count(@Param("tag") String tag, @Param("search") String search);

    @Select("select * from question where creator = #{userId} order by gmt_create desc limit #{offset}, #{size}")
    List<Question> findQuestionByUserId(@Param("userId") Integer userId, @Param("offset") Integer offset, @Param("size") Integer size);

    @Select("select * from question where id = #{id}")
    Question getByQuestionId(@Param("id") Integer id);

    @Update("update question set title = #{title}, description = #{description}, tag = #{tag}, gmt_modified = #{gmtModified} where id = #{id}")
    Integer update(Question question);

    @Update("update question set view_count = view_count + 1 where id = #{id}")
    void incrViewById(@Param("id") Integer id);

    @Select("select * from question where id = #{id}")
    Question selectById(@Param("id") Integer parentId);

    @Update("update question set comment_count = comment_count + #{commentCount} where id = #{id}")
    void incrCommentById(Question question);

    List<Question> listByGmtcreateDesc(QueryDTO queryDTO);

    @Select("select * from question where id != #{id} and tag regexp #{tag}")
    List<Question> selectRelatedQuestion(Question question);

    @Select("select count(1) from question where creator = #{userId}")
    Integer myQuestionCount(@Param("userId") Integer userId);

}
