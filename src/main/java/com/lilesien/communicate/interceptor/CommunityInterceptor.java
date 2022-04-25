package com.lilesien.communicate.interceptor;

import com.lilesien.communicate.exception.CustomizeErrorCode;
import com.lilesien.communicate.exception.CustomizeException;
import com.lilesien.communicate.pojo.User;
import com.lilesien.communicate.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class CommunityInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //由于会话关闭数据就不存在，需要通过cookie获取数据库用户，然后将用户存入session中
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accountId")) {
                    String accountId = cookie.getValue();
                    User user = userMapper.selectByAccountId(accountId);
                    log.info("拦截器通过cookie获取user:" + user);
                    request.getSession().setAttribute("user",user);
                    break;
                }
            }
        }
        //
        if(request.getSession().getAttribute("user") == null){
             throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
//             response.sendRedirect("/");
//             return false;
         }

        return true;
    }
}
