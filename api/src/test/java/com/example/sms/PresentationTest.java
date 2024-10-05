package com.example.sms;

import com.example.sms.infrastructure.datasource.user.UserDataSource;
import com.example.sms.infrastructure.datasource.user.UserObjMapper;
import com.example.sms.infrastructure.repository.user.UserRepository;
import com.example.sms.infrastructure.security.JWTAuth.JwtUtils;
import com.example.sms.service.system.auth.AuthApiService;
import com.example.sms.service.system.auth.AuthService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@WebMvcTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {
                AuthService.class,
                AuthApiService.class,
                UserRepository.class,
                WebSecurityConfig.class,
                JwtUtils.class,
                UserDataSource.class,
                UserObjMapper.class
        }
))
public @interface PresentationTest {
}
