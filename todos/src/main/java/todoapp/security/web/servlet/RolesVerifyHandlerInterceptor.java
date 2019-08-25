package todoapp.security.web.servlet;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import todoapp.security.AccessDeniedException;
import todoapp.security.UnauthorizedAccessException;
import todoapp.security.UserSessionRepository;
import todoapp.security.support.RolesAllowedSupport;

/**
 * Role(역할) 기반으로 사용자가 사용 권한을 확인하는 인터셉터 구현체
 *
 * @author springrunner.kr@gmail.com
 */
public class RolesVerifyHandlerInterceptor implements HandlerInterceptor, RolesAllowedSupport {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    /*
    private UserSessionRepository sessionRepository;
    
    public RolesVerifyHandlerInterceptor(UserSessionRepository sessionRepository) {
    	this.sessionRepository = sessionRepository;
    }
    */

    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    		throws Exception {
    	
        if (handler instanceof HandlerMethod) {
        	HandlerMethod handlerMethod = ((HandlerMethod) handler);
        	RolesAllowed rolesAllowed = handlerMethod.getMethodAnnotation(RolesAllowed.class);
        	if (Objects.isNull(rolesAllowed)) {
        		rolesAllowed = AnnotatedElementUtils
        				.findMergedAnnotation(handlerMethod.getBeanType(), RolesAllowed.class);
        	}
        	
        	if (Objects.nonNull(rolesAllowed)) {
        		// 1.사용자가 로그인되어 있습니까?
        		/*
        		UserSession session = sessionRepository.get();
        		if (Objects.isNull(session)) {
        			throw new UnauthorizedAccessException();
        		}
        		*/
        		
        		if (Objects.isNull(request.getUserPrincipal())) {
        			throw new UnauthorizedAccessException();
        		}
        		
        		// 2. 사용자가 핸들러를 실행할 권한을 가지고 있습니까?
        		/*
        		Set<String> matchedRoles = Stream.of(rolesAllowed.value())
        				.filter(session::hasRole).
        				collect(Collectors.toSet());
        		*/
        		Set<String>matchedRoles = Stream.of(rolesAllowed.value())
        				.filter(request::isUserInRole)
        				.collect(Collectors.toSet());
        		log.info("matched roles: {}", matchedRoles);
        		
        		if (matchedRoles.size() > 0) {
        			//핸들러 실행 승인
        			return true;
        		}
        		//승인되지 않음
        		throw new AccessDeniedException();
        	}
        }
        
        return true;
    }

}
