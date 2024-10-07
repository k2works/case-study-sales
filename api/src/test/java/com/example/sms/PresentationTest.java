package com.example.sms;

import com.example.sms.infrastructure.datasource.system.auth.JWTDataSource;
import com.example.sms.infrastructure.datasource.system.user.UserDataSource;
import com.example.sms.infrastructure.datasource.system.user.UserObjMapper;
import com.example.sms.infrastructure.security.JWTAuth.JwtUtils;
import com.example.sms.presentation.Message;
import com.example.sms.service.system.auth.AuthApiService;
import com.example.sms.service.system.auth.AuthRepository;
import com.example.sms.service.system.auth.AuthService;
import com.example.sms.service.system.user.UserManagementService;
import com.example.sms.service.system.user.UserRepository;
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
                AuthRepository.class,
                AuthApiService.class,
                UserManagementService.class,
                UserRepository.class,
                AuthRepository.class,
                JwtUtils.class,
                JWTDataSource.class,
                UserDataSource.class,
                UserObjMapper.class,
                TestDataFactory.class,
                Message.class
        }
))
@ImportAutoConfiguration(H2ConsoleAutoConfiguration.class)
public @interface PresentationTest {
}
