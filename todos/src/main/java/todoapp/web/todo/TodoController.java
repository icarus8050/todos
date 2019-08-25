package todoapp.web.todo;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.AbstractView;

import todoapp.commons.domain.Spreadsheet;
import todoapp.core.todos.application.TodoFinder;
import todoapp.core.todos.domain.Todo;
import todoapp.web.convert.TodoToSpreadsheetConverter;
import todoapp.web.model.SiteProperties;

@Controller
public class TodoController {

	private Environment env;
	
	@Value("${sitee.author}")
	private String author;
	
	private SiteProperties site;
	
	private TodoFinder finder;
	
	public TodoController(Environment env, SiteProperties site, TodoFinder finder) {
		this.env = env;
		this.site = site;
		this.finder = finder;
	}
	
	@RequestMapping("/todos")
	public void todos(Model model) {
		List<Todo> todos = finder.getAll();
		Spreadsheet sheet = new TodoToSpreadsheetConverter().convert(todos);
		
		model.addAttribute(sheet);
	}
	
	public static class TodoCsvView extends AbstractView {

		@Override
		protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			
		}
		
	}
}
