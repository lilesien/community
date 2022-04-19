package com.lilesien.communicate.controller;

import com.lilesien.communicate.dto.AccessTokenDTO;
import com.lilesien.communicate.dto.GithubUser;
import com.lilesien.communicate.pojo.User;
import com.lilesien.communicate.mapper.UserMapper;
import com.lilesien.communicate.provider.GithubProvider;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lilesien.communicate.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class AuthorizeController {

    @Autowired
    private GithubProvider provider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.client.redirectUri}")
    private String redirectUri;

    
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;
    
    //callback为第一次请求用户信息时的回调网址
    @RequestMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response) {
        System.out.println( "code = " + code);
        //第二次请求获取access_token
        AccessTokenDTO tokenDTO = new AccessTokenDTO();
        //设置参数
        tokenDTO.setClient_secret(clientSecret);
        tokenDTO.setClient_id(clientId);
        tokenDTO.setCode(code);
//        tokenDTO.setRedirect_uri("http://localhost:8080/callback");
        tokenDTO.setState(state);
        //发送请求，获取token
        String accessToken = provider.getAccessToken(tokenDTO);
        System.out.println("accessToken = " + accessToken);
        //拿到token后第三步请求用户数据
        GithubUser githubUser = provider.getUser(accessToken);
        System.out.println(githubUser == null ? "githubUser" : "githubUser :" + githubUser.getName());
        if(githubUser != null){
            //设置数据库用户
            User user = new User();
            //设置数据库用户accessToken信息,每次获取的accessToken信息都不一样
            user.setToken(accessToken);
            //将github得到的用户信息存入到数据库
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatarUrl());

            log.info("与数据库中token不一样的用户:" + user);

            userService.createOrUpdate(user);
/*            if(userMapper.selectByAccountId(user.getAccountId()) == null) {
                log.info("数据库新增用户");
                userMapper.insert(user);
            }*/
            //session.setAttribute("user",githubUser);转为使用cookie存储用户信息返回到首页进行判断
            //存储name为token，value为数据库中用户token的cookie
            response.addCookie(new Cookie("accountId", user.getAccountId()));
            return "redirect:/";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        //删除session中的user属性
        request.getSession().removeAttribute("user");
        //删除cookie
        Cookie cookie = new Cookie("accountId",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

}
