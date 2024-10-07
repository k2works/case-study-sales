package com.example.sms;

import com.example.sms.infrastructure.Message;
import com.example.sms.infrastructure.datasource.system.user.UserDataSource;
import com.example.sms.infrastructure.datasource.system.user.UserObjMapper;
import com.example.sms.infrastructure.repository.system.auth.JWTRepository;
import com.example.sms.infrastructure.repository.system.user.UserRepository;
import com.example.sms.infrastructure.security.JWTAuth.JwtUtils;
import com.example.sms.service.system.auth.AuthApiService;
import com.example.sms.service.system.auth.AuthService;
import com.example.sms.service.system.user.UserManagementService;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
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
                WebSecurityConfig.class,
                AuthService.class,
                AuthApiService.class,
                UserManagementService.class,
                UserRepository.class,
                JWTRepository.class,
                JwtUtils.class,
                UserDataSource.class,
                UserObjMapper.class,
                TestDataFactory.class,
                Message.class
        }
))
@ImportAutoConfiguration(H2ConsoleAutoConfiguration.class)
public @interface PresentationTest {
}
