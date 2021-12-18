package com.ftmk.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ftmk.dao.UserInfoDao;
import com.ftmk.model.UserInfo;
import com.ftmk.model.UserPersonalDetails;
import com.ftmk.model.UserTableDisplay;

@Controller
public class LoginController {
	
	@RequestMapping("/")
	public String main() {
		
		return"loginPage";
	}
	
	
	@RequestMapping(value="/logoutSuccessful")
	public String logoutRedirect() {
		
		return"redirect:/";
	}
	
	@RequestMapping("/default")
    public String defaultAfterLogin(HttpServletRequest request) {
		
        if (request.isUserInRole("ROLE_ADMIN")) {
            return "redirect:/adminDashboard";
        }else if (request.isUserInRole("ROLE_STUDENT")) {
        	 return "";
        }else if(request.isUserInRole("ROLE_PARENT")) {
        	return "";
        }else if(request.isUserInRole("ROLE_CLERK")) {
        	return "redirect:/clerkDashboard";
        	}
        else if(request.isUserInRole("ROLE_TEACHER")) {
            return "redirect:/teacherDashboard";
        }else
        	return "403Page";
       
    }
	
	
	

}
