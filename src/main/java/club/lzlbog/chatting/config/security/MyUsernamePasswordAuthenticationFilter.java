package club.lzlbog.chatting.config.security;

import club.lzlbog.chatting.dto.LoginDTO;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author li
 * @version 1.0
 * @date 2019-03-17 13:19
 * 自定义的认证过滤器：
 * 区分json提交和表单提交，分别进行处理
 **/
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(MyUsernamePasswordAuthenticationFilter.class);
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //json提交的登录处理
        if (MediaType.APPLICATION_JSON_UTF8_VALUE.equals(request.getContentType())
                || MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())) {
            //封装用户名和密码的对象
            LoginDTO loginDTO;

            //获取json提交的数据
            try {
                //获取输入流
                ServletInputStream inputStream = request.getInputStream();
                //将输入流转化成对象
                loginDTO = JSON.parseObject(inputStream, LoginDTO.class);
            } catch (IOException e) {
                //IO异常处理，待完善
                logger.error("无法解析json提交的数据！", e);
                loginDTO = new LoginDTO("","");
            }

            //构造认证的token对象
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername(), loginDTO.getPassword());

            // 继承自父类的处理
            setDetails(request, authRequest);
            //返回Authentication对象
            return this.getAuthenticationManager().authenticate(authRequest);

        } else {    //表单提交的登录处理
            return super.attemptAuthentication(request, response);
        }
    }
}
