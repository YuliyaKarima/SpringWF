package controller;

import static org.apache.log4j.Logger.getLogger;

import java.security.Principal;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SecurityController {
	private static final Logger LOGGER = getLogger(SecurityController.class);	
	
	@RequestMapping("/admin/mypage")
	public String mypage(Model model, Principal principal) {
		String userName = principal.getName();
		model.addAttribute("message", "Hi " + userName + ", Welcome to Admin page");		
		return "admin/mypage";
	}
	
	@RequestMapping("/cooker/mypage")
	public String cookerMypage(Model model, Principal principal) {
		String userName = principal.getName();
		model.addAttribute("message", "Hi " + userName + ", Welcome to Cooker admin page");		
		return "cooker/mypage";
	}
	
	@RequestMapping("/")
	public String start() {		
		return "redirect:/pizzaFlow";
	}

	@RequestMapping(value = "/logoutPage", method = RequestMethod.GET)
	public String logoutPage() {
		return "logoutPage";
	}

	@RequestMapping(value = "/loginPage", method = RequestMethod.GET)
	public String loginPage() {
		return "loginPage";
	}
}
