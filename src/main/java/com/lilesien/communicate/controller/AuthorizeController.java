package com.lilesien.communicate.controller;

import com.lilesien.communicate.dto.AccessTokenDTO;
import com.lilesien.communicate.dto.GithubUser;
import com.lilesien.communicate.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider provider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.client.redirectUri}")
    private String redirectUri;

    //callback为第一次请求用户信息时的回调网址
    @RequestMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state){
        //第二次请求获取access_token
        AccessTokenDTO tokenDTO = new AccessTokenDTO();
        //设置参数
        tokenDTO.setClient_secret("0fcc6db2e9213b42ec4e77a3daf438e37e94f362");
        tokenDTO.setClient_id("Iv1.933474e281b2fa6b");
        tokenDTO.setCode(code);
//        tokenDTO.setRedirect_uri("http://localhost:8080/callback");
        tokenDTO.setState(state);
        //发送请求，获取token
        String accessToken = provider.getAccessToken(tokenDTO);
        System.out.println(accessToken);
        //拿到token后第三步请求用户数据
        GithubUser user = provider.getUser(accessToken);
        System.out.println(user.getName());
        return "index";
    }

}
