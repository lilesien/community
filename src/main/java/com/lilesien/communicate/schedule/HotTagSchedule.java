package com.lilesien.communicate.schedule;

import com.lilesien.communicate.cache.HotTagCache;
import com.lilesien.communicate.mapper.QuestionMapper;
import com.lilesien.communicate.pojo.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class HotTagSchedule {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private HotTagCache hotTagCache;

    @Scheduled(fixedDelay = 5000)
//    @Scheduled(cron = "* * 6/19 * * *")
    public void hotTag() {
        log.info("schedule start {}" ,new Date());
        Map<String, Integer> tagMap = new HashMap<>();
        //查询出所有的问题信息
        int offset = 0, size = 10;
        List<Question> questionList = new ArrayList<>();
        while(offset == 0 || questionList.size() == size) {
            questionList = questionMapper.list(offset, size);
            //对问题列表进行更新
            for (Question question : questionList) {
                //得到问题列表
                String[] tags = question.getTag().split(",");
                for (String tag: tags) {
                    Integer priority = tagMap.get(tag);
                    if (priority == null) {
                        tagMap.put(tag, 5 * question.getViewCount() + question.getCommentCount());
                    } else {
                        tagMap.put(tag, priority + 5 * question.getViewCount() + question.getCommentCount());
                    }
                }
            }
            offset += size;
        }

        hotTagCache.setTagMap(tagMap);

        hotTagCache.getTagMap().forEach((name,priority) -> {
            System.out.println(name + ":" + priority);
        });

        hotTagCache.getHotMessage(hotTagCache.getTagMap());
        log.info("schedule start {}", new Date());
    }

}
