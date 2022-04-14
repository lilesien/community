package com.lilesien.communicate.provider;

import com.alibaba.fastjson.JSON;
import com.lilesien.communicate.dto.AccessTokenDTO;
import com.lilesien.communicate.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {


    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON.toJSONString(accessTokenDTO),mediaType);
        Request request = new Request.Builder()
                //请求的路径，body为数据
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        /**
         * 默认情况下，响应采用以下形式
         * access_token=gho_16C7e42F292c6912E7710c838347Ae178B4a&scope=repo%2Cgist&token_type=bearer
         */
        try (Response response = client.newCall(request).execute()) {
            //进行分割获取access_token
            String string = response.body().string();
            String s = string.split("&")[0].split("=")[1];
            return s;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    //拿到token后通过user路径传递参数请求用户数据
    public GithubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization","token "+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
