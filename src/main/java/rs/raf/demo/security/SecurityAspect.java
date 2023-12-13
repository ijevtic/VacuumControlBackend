package rs.raf.demo.security;

import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.TokenService;
import rs.raf.demo.utils.JwtUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Aspect
@Configuration
public class SecurityAspect {
    private JwtUtil jwtUtil;

    public SecurityAspect(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Around("@annotation(rs.raf.demo.security.CheckSecurity)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //Get method signature
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        //Check for authorization parameter
        String token = null;
        for (int i = 0; i < methodSignature.getParameterNames().length; i++) {
            if (methodSignature.getParameterNames()[i].equals("authorization")) {
                //Check bearer schema
                if (joinPoint.getArgs()[i].toString().startsWith("Bearer")) {
                    //Get token
                    token = joinPoint.getArgs()[i].toString().split(" ")[1];
                }
            }
        }
        //If token is not presents return UNAUTHORIZED response
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        //Check user role and proceed if user has appropriate role for specified route
        CheckSecurity checkSecurity = method.getAnnotation(CheckSecurity.class);
        List<String> roles = jwtUtil.extractRoles(token);
        if (roles == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (roles.contains(checkSecurity.role())) {
            return joinPoint.proceed();
        }
        //Return FORBIDDEN if user has't appropriate role for specified route
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
