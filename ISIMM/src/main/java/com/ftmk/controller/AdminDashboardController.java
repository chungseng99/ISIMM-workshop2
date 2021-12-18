package com.ftmk.controller;

import java.net.URLEncoder;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ftmk.dao.RegistrationDao;
import com.ftmk.dao.UserDao;
import com.ftmk.dao.UserInfoDao;
import com.ftmk.model.UserConfirmation;
import com.ftmk.model.UserInfo;
import com.ftmk.model.UserPersonalDetails;
import com.ftmk.model.UserRole;
import com.ftmk.model.UserTableDisplay;
import com.ftmk.service.PasswordGenerator;

@Controller
public class AdminDashboardController {
	

	@Autowired
	private UserInfoDao userInfoDao;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private RegistrationDao registerDao;

	@Autowired
	private JavaMailSender mailSender;

	/*** START OF ADMIN DASHBOARD ***/
	@SuppressWarnings("unchecked")
	// Add list of user into admin dashboard and display
	@RequestMapping(value = { "/adminDashboard", "/{page}"}, method = RequestMethod.GET)
	public ModelAndView adminPage(ModelAndView model, @PathVariable(required = false, name = "page") String page,
			HttpServletRequest request, HttpServletResponse response) {

		PagedListHolder<UserTableDisplay> userList;
		List<UserTableDisplay> displayList;
		if (page == null) {

			userList = new PagedListHolder<UserTableDisplay>();
			displayList = userInfoDao.listUser();
			userList.setSource(displayList);
			userList.setPageSize(5);
			request.getSession().setAttribute("userList", userList);

		} else if (page.equals("prev")) {

			userList = (PagedListHolder<UserTableDisplay>) request.getSession().getAttribute("userList");
			userList.previousPage();

		} else if (page.equals("next")) {

			userList = (PagedListHolder<UserTableDisplay>) request.getSession().getAttribute("userList");
			userList.nextPage();

		} else {

			int pageNum = Integer.parseInt(page);
			userList = (PagedListHolder<UserTableDisplay>) request.getSession().getAttribute("userList");
			userList.setPage(pageNum - 1);

		}

		model.setViewName("adminDashboard");
		return model;

	}

	@RequestMapping(value = "/createUser", method = RequestMethod.GET)
	public String CreateUser(Model model) {

		return "createUserPage";
	}

	@RequestMapping(value = "/createUserForm", method = RequestMethod.POST)
	public ModelAndView CreateUserForm(UserInfo user, ModelAndView model, UserConfirmation confirmation,
			UserPersonalDetails userPersonalDetails, UserRole userRole) {

		String existingUsername = registerDao.searchUsername(user.getUsername());
		String existingIC = registerDao.searchIC(userPersonalDetails.getIcNumber());

		// check if username exist
		if ((existingUsername == null) && (existingIC == null)) {

			// generate password, encode it and save it to database
			PasswordGenerator generate = new PasswordGenerator();
			String generatedPassword = generate.generateCommonPassword();
			String encodedPassword = passwordEncoder.encode(generatedPassword);
			user.setPassword(encodedPassword);

			// save username and password into database
			registerDao.register(user);
			Integer Id = registerDao.getUserId(user);// get user_id
			registerDao.getToken(Id, confirmation);// get token by user_id

			System.out.println("email " + userPersonalDetails.getEmail());// print some details for debugging
			System.out.println("Role from form " + userRole.getRole());
			System.out.println("Name: " + userPersonalDetails.getName());
			System.out.println("IC: " + userPersonalDetails.getIcNumber());

			registerDao.InsertUserDetails(userPersonalDetails, Id);
			registerDao.InsertRole(Id, userRole);

			// Send link to activate user account
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(userPersonalDetails.getEmail());
			mailMessage.setSubject("Registration Completed!");
			mailMessage.setText("To activate your account, please click the link here: "
					+ "http://localhost:8080/SchoolInformationSystem/activate-account?token="
					+ confirmation.getConfirmationToken() + "\n" + "Use username and password below to sign in:" + "\n"
					+ "Username:" + user.getUsername() + "\n" + "Password:" + generatedPassword);
			mailSender.send(mailMessage);

			model.addObject("email", userPersonalDetails.getEmail());
			model.setViewName("successfulRegistration");
		} else {

			model.addObject("message", "Email or IC number has been registered");
			model.setViewName("createUserPage");
		}
		return model;

	}

	// Select the user to edit based on userId
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editUser(@RequestParam Integer userId) {

		// Integer userId = Integer.parseInt(request.getParameter("userId"));
		UserTableDisplay user = userInfoDao.getUserById(userId);

		ModelAndView model = new ModelAndView("editUserForm");
		model.addObject("userList", user);
		return model;

	}

	@RequestMapping(value = "/deactivate", method = RequestMethod.GET)
	public ModelAndView deactivateUser(@RequestParam Integer userId) {

		userInfoDao.deactivateUser(userId);
		return new ModelAndView("redirect:/adminDashboard");
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView updateUser(@ModelAttribute("userList") UserTableDisplay user) {

		userInfoDao.updateUser(user);
		return new ModelAndView("redirect:/adminDashboard");

	}

	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/viewAll","/viewAll/{page}"} , method = RequestMethod.GET)
	public ModelAndView viewUser(ModelAndView model, @PathVariable(required = false, name = "page") String page,
			HttpServletRequest request, HttpServletResponse response) {

		PagedListHolder<UserTableDisplay> userList;
		List<UserTableDisplay> displayList;
		if (page == null) {

			userList = new PagedListHolder<UserTableDisplay>();
			displayList = userInfoDao.listUser();
			userList.setSource(displayList);
			userList.setPageSize(10);
			request.getSession().setAttribute("userList", userList);

		} else if (page.equals("prev")) {

			userList = (PagedListHolder<UserTableDisplay>) request.getSession().getAttribute("userList");
			userList.previousPage();

		} else if (page.equals("next")) {

			userList = (PagedListHolder<UserTableDisplay>) request.getSession().getAttribute("userList");
			userList.nextPage();

		} else {

			int pageNum = Integer.parseInt(page);
			userList = (PagedListHolder<UserTableDisplay>) request.getSession().getAttribute("userList");
			userList.setPage(pageNum - 1);

		}

		model.setViewName("viewUser");
		return model;

	}

	@RequestMapping(value = "/searchUser", method = RequestMethod.GET)
	public String search() {

		return "search";

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/searchUsername","/searchUsername/{page}"}, method = RequestMethod.GET)
	public ModelAndView searchByUsername(ModelAndView model, @RequestParam("searchUsername") String search,
			@PathVariable(required = false, name = "page") String page,
			HttpServletRequest request, HttpServletResponse response) {

		List<UserTableDisplay> usernameSearchResult = userInfoDao.searchByUsername(search);
			
		if (usernameSearchResult.isEmpty()|| search.isBlank()) {
			model.addObject("Message", "No Result Found");
			model.setViewName("noResultFound");

		} else {
			PagedListHolder<UserTableDisplay> userList;
			if (page == null) {

				userList = new PagedListHolder<UserTableDisplay>();
				userList.setSource(usernameSearchResult);
				userList.setPageSize(10);
				request.getSession().setAttribute("userList", userList);

			} else if (page.equals("prev")) {

				userList = (PagedListHolder<UserTableDisplay>) request.getSession().getAttribute("userList");
				userList.previousPage();

			} else if (page.equals("next")) {

				userList = (PagedListHolder<UserTableDisplay>) request.getSession().getAttribute("userList");
				userList.nextPage();

			} else {

				int pageNum = Integer.parseInt(page);
				userList = (PagedListHolder<UserTableDisplay>) request.getSession().getAttribute("userList");
				userList.setPage(pageNum - 1);

			}
			String encode=URLEncoder.encode(search);
			model.addObject("search",encode);
			model.setViewName("searchResult");
		}
		return model;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/searchName","/searchName/{page}"}, method = RequestMethod.GET)
	public ModelAndView searchByName(ModelAndView model, @RequestParam("searchName") String search,
			@PathVariable(required = false, name = "page") String page,
			HttpServletRequest request, HttpServletResponse response) {

		List<UserTableDisplay> nameSearchResult = userInfoDao.searchByName(search);

		if (nameSearchResult.isEmpty()|| search.isBlank()) {

			model.addObject("Message", "No Result Found");
			model.setViewName("noResultFound");

		} else {

			PagedListHolder<UserTableDisplay> userList;
			if (page == null) {

				userList = new PagedListHolder<UserTableDisplay>();
				userList.setSource(nameSearchResult);
				userList.setPageSize(10);
				request.getSession().setAttribute("userList", userList);

			} else if (page.equals("prev")) {

				userList = (PagedListHolder<UserTableDisplay>) request.getSession().getAttribute("userList");
				userList.previousPage();

			} else if (page.equals("next")) {

				userList = (PagedListHolder<UserTableDisplay>) request.getSession().getAttribute("userList");
				userList.nextPage();

			} else {

				int pageNum = Integer.parseInt(page);
				userList = (PagedListHolder<UserTableDisplay>) request.getSession().getAttribute("userList");
				userList.setPage(pageNum - 1);

			}
			String encode=URLEncoder.encode(search);
			model.addObject("search",encode);
			model.setViewName("searchResult");
		}
		return model;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/searchIC", method = RequestMethod.GET)
	public ModelAndView searchByIC(ModelAndView model, @RequestParam("searchIC") String search,
			@PathVariable(required = false, name = "page") String page,
			HttpServletRequest request, HttpServletResponse response) {

		List<UserTableDisplay> icSearchResult = userInfoDao.searchByIC(search);

		if (icSearchResult.isEmpty()|| search.isBlank()) {
			model.addObject("Message", "No Result Found");
			model.setViewName("noResultFound");

		} else {
			PagedListHolder<UserTableDisplay> userList;
			if (page == null) {

				userList = new PagedListHolder<UserTableDisplay>();
				userList.setSource(icSearchResult);
				userList.setPageSize(10);
				request.getSession().setAttribute("userList", userList);

			} else if (page.equals("prev")) {

				userList = (PagedListHolder<UserTableDisplay>) request.getSession().getAttribute("userList");
				userList.previousPage();

			} else if (page.equals("next")) {

				userList = (PagedListHolder<UserTableDisplay>) request.getSession().getAttribute("userList");
				userList.nextPage();

			} else {

				int pageNum = Integer.parseInt(page);
				userList = (PagedListHolder<UserTableDisplay>) request.getSession().getAttribute("userList");
				userList.setPage(pageNum - 1);

			}
			String encode=URLEncoder.encode(search);
			model.addObject("search",encode);
			model.setViewName("searchResult");
		}
		return model;

	}

	@RequestMapping(value = "/redirectChangePassword")
	public String redirectChangePassword() {

		return "changePasswordForm";

	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public String changePassword(@RequestParam("password") String password, Principal principal,HttpServletRequest request) {

		String newPassword = passwordEncoder.encode(password);
		String username = principal.getName();
		String role = userInfoDao.getUserRoles(username);
		userInfoDao.changePassword(newPassword, username);
		
		if(role.equals("ADMIN")) {
			return "redirect:/adminDashboard";
		}else if(role.equals("TEACHER")) {
			return "redirect:/teacherDashboard";
		}else if (role.equals("PARENT")) {
			 return "";
		}else if (role.equals("CLERK")) {
			 return "redirect:/clerkDashboard";
		}else if (role.equals("STUDENT")) {
			 return "";
		}else 
			
		return "403";
		
		
		
	}

	/*** END OF ADMIN DASHBOARD ***/

}
