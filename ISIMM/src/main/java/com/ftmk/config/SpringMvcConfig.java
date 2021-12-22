package com.ftmk.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.ResourceBundleViewResolver;

import com.ftmk.dao.RegistrationDao;
import com.ftmk.dao.UserInfoDao;
import com.ftmk.dao.clerkDashboardDao;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan(basePackages = "com.ftmk")
public class SpringMvcConfig implements WebMvcConfigurer{
	
	@Bean
	public DataSource getDataSource() {

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/db_sis");
		dataSource.setUsername("root");
		dataSource.setPassword("abc123");

		return dataSource;
	}
	
	@Bean(name="JSPViewResolver")
	public ViewResolver getViewResolver() {

		
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setOrder(2);
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");

		return resolver;

	}
	
	@Bean(name="PDFResolver")
	public ViewResolver getResourceViewResolver() {

		
		ResourceBundleViewResolver resolver = new ResourceBundleViewResolver();
		resolver.setOrder(1);
		resolver.setBasename("pdf-view");
		
		return resolver;

	}
	
	@Autowired
	@Bean(name="transactionManager")
	public DataSourceTransactionManager getTransactionManager(DataSource dataSource) {
		
		DataSourceTransactionManager transactionManager=new DataSourceTransactionManager(dataSource);
		return transactionManager;
		
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry.addResourceHandler("/URL/**").addResourceLocations("/resources/");
		
	}
	
	@Bean
	public RegistrationDao register() {
		
		return new RegistrationDao(getDataSource());
	}
	
	@Bean
	public UserInfoDao userInfoDao() {
		
		return new UserInfoDao(getDataSource());
	}
	
	@Bean
	public clerkDashboardDao clerk() {
		
		return new clerkDashboardDao(getDataSource());
		
	}
	
	@Bean
	public JavaMailSender getMailSender() {
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
	
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		
		mailSender.setUsername("");
		mailSender.setPassword("");
		
		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.starttls.enable", "true");
		javaMailProperties.put("mail.smtp.auth", "true");
		javaMailProperties.put("mail.transport.protocol", "smtp");
		javaMailProperties.put("mail.debug", "true");
		
		mailSender.setJavaMailProperties(javaMailProperties);
		return mailSender;
	}
	
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
		
	}
	
	@Bean
	public MultipartResolver multipartResolver() {
		
		return new CommonsMultipartResolver();
		
	}

}
