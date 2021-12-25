package com.ftmk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ftmk.dao.RegistrationDao;
import com.ftmk.dao.UserInfoDao;
import com.ftmk.model.UserConfirmation;
import com.ftmk.model.UserInfo;
import com.ftmk.model.UserPersonalDetails;
import com.ftmk.model.UserRole;
import com.ftmk.service.PasswordGenerator;

@Controller
public class RegisterController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private RegistrationDao registerDao;


	@RequestMapping(value = "/activate-account", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView tokenValidation(ModelAndView model, @RequestParam("token") String token) {

		String userToken = registerDao.tokenValidate(token);
		// check if token is null
		if (userToken == null) {
			model.addObject("message", "The link is invalid or has expired.");
			model.setViewName("errorToken");
		} else {
			// get user_id by token
			Integer id = registerDao.getIdByToken(userToken);
			registerDao.setEnabled(true, id);// set enabled to true
			model.setViewName("accountVerified");
		}

		return model;
	}

	@RequestMapping("/reset")
	public String redirectReset() {

		return "resetPasswordForm";

	}

	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
	public ModelAndView resetPassword(ModelAndView model, @RequestParam("username") String username,
			@RequestParam("icNumber") String icNumber) {

		String checkUsername = registerDao.searchUsername(username);
		String checkIC = registerDao.searchICByUsername(icNumber,username);
		System.out.println("IC number "+ checkIC);
		if ((checkUsername == null) || (checkIC == null)) {

			model.addObject("message", "Email or IC number does not match or exist");
			model.setViewName("resetPasswordForm");

		} else {
			
			String email=registerDao.userEmail(username);

			// generate password, encode it and save it to database
			PasswordGenerator generate = new PasswordGenerator();
			String generatedPassword = generate.generateCommonPassword();
			String encodedPassword = passwordEncoder.encode(generatedPassword);

			registerDao.resetPassword(encodedPassword, checkUsername);

			// Send new password to user email
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(email);
			mailMessage.setSubject("Reset Password");
			mailMessage.setText("You have requested to reset password.\n" + "Below is your new password \n"
					+ "Password:" + generatedPassword);
			mailSender.send(mailMessage);

			model.addObject("email", email);
			model.setViewName("resetComplete");

		}
		return model;
	}

	@RequestMapping(value = "/studentRegister", method = RequestMethod.GET)
	public String RegisterStudent(Model model) {

		return "StudentRegisterPage";
	}

	@RequestMapping(value = "/studentRegistration", method = RequestMethod.POST)
	public ModelAndView RegisterStudentForm(UserInfo user, ModelAndView model, UserConfirmation confirmation,
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

			registerDao.InsertStudentDetails(userPersonalDetails, Id);
			registerDao.InsertStudentRole(Id, "STUDENT");

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
			model.setViewName("StudentRegisterPage");
		}
		return model;

	}

}
