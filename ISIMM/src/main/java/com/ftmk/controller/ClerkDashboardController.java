package com.ftmk.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ftmk.dao.clerkDashboardDao;
import com.ftmk.model.Announcement;
import com.ftmk.model.ClassParticipant;
import com.ftmk.model.Classroom;
import com.ftmk.model.UserPersonalDetails;
import com.ftmk.model.UserTableDisplay;

@Controller
public class ClerkDashboardController {
	
	@Autowired
	private clerkDashboardDao clerkDao;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@RequestMapping(value="/clerkDashboard")
	public ModelAndView clerkDahsboard(ModelAndView model) {
		
		int totalStudent=clerkDao.totalStudent();
		int unassignedStudentNum= clerkDao.countUnassignedStudent();
		int assignedStudentNum= totalStudent-unassignedStudentNum;
		
		int totalClassroom=clerkDao.totalClassroom();
		int emptyClassroomNum=clerkDao.emptyClassroomNum();
		List<Classroom> emptyClassroom = clerkDao.emptyClassroom();
		
		model.addObject("totalStudent",totalStudent);
		model.addObject("assignedStudentNum",assignedStudentNum);
		model.addObject("unassignedStudentNum",unassignedStudentNum);
		
		model.addObject("totalClassroom",totalClassroom);
		model.addObject("emptyClassroom",emptyClassroom);
		model.addObject("emptyClassroomNum", emptyClassroomNum);
		
		model.setViewName("clerkDashboard");
		return model;
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value={"/classroomPage","/classroomPage/{page}"},method=RequestMethod.GET)
	public ModelAndView classroomList(ModelAndView model,@PathVariable(required = false, name = "page") String page,
			HttpServletRequest request, HttpServletResponse response) {
		
		PagedListHolder<Classroom> list;
		List<Classroom> listClassroom;
		if (page == null) {

			list = new PagedListHolder<Classroom>();
			listClassroom = clerkDao.classroomList();
			list.setSource(listClassroom);
			list.setPageSize(5);
			request.getSession().setAttribute("classroom", list);

		} else if (page.equals("prev")) {

			list = (PagedListHolder<Classroom>) request.getSession().getAttribute("classroom");
			list.previousPage();

		} else if (page.equals("next")) {

			list = (PagedListHolder<Classroom>) request.getSession().getAttribute("classroom");
			list.nextPage();

		} else {

			int pageNum = Integer.parseInt(page);
			list = (PagedListHolder<Classroom>) request.getSession().getAttribute("classroom");
			list.setPage(pageNum - 1);

		}
		model.setViewName("classroomPage");
		return model;
		
		
	}
	
	@RequestMapping(value="/createClassroomForm")
	public ModelAndView createClassroomForm(ModelAndView model,Classroom classroom) {
		List<UserPersonalDetails> teacherList= clerkDao.teacherList();
		model.addObject("teacherList",teacherList);
		model.addObject("classroom", new Classroom());
		model.setViewName("createClassroom");
		return model;
	}
	
	
	@RequestMapping(value="createClassroom",method=RequestMethod.POST)
	public ModelAndView createClassroom(ModelAndView model, Classroom classroom) {
		
		String checkClassName=clerkDao.checkClassName(classroom.getClassName());
		
		if(checkClassName==null) {
			
		clerkDao.createClassroom(classroom);
		model.addObject("classroom",classroom);
		model.setViewName("redirect:/classroomPage");
		
		}else {
			model.addObject("classroom",classroom);
			model.addObject("message","Class name already exist");
			model.setViewName("createClassroom");
		}
		return model;
		
		
	}
	
	@RequestMapping(value="/editClassroom")
	public ModelAndView editClassroom(@RequestParam Integer classroomId) {
		
		Classroom classroom = clerkDao.getClassroomById(classroomId);
		List<UserPersonalDetails> teacherList= clerkDao.teacherList();
		
		ModelAndView model = new ModelAndView("editClassroomForm");
		
		model.addObject("classroom",classroom);
		model.addObject("teacherList",teacherList);
		return model;
		
	}

	
	@RequestMapping(value="/updateClassroom",method=RequestMethod.POST)
	public ModelAndView updateClassroom(ModelAndView model,@ModelAttribute("classroom") Classroom classroom) {
		
		String checkClassName=clerkDao.checkClassName(classroom.getClassName());
		String checkClassNameById=clerkDao.checkClassNameById(classroom.getClassroomId());
		
		if(checkClassName==null||checkClassNameById.equals(classroom.getClassName())) {
			
			clerkDao.updateClassroom(classroom);
			model.addObject("classroom",classroom);
			model.setViewName("redirect:/classroomPage");
			
		}else {
			
			List<UserPersonalDetails> teacherList= clerkDao.teacherList();
			model.addObject("message","Class name already exist");
			model.addObject("teacherList",teacherList);
			model.setViewName("editClassroomForm");
			
		}
		return model;
		
	}
	
	@RequestMapping(value="/deleteClassroom")
	public ModelAndView deleteClassroom(@RequestParam Integer classroomId) {
		
		
		clerkDao.deleteClassroom(classroomId);

		return new ModelAndView("redirect:/classroomPage");
		
	}
	
	@RequestMapping(value="/manageParticipant",method=RequestMethod.GET)
	public ModelAndView manageParticipant(@RequestParam Integer classroomId) {
		
		Classroom classroom = clerkDao.getClassroomById(classroomId);
		List<UserPersonalDetails> studentList = clerkDao.unassignedStudentList();
		List<UserPersonalDetails> participantList=clerkDao.participantList(classroomId);
		int studentCount=classroom.getMaxParticipant()-clerkDao.getStudentCount(classroomId);
		
		ModelAndView model = new ModelAndView("manageParticipantForm");
		model.addObject("classParticipant",new ClassParticipant());
		model.addObject("classroom",classroom);
		model.addObject("participant", participantList);
		model.addObject("studentList",studentList);
		model.addObject("studentCount",studentCount);
		return model;
		
	}
	
	@RequestMapping(value="/addStudent",method=RequestMethod.POST)
	public ModelAndView addStudent(ModelAndView model,@RequestParam Integer classroomId,ClassParticipant participant) {
		
		clerkDao.addStudentToClass(classroomId, participant.getUserId());
		model.setViewName("redirect:/manageParticipant?classroomId="+classroomId);
		return model;
	}
	
	@RequestMapping(value="/removeStudent",method=RequestMethod.GET)
	public ModelAndView removeStudent(ModelAndView model,@RequestParam Integer userId) {
		
		Integer classroomId=clerkDao.getParticipantClassId(userId);
		clerkDao.removeParticipant(userId);
		
		model.setViewName("redirect:/manageParticipant?classroomId="+classroomId);
		return model;
		
	}
	
	@RequestMapping(value="/searchClassroom",method=RequestMethod.GET)
	public String searchClassroom() {
		
		return "searchClassroom";
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/searchClassName","/searchClassName/{page}"}, method = RequestMethod.GET)
	public ModelAndView searchByName(ModelAndView model, @RequestParam("searchClassName") String search,
			@PathVariable(required = false, name = "page") String page,
			HttpServletRequest request, HttpServletResponse response) {

		List<Classroom> nameSearchResult = clerkDao.searchClassroomByName(search);

		if (nameSearchResult.isEmpty()|| search.isBlank()) {

			model.addObject("Message", "No Result Found");
			model.setViewName("noClassResultFound");

		} else {

			PagedListHolder<Classroom> List;
			if (page == null) {

				List = new PagedListHolder<Classroom>();
				List.setSource(nameSearchResult);
				List.setPageSize(10);
				request.getSession().setAttribute("classroom", List);

			} else if (page.equals("prev")) {

				List = (PagedListHolder<Classroom>) request.getSession().getAttribute("classroom");
				List.previousPage();

			} else if (page.equals("next")) {

				List = (PagedListHolder<Classroom>) request.getSession().getAttribute("classroom");
				List.nextPage();

			} else {

				int pageNum = Integer.parseInt(page);
				List = (PagedListHolder<Classroom>) request.getSession().getAttribute("classroom");
				List.setPage(pageNum - 1);

			}
			String encode=URLEncoder.encode(search);
			model.addObject("search",encode);
			model.setViewName("searchClassNameResult");
		}
		return model;

	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/searchTeacherName","/searchTeacherName/{page}"}, method = RequestMethod.GET)
	public ModelAndView searchByTeacherName(ModelAndView model, @RequestParam("searchTeacherName") String search,
			@PathVariable(required = false, name = "page") String page,
			HttpServletRequest request, HttpServletResponse response) {

		List<Classroom> nameSearchResult = clerkDao.searchClassroomByTeacherName(search);

		if (nameSearchResult.isEmpty()|| search.isBlank()) {

			model.addObject("Message", "No Result Found");
			model.setViewName("noClassResultFound");

		} else {

			PagedListHolder<Classroom> List;
			if (page == null) {

				List = new PagedListHolder<Classroom>();
				List.setSource(nameSearchResult);
				List.setPageSize(10);
				request.getSession().setAttribute("classroom", List);

			} else if (page.equals("prev")) {

				List = (PagedListHolder<Classroom>) request.getSession().getAttribute("classroom");
				List.previousPage();

			} else if (page.equals("next")) {

				List = (PagedListHolder<Classroom>) request.getSession().getAttribute("classroom");
				List.nextPage();

			} else {

				int pageNum = Integer.parseInt(page);
				List = (PagedListHolder<Classroom>) request.getSession().getAttribute("classroom");
				List.setPage(pageNum - 1);

			}
			String encode=URLEncoder.encode(search);
			model.addObject("search",encode);
			model.setViewName("searchTeacherNameResult");
		}
		return model;

	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/searchClassForm","/searchClassForm/{page}"}, method = RequestMethod.GET)
	public ModelAndView searchByForm(ModelAndView model, @RequestParam("searchClassForm") String search,
			@PathVariable(required = false, name = "page") String page,
			HttpServletRequest request, HttpServletResponse response) {

		List<Classroom> nameSearchResult = clerkDao.searchClassroomByForm(search);

		if (nameSearchResult.isEmpty()|| search.isBlank()) {

			model.addObject("Message", "No Result Found");
			model.setViewName("noClassResultFound");

		} else {

			PagedListHolder<Classroom> List;
			if (page == null) {

				List = new PagedListHolder<Classroom>();
				List.setSource(nameSearchResult);
				List.setPageSize(10);
				request.getSession().setAttribute("classroom", List);

			} else if (page.equals("prev")) {

				List = (PagedListHolder<Classroom>) request.getSession().getAttribute("classroom");
				List.previousPage();

			} else if (page.equals("next")) {

				List = (PagedListHolder<Classroom>) request.getSession().getAttribute("classroom");
				List.nextPage();

			} else {

				int pageNum = Integer.parseInt(page);
				List = (PagedListHolder<Classroom>) request.getSession().getAttribute("classroom");
				List.setPage(pageNum - 1);

			}
			String encode=URLEncoder.encode(search);
			model.addObject("search",encode);
			model.setViewName("searchClassFormResult");
		}
		return model;

	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value={"/announcementPage","/announcementPage/{page}"},method=RequestMethod.GET)
	public ModelAndView announcementList(ModelAndView model,@PathVariable(required = false, name = "page") String page,
			HttpServletRequest request, HttpServletResponse response) {
		
		PagedListHolder<Announcement> list;
		List<Announcement> announcementList;
		if (page == null) {

			list = new PagedListHolder<Announcement>();
			announcementList = clerkDao.announcementList();
			list.setSource(announcementList);
			list.setPageSize(5);
			request.getSession().setAttribute("announcement", list);

		} else if (page.equals("prev")) {

			list = (PagedListHolder<Announcement>) request.getSession().getAttribute("announcement");
			list.previousPage();

		} else if (page.equals("next")) {

			list = (PagedListHolder<Announcement>) request.getSession().getAttribute("announcement");
			list.nextPage();

		} else {

			int pageNum = Integer.parseInt(page);
			list = (PagedListHolder<Announcement>) request.getSession().getAttribute("announcement");
			list.setPage(pageNum - 1);

		}
		model.setViewName("announcementPage");
		return model;
		
		
	}
	
	@RequestMapping(value="/createAnnouncementForm")
	public ModelAndView createAnnouncementForm(ModelAndView model,Announcement announcement) {
		
		model.addObject("announcement", new Announcement());
		model.setViewName("createAnnouncement");
		return model;
	}
	
	
	@RequestMapping(value="/createAnnouncement")
	public ModelAndView createAnnouncement(ModelAndView model, Announcement announcement,Principal principal) {
		
		Integer userId = clerkDao.getUserIdByUsername(principal.getName());
		clerkDao.createAnnouncement(announcement, userId);
		model.setViewName("redirect:/announcementPage");
		return model;
	}
	
	@RequestMapping(value="/editAnnouncement",method=RequestMethod.GET)
	public ModelAndView editAnnouncement(@RequestParam Integer announcementId) {
		
		Announcement announcement = clerkDao.getAnnouncementById(announcementId);
		ModelAndView model = new ModelAndView("editAnnouncementForm");
		model.addObject("announcement",announcement);
		return model;
		
	}
	
	@RequestMapping(value="/udpateAnnouncement",method=RequestMethod.POST)
	public ModelAndView updateAnnouncement(ModelAndView model, @ModelAttribute("announcement") Announcement announcement,Principal principal) {
		
		Integer userId = clerkDao.getUserIdByUsername(principal.getName());
		clerkDao.udpateAnnouncement(announcement, userId);
		model.addObject("announcement",announcement);
		model.setViewName("redirect:/announcementPage");
		return model;
		
	}

	@RequestMapping(value="/deleteAnnouncement")
	public ModelAndView deleteAnnouncement(@RequestParam Integer announcementId) {
		
		
		clerkDao.deleteAnnouncement(announcementId);

		return new ModelAndView("redirect:/announcementPage");
		
	}
	
	@RequestMapping(value="/sendAnnouncement",method = RequestMethod.GET)
	public ModelAndView sendAnnouncement(@RequestParam Integer announcementId) {
		
		
		Announcement announcement= clerkDao.getAnnouncementById(announcementId);
		List<String> emails=clerkDao.emails();
		StringBuilder sb = new StringBuilder();
		
		int i =0;
		for (String email:emails) {
			
			sb.append(email);
			i++;
			if(emails.size()>i) {
				
				sb.append(",");
			}
		}
		String combinedEmail=sb.toString();
		
		MimeMessagePreparator message=new MimeMessagePreparator() {
			
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true, "UTF-8");
				helper.setTo(InternetAddress.parse(combinedEmail));
				helper.setSubject(announcement.getTitle());
				helper.setText(announcement.getDescription());
				
				
			}
		};
		mailSender.send(message);
		return new ModelAndView("redirect:/announcementPage");
	}
	
	
	@RequestMapping(value="/searchAnnouncement",method=RequestMethod.GET)
	public String searchAnnouncement() {
		
		return "searchAnnouncement";
		
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/searchTitle","/searchTitle/{page}"}, method = RequestMethod.GET)
	public ModelAndView searchByTitle(ModelAndView model, @RequestParam("searchTitle") String search,
			@PathVariable(required = false, name = "page") String page,
			HttpServletRequest request, HttpServletResponse response) {

		List<Announcement> titleSearchResult = clerkDao.searchByTitle(search);

		if (titleSearchResult.isEmpty()|| search.isBlank()) {

			model.addObject("Message", "No Result Found");
			model.setViewName("noClassResultFound");

		} else {

			PagedListHolder<Announcement> List;
			if (page == null) {

				List = new PagedListHolder<Announcement>();
				List.setSource(titleSearchResult);
				List.setPageSize(5);
				request.getSession().setAttribute("announcement", List);

			} else if (page.equals("prev")) {

				List = (PagedListHolder<Announcement>) request.getSession().getAttribute("announcement");
				List.previousPage();

			} else if (page.equals("next")) {

				List = (PagedListHolder<Announcement>) request.getSession().getAttribute("announcement");
				List.nextPage();

			} else {

				int pageNum = Integer.parseInt(page);
				List = (PagedListHolder<Announcement>) request.getSession().getAttribute("announcement");
				List.setPage(pageNum - 1);

			}
			String encode=URLEncoder.encode(search);
			model.addObject("search",encode);
			model.setViewName("searchTitleResult");
		}
		return model;

	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/searchCreator","/searchCreator/{page}"}, method = RequestMethod.GET)
	public ModelAndView searchByCreator(ModelAndView model, @RequestParam("searchCreator") String search,
			@PathVariable(required = false, name = "page") String page,
			HttpServletRequest request, HttpServletResponse response) {

		List<Announcement> creatorSearchResult = clerkDao.searchByCreator(search);

		if (creatorSearchResult.isEmpty()|| search.isBlank()) {

			model.addObject("Message", "No Result Found");
			model.setViewName("noClassResultFound");

		} else {

			PagedListHolder<Announcement> List;
			if (page == null) {

				List = new PagedListHolder<Announcement>();
				List.setSource(creatorSearchResult);
				List.setPageSize(1);
				request.getSession().setAttribute("announcement", List);

			} else if (page.equals("prev")) {

				List = (PagedListHolder<Announcement>) request.getSession().getAttribute("announcement");
				List.previousPage();

			} else if (page.equals("next")) {

				List = (PagedListHolder<Announcement>) request.getSession().getAttribute("announcement");
				List.nextPage();

			} else {

				int pageNum = Integer.parseInt(page);
				List = (PagedListHolder<Announcement>) request.getSession().getAttribute("announcement");
				List.setPage(pageNum - 1);

			}
			String encode=URLEncoder.encode(search);
			model.addObject("search",encode);
			model.setViewName("searchCreatorResult");
		}
		return model;

	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/searchDateCreated","/searchDateCreated/{page}"}, method = RequestMethod.GET)
	public ModelAndView searchByDateCreated(ModelAndView model, @RequestParam("searchDateCreated") String search,
			@PathVariable(required = false, name = "page") String page,
			HttpServletRequest request, HttpServletResponse response) {

		List<Announcement> dateSearchResult = clerkDao.searchByDateCreated(search);

		if (dateSearchResult.isEmpty()|| search.isBlank()) {

			model.addObject("Message", "No Result Found");
			model.setViewName("noClassResultFound");

		} else {

			PagedListHolder<Announcement> List;
			if (page == null) {

				List = new PagedListHolder<Announcement>();
				List.setSource(dateSearchResult);
				List.setPageSize(1);
				request.getSession().setAttribute("announcement", List);

			} else if (page.equals("prev")) {

				List = (PagedListHolder<Announcement>) request.getSession().getAttribute("announcement");
				List.previousPage();

			} else if (page.equals("next")) {

				List = (PagedListHolder<Announcement>) request.getSession().getAttribute("announcement");
				List.nextPage();

			} else {

				int pageNum = Integer.parseInt(page);
				List = (PagedListHolder<Announcement>) request.getSession().getAttribute("announcement");
				List.setPage(pageNum - 1);

			}
			String encode=URLEncoder.encode(search);
			model.addObject("search",encode);
			model.setViewName("searchDateResult");
		}
		return model;

	}

	
}
