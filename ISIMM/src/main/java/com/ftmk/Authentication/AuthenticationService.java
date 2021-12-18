package com.ftmk.Authentication;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ftmk.dao.UserDao;
import com.ftmk.model.UserInfo;

@Service
public class AuthenticationService implements UserDetailsService{
	
	@Autowired
	private UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserInfo user = userDao.findUser(username);
		
		 System.out.println("UserInfo= " + user);

	        if (user == null) {
	            throw new UsernameNotFoundException("User " + username + " was not found in the database");
	        }
	    
	        List<String> roles = userDao.getUserRoles(username);
	        
	        List<GrantedAuthority> grantList= new ArrayList<GrantedAuthority>();
	        int index=0;
	        if(roles!= null)  {
	            for(String role: roles)  {
	                // ROLE_USER, ROLE_ADMIN,..
	                GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+role);
	                grantList.add(authority);
	                System.out.println(grantList.get(index));
	                index++;
	            }
	        }        
	        
	        UserDetails userDetails = (UserDetails) new User(user.getUsername(), //
	                user.getPassword(),grantList);

	        return userDetails;
	}

}
