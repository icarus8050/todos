package todoapp.web.todo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import todoapp.web.model.SiteProperties;

@Controller
public class TodoController {

	private Environment env;
	
	@Value("${site.author}")
	private String author;
	
	private SiteProperties site;
	
	public TodoController(Environment env, SiteProperties site) {
		this.env = env;
		this.site = site;
	}
	
	@RequestMapping("/todos")
	public void todos(Model model) {
		model.addAttribute("site", site);
	}
}
