package org.spring.e1i4TeamProject.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
        throws IOException, ServletException {
        // 인증 실패시 상태
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        // 기본적으로 전달하는 예외 메시지를 작성
        String errorMessage = "Invalid Username or Password";

        // 시큐리티 로그인 예외가 발생이 되면 exception 별로
        if (exception instanceof BadCredentialsException) {
            errorMessage = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해주세요.";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "내부 시스템 문제로 인해 요청을 처리할 수 없습니다. 관리자에게 문의해주세요.";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "계정이 존재하지 않습니다. 회원가입을 실행해주세요.";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errorMessage = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
        }
//        else {
//            errorMessage = "알 수 없는 이유로 로그인에 실패하였습니다. 관리자에게 문의하세요.";
//        }

        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");

        // /member/login페이지로 error, exception 보냄
        setDefaultFailureUrl("/member/memberLogin?error=true&exception=" + errorMessage);

        super.onAuthenticationFailure(request, response, exception);
    }
}
