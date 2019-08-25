package todoapp.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import todoapp.security.UserSession;
import todoapp.security.UserSessionRepository;

public class UserSessionArgumentResolver implements HandlerMethodArgumentResolver {
	
	private final Logger log = LoggerFactory.getLogger(UserSessionArgumentResolver.class);
	private UserSessionRepository sessionRepository;
	
	public UserSessionArgumentResolver(UserSessionRepository sessionRepository) {
		this.sessionRepository = sessionRepository;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().isAssignableFrom(UserSession.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		UserSession session = sessionRepository.get();
		log.debug("사용자 세션 정보를 반환합니다. (session: {});", session);
		
		return session;
	}

}
