package com.lilesien.communicate.controller;

import com.lilesien.communicate.cache.HotTagCache;
import com.lilesien.communicate.dto.HotTagDTO;
import com.lilesien.communicate.dto.PaginationDTO;
import com.lilesien.communicate.dto.QuestionDTO;
import com.lilesien.communicate.pojo.User;
import com.lilesien.communicate.mapper.UserMapper;
import com.lilesien.communicate.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@Slf4j
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private HotTagCache hotTagCache;

    @RequestMapping({"/","/index"})
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "10") Integer size,
                        @RequestParam(value = "search", required = false) String search,
                        @RequestParam(value = "tag", required = false) String tag){
//        List<QuestionDTO> list = questionService.list();//分页前的question集合
        PaginationDTO<QuestionDTO> pagination = questionService.list(tag,search, page, size);
/*        log.info("question集合:" + list);
        model.addAttribute("questionList", list);*/
        List<String> hotTagList = hotTagCache.getHots();
        model.addAttribute("pageInfo", pagination);
        model.addAttribute("search",search);
        model.addAttribute("tag",tag);
        model.addAttribute("hotTags",hotTagList);
        return "index";
    }
}
