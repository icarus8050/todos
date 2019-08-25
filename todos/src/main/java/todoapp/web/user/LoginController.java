package todoapp.web.user;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import todoapp.core.user.application.UserJoinder;
import todoapp.core.user.application.UserPasswordVerifier;
import todoapp.core.user.domain.User;
import todoapp.core.user.domain.UserEntityNotFoundException;
import todoapp.core.user.domain.UserPasswordNotMatchedException;
import todoapp.security.UserSession;
import todoapp.security.UserSessionRepository;

@Controller
public class LoginController {
	
	private final Logger log = LoggerFactory.getLogger(LoginController.class);
	
	private UserPasswordVerifier verifier;
	private UserJoinder joinder;
	private UserSessionRepository sessionRepository;
	
	public LoginController(UserPasswordVerifier verifier, UserJoinder joinder, UserSessionRepository sessionRepository) {
		this.verifier = verifier;
		this.joinder = joinder;
		this.sessionRepository = sessionRepository;
	}
	
	@GetMapping("/login")
	public void loginForm() {
		
	}
	
	@PostMapping("/login")
	public String loginProcess(@Valid LoginCommand command, Model model) {
		log.info("username: {}, password: {}", command.getUsername(), command.getPassword());
		
		//시스템 기본으로 등록된 사용자 => username: user, password: password
		
		User user;
		
		try {
			user = verifier.verify(command.getUsername(), command.getPassword());
		} catch (UserPasswordNotMatchedException error) {
			model.addAttribute("message", error.getMessage());
			return "login";
		} catch (UserEntityNotFoundException error) {
			//등록된 사용자가 없으면, 신규 사용자로 가입을 시켜줌
			user = joinder.join(command.getUsername(), command.getPassword());
		}
		log.info("current user : {}", user);
		sessionRepository.set(new UserSession(user));
		
		return "redirect:/todos";
	}
	
	@ExceptionHandler(BindException.class)
	public String handleBindException(BindException error, Model model) {
		model.addAttribute("bindingResult", error.getBindingResult());
		model.addAttribute("message", "사용자 입력 값이 올바르지 않습니다.");
		return "/login";
	}
	
	public static class LoginCommand {
		@Size(min = 4, max = 20)
		private String username;
		private String password;
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
	}
}
