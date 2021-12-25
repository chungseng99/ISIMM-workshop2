package com.ftmk.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.Principal;
import java.sql.Blob;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ftmk.dao.clerkDashboardDao;
import com.ftmk.model.Announcement;
import com.ftmk.model.Attendance;
import com.ftmk.model.ClassParticipant;
import com.ftmk.model.Classroom;
import com.ftmk.model.Fee;
import com.ftmk.model.Payment;
import com.ftmk.model.ReportCard;
import com.ftmk.model.Subject;
import com.ftmk.model.UserPersonalDetails;
import com.ftmk.model.UserTableDisplay;

@Controller
public class ClerkDashboardController {

	@Autowired
	private clerkDashboardDao clerkDao;

	@Autowired
	private JavaMailSender mailSender;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/clerkDashboard", "/clerkDashboard/{page}" }, method = RequestMethod.GET)
	public ModelAndView clerkDahsboard(ModelAndView model, @PathVariable(required = false, name = "page") String page,
			HttpServletRequest request, HttpServletResponse response) {

		int totalStudent = clerkDao.totalStudent();
		int unassignedStudentNum = clerkDao.countUnassignedStudent();
		int assignedStudentNum = totalStudent - unassignedStudentNum;
		List<UserPersonalDetails> unassignedStudentList;

		int totalClassroom = clerkDao.totalClassroom();
		int emptyClassroomNum = clerkDao.emptyClassroomNum();
		List<Classroom> emptyClassroom = clerkDao.emptyClassroom();

		Double totalPaymentReceived = clerkDao.totalPaymentReceived();
		int numberOfAcceptedStatus = clerkDao.numberOfAcceptedStatus();
		int numberOfPendingStatus = clerkDao.numberOfPendingStatus();
		int numberOfRejectedStatus = clerkDao.numberOfRejectedStatus();

		PagedListHolder<UserPersonalDetails> list;

		if (page == null) {

			list = new PagedListHolder<UserPersonalDetails>();
			unassignedStudentList = clerkDao.unassignedStudentList();
			list.setSource(unassignedStudentList);
			list.setPageSize(5);
			request.getSession().setAttribute("student", list);

		} else if (page.equals("prev")) {

			list = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("student");
			list.previousPage();

		} else if (page.equals("next")) {

			list = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("student");
			list.nextPage();

		} else {

			int pageNum = Integer.parseInt(page);
			list = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("student");
			list.setPage(pageNum - 1);

		}

		model.addObject("totalStudent", totalStudent);
		model.addObject("assignedStudentNum", assignedStudentNum);
		model.addObject("unassignedStudentNum", unassignedStudentNum);

		model.addObject("totalClassroom", totalClassroom);
		model.addObject("emptyClassroom", emptyClassroom);
		model.addObject("emptyClassroomNum", emptyClassroomNum);

		model.addObject("totalPayment", totalPaymentReceived);
		model.addObject("accepted", numberOfAcceptedStatus);
		model.addObject("pending", numberOfPendingStatus);
		model.addObject("rejected", numberOfRejectedStatus);

		model.setViewName("clerkDashboard");
		return model;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/classroomPage", "/classroomPage/{page}" }, method = RequestMethod.GET)
	public ModelAndView classroomList(ModelAndView model, @PathVariable(required = false, name = "page") String page,
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

	@RequestMapping(value = "/createClassroomForm")
	public ModelAndView createClassroomForm(ModelAndView model, Classroom classroom) {
		List<UserPersonalDetails> teacherList = clerkDao.teacherList();
		model.addObject("teacherList", teacherList);
		model.addObject("classroom", new Classroom());
		model.setViewName("createClassroom");
		return model;
	}

	@RequestMapping(value = "createClassroom", method = RequestMethod.POST)
	public ModelAndView createClassroom(ModelAndView model, Classroom classroom) {

		String checkClassName = clerkDao.checkClassName(classroom.getClassName());

		if (checkClassName == null) {

			clerkDao.createClassroom(classroom);
			model.addObject("classroom", classroom);
			model.setViewName("redirect:/classroomPage");

		} else {
			model.addObject("classroom", classroom);
			model.addObject("message", "Class name already exist");
			model.setViewName("createClassroom");
		}
		return model;

	}

	@RequestMapping(value = "/editClassroom")
	public ModelAndView editClassroom(@RequestParam Integer classroomId) {

		Classroom classroom = clerkDao.getClassroomById(classroomId);
		List<UserPersonalDetails> teacherList = clerkDao.teacherList();

		ModelAndView model = new ModelAndView("editClassroomForm");

		model.addObject("classroom", classroom);
		model.addObject("teacherList", teacherList);
		return model;

	}

	@RequestMapping(value = "/updateClassroom", method = RequestMethod.POST)
	public ModelAndView updateClassroom(ModelAndView model, @ModelAttribute("classroom") Classroom classroom) {

		String checkClassName = clerkDao.checkClassName(classroom.getClassName());
		String checkClassNameById = clerkDao.checkClassNameById(classroom.getClassroomId());

		if (checkClassName == null || checkClassNameById.equals(classroom.getClassName())) {

			clerkDao.updateClassroom(classroom);
			model.addObject("classroom", classroom);
			model.setViewName("redirect:/classroomPage");

		} else {

			List<UserPersonalDetails> teacherList = clerkDao.teacherList();
			model.addObject("message", "Class name already exist");
			model.addObject("teacherList", teacherList);
			model.setViewName("editClassroomForm");

		}
		return model;

	}

	@RequestMapping(value = "/deleteClassroom")
	public ModelAndView deleteClassroom(@RequestParam Integer classroomId) {

		clerkDao.deleteClassroom(classroomId);

		return new ModelAndView("redirect:/classroomPage");

	}

	@RequestMapping(value = "/manageParticipant", method = RequestMethod.GET)
	public ModelAndView manageParticipant(@RequestParam Integer classroomId) {

		Classroom classroom = clerkDao.getClassroomById(classroomId);
		List<UserPersonalDetails> studentList = clerkDao.unassignedStudentList();
		List<UserPersonalDetails> participantList = clerkDao.participantList(classroomId);

		ModelAndView model = new ModelAndView("manageParticipantForm");
		model.addObject("classParticipant", new ClassParticipant());
		model.addObject("classroom", classroom);
		model.addObject("participant", participantList);
		model.addObject("studentList", studentList);
		return model;

	}

	@RequestMapping(value = "/addStudent", method = RequestMethod.POST)
	public ModelAndView addStudent(ModelAndView model, @RequestParam Integer classroomId,
			ClassParticipant participant) {

		clerkDao.addStudentToClass(classroomId, participant.getUserId());
		model.setViewName("redirect:/manageParticipant?classroomId=" + classroomId);
		return model;
	}

	@RequestMapping(value = "/removeStudent", method = RequestMethod.GET)
	public ModelAndView removeStudent(ModelAndView model, @RequestParam Integer userId) {

		Integer classroomId = clerkDao.getParticipantClassId(userId);
		clerkDao.removeParticipant(userId);

		model.setViewName("redirect:/manageParticipant?classroomId=" + classroomId);
		return model;

	}

	@RequestMapping(value = "/searchClassroom", method = RequestMethod.GET)
	public String searchClassroom() {

		return "searchClassroom";

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/searchClassName", "/searchClassName/{page}" }, method = RequestMethod.GET)
	public ModelAndView searchByName(ModelAndView model, @RequestParam("searchClassName") String search,
			@PathVariable(required = false, name = "page") String page, HttpServletRequest request,
			HttpServletResponse response) {

		List<Classroom> nameSearchResult = clerkDao.searchClassroomByName(search);

		if (nameSearchResult.isEmpty() || search.isBlank()) {

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
			String encode = URLEncoder.encode(search);
			model.addObject("search", encode);
			model.setViewName("searchClassNameResult");
		}
		return model;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/searchTeacherName", "/searchTeacherName/{page}" }, method = RequestMethod.GET)
	public ModelAndView searchByTeacherName(ModelAndView model, @RequestParam("searchTeacherName") String search,
			@PathVariable(required = false, name = "page") String page, HttpServletRequest request,
			HttpServletResponse response) {

		List<Classroom> nameSearchResult = clerkDao.searchClassroomByTeacherName(search);

		if (nameSearchResult.isEmpty() || search.isBlank()) {

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
			String encode = URLEncoder.encode(search);
			model.addObject("search", encode);
			model.setViewName("searchTeacherNameResult");
		}
		return model;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/searchClassForm", "/searchClassForm/{page}" }, method = RequestMethod.GET)
	public ModelAndView searchByForm(ModelAndView model, @RequestParam("searchClassForm") String search,
			@PathVariable(required = false, name = "page") String page, HttpServletRequest request,
			HttpServletResponse response) {

		List<Classroom> nameSearchResult = clerkDao.searchClassroomByForm(search);

		if (nameSearchResult.isEmpty() || search.isBlank()) {

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
			String encode = URLEncoder.encode(search);
			model.addObject("search", encode);
			model.setViewName("searchClassFormResult");
		}
		return model;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/announcementPage", "/announcementPage/{page}" }, method = RequestMethod.GET)
	public ModelAndView announcementList(ModelAndView model, @PathVariable(required = false, name = "page") String page,
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

	@RequestMapping(value = "/createAnnouncementForm")
	public ModelAndView createAnnouncementForm(ModelAndView model, Announcement announcement) {

		model.addObject("announcement", new Announcement());
		model.setViewName("createAnnouncement");
		return model;
	}

	@RequestMapping(value = "/createAnnouncement")
	public ModelAndView createAnnouncement(ModelAndView model, Announcement announcement, Principal principal) {

		Integer userId = clerkDao.getUserIdByUsername(principal.getName());
		clerkDao.createAnnouncement(announcement, userId);
		model.setViewName("redirect:/announcementPage");
		return model;
	}

	@RequestMapping(value = "/editAnnouncement", method = RequestMethod.GET)
	public ModelAndView editAnnouncement(@RequestParam Integer announcementId) {

		Announcement announcement = clerkDao.getAnnouncementById(announcementId);
		ModelAndView model = new ModelAndView("editAnnouncementForm");
		model.addObject("announcement", announcement);
		return model;

	}

	@RequestMapping(value = "/udpateAnnouncement", method = RequestMethod.POST)
	public ModelAndView updateAnnouncement(ModelAndView model,
			@ModelAttribute("announcement") Announcement announcement, Principal principal) {

		Integer userId = clerkDao.getUserIdByUsername(principal.getName());
		clerkDao.udpateAnnouncement(announcement, userId);
		model.addObject("announcement", announcement);
		model.setViewName("redirect:/announcementPage");
		return model;

	}

	@RequestMapping(value = "/deleteAnnouncement", method = RequestMethod.GET)
	public ModelAndView deleteAnnouncement(@RequestParam Integer announcementId) {

		clerkDao.deleteAnnouncement(announcementId);

		return new ModelAndView("redirect:/announcementPage");

	}

	@RequestMapping(value = "/sendAnnouncement", method = RequestMethod.GET)
	public ModelAndView sendAnnouncement(@RequestParam Integer announcementId) {

		Announcement announcement = clerkDao.getAnnouncementById(announcementId);
		List<String> emails = clerkDao.emails();
		StringBuilder sb = new StringBuilder();

		int i = 0;
		for (String email : emails) {

			sb.append(email);
			i++;
			if (emails.size() > i) {

				sb.append(",");
			}
		}
		String combinedEmail = sb.toString();

		MimeMessagePreparator message = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {

				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
				helper.setTo(InternetAddress.parse(combinedEmail));
				helper.setSubject(announcement.getTitle());
				helper.setText("Integrated School Information Management and Monitoring System\n"+ 
				"________________________________________________________________________________\n\n"
				+announcement.getDescription());

			}
		};
		mailSender.send(message);
		return new ModelAndView("redirect:/announcementPage");
	}

	@RequestMapping(value = "/searchAnnouncement", method = RequestMethod.GET)
	public String searchAnnouncement() {

		return "searchAnnouncement";

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/searchTitle", "/searchTitle/{page}" }, method = RequestMethod.GET)
	public ModelAndView searchByTitle(ModelAndView model, @RequestParam("searchTitle") String search,
			@PathVariable(required = false, name = "page") String page, HttpServletRequest request,
			HttpServletResponse response) {

		List<Announcement> titleSearchResult = clerkDao.searchByTitle(search);

		if (titleSearchResult.isEmpty() || search.isBlank()) {

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
			String encode = URLEncoder.encode(search);
			model.addObject("search", encode);
			model.setViewName("searchTitleResult");
		}
		return model;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/searchCreator", "/searchCreator/{page}" }, method = RequestMethod.GET)
	public ModelAndView searchByCreator(ModelAndView model, @RequestParam("searchCreator") String search,
			@PathVariable(required = false, name = "page") String page, HttpServletRequest request,
			HttpServletResponse response) {

		List<Announcement> creatorSearchResult = clerkDao.searchByCreator(search);

		if (creatorSearchResult.isEmpty() || search.isBlank()) {

			model.addObject("Message", "No Result Found");
			model.setViewName("noClassResultFound");

		} else {

			PagedListHolder<Announcement> List;
			if (page == null) {

				List = new PagedListHolder<Announcement>();
				List.setSource(creatorSearchResult);
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
			String encode = URLEncoder.encode(search);
			model.addObject("search", encode);
			model.setViewName("searchCreatorResult");
		}
		return model;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/searchDateCreated", "/searchDateCreated/{page}" }, method = RequestMethod.GET)
	public ModelAndView searchByDateCreated(ModelAndView model, @RequestParam("searchDateCreated") String search,
			@PathVariable(required = false, name = "page") String page, HttpServletRequest request,
			HttpServletResponse response) {

		List<Announcement> dateSearchResult = clerkDao.searchByDateCreated(search);

		if (dateSearchResult.isEmpty() || search.isBlank()) {

			model.addObject("Message", "No Result Found");
			model.setViewName("noClassResultFound");

		} else {

			PagedListHolder<Announcement> List;
			if (page == null) {

				List = new PagedListHolder<Announcement>();
				List.setSource(dateSearchResult);
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
			String encode = URLEncoder.encode(search);
			model.addObject("search", encode);
			model.setViewName("searchDateResult");
		}
		return model;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/feePage", "/feePage/{page}" }, method = RequestMethod.GET)
	public ModelAndView feePage(ModelAndView model, @PathVariable(required = false, name = "page") String page,
			HttpServletRequest request, HttpServletResponse response) {

		PagedListHolder<Fee> list;
		List<Fee> feeList;
		if (page == null) {

			list = new PagedListHolder<Fee>();
			feeList = clerkDao.feeList();
			list.setSource(feeList);
			list.setPageSize(5);
			request.getSession().setAttribute("fee", list);

		} else if (page.equals("prev")) {

			list = (PagedListHolder<Fee>) request.getSession().getAttribute("fee");
			list.previousPage();

		} else if (page.equals("next")) {

			list = (PagedListHolder<Fee>) request.getSession().getAttribute("fee");
			list.nextPage();

		} else {

			int pageNum = Integer.parseInt(page);
			list = (PagedListHolder<Fee>) request.getSession().getAttribute("fee");
			list.setPage(pageNum - 1);

		}
		model.setViewName("feePage");
		return model;
	}

	@RequestMapping(value = "/createFeeForm")
	public String createFeeForm() {

		return "createFeeForm";
	}

	@RequestMapping(value = "/createFee", method = RequestMethod.POST)
	public ModelAndView createFee(ModelAndView model, Fee fee, Principal principal) {

		int userId = clerkDao.getUserIdByUsername(principal.getName());
		clerkDao.createFee(fee, userId);
		model.setViewName("redirect:/feePage");
		return model;

	}

	@RequestMapping(value = "/editFee", method = RequestMethod.GET)
	public ModelAndView editFee(@RequestParam Integer feeId) {

		Fee fee = clerkDao.getFeeById(feeId);
		ModelAndView model = new ModelAndView("editFeeForm");
		model.addObject("fee", fee);
		return model;

	}

	@RequestMapping(value = "/updateFee", method = RequestMethod.POST)
	public ModelAndView updateFee(ModelAndView model, @ModelAttribute(name = "fee") Fee fee, Principal principal) {

		int userId = clerkDao.getUserIdByUsername(principal.getName());
		clerkDao.updateFee(fee, userId);
		model.addObject("fee", fee);
		model.setViewName("redirect:/feePage");
		return model;

	}

	@RequestMapping(value = "/deleteFee", method = RequestMethod.GET)
	public ModelAndView deleteFee(@RequestParam Integer feeId) {

		clerkDao.deleteFee(feeId);

		return new ModelAndView("redirect:/feePage");

	}

	@RequestMapping(value = "/searchFee", method = RequestMethod.GET)
	public String searchFee() {

		return "searchFee";

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/searchFeeTitle", "/searchFeeTitle/{page}" }, method = RequestMethod.GET)
	public ModelAndView searchFeeTitle(ModelAndView model, @RequestParam("searchFeeTitle") String search,
			@PathVariable(required = false, name = "page") String page, HttpServletRequest request,
			HttpServletResponse response) {

		List<Fee> titleSearchResult = clerkDao.searchByFeeTitle(search);

		if (titleSearchResult.isEmpty() || search.isBlank()) {

			model.addObject("Message", "No Result Found");
			model.setViewName("noClassResultFound");

		} else {

			PagedListHolder<Fee> List;
			if (page == null) {

				List = new PagedListHolder<Fee>();
				List.setSource(titleSearchResult);
				List.setPageSize(5);
				request.getSession().setAttribute("fee", List);

			} else if (page.equals("prev")) {

				List = (PagedListHolder<Fee>) request.getSession().getAttribute("fee");
				List.previousPage();

			} else if (page.equals("next")) {

				List = (PagedListHolder<Fee>) request.getSession().getAttribute("fee");
				List.nextPage();

			} else {

				int pageNum = Integer.parseInt(page);
				List = (PagedListHolder<Fee>) request.getSession().getAttribute("fee");
				List.setPage(pageNum - 1);

			}
			String encode = URLEncoder.encode(search);
			model.addObject("search", encode);
			model.setViewName("searchFeeTitleResult");
		}
		return model;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/searchFeeDate", "/searchFeeDate/{page}" }, method = RequestMethod.GET)
	public ModelAndView searchFeeDate(ModelAndView model, @RequestParam("searchFeeDate") String search,
			@PathVariable(required = false, name = "page") String page, HttpServletRequest request,
			HttpServletResponse response) {

		List<Fee> dateSearchResult = clerkDao.searchByFeeDate(search);

		if (dateSearchResult.isEmpty() || search.isBlank()) {

			model.addObject("Message", "No Result Found");
			model.setViewName("noClassResultFound");

		} else {

			PagedListHolder<Fee> List;
			if (page == null) {

				List = new PagedListHolder<Fee>();
				List.setSource(dateSearchResult);
				List.setPageSize(5);
				request.getSession().setAttribute("fee", List);

			} else if (page.equals("prev")) {

				List = (PagedListHolder<Fee>) request.getSession().getAttribute("fee");
				List.previousPage();

			} else if (page.equals("next")) {

				List = (PagedListHolder<Fee>) request.getSession().getAttribute("fee");
				List.nextPage();

			} else {

				int pageNum = Integer.parseInt(page);
				List = (PagedListHolder<Fee>) request.getSession().getAttribute("fee");
				List.setPage(pageNum - 1);

			}
			String encode = URLEncoder.encode(search);
			model.addObject("search", encode);
			model.setViewName("searchFeeDateResult");
		}
		return model;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/searchFeeCreator", "/searchFeeCreator/{page}" }, method = RequestMethod.GET)
	public ModelAndView searchFeeCreator(ModelAndView model, @RequestParam("searchFeeCreator") String search,
			@PathVariable(required = false, name = "page") String page, HttpServletRequest request,
			HttpServletResponse response) {

		List<Fee> creatorSearchResult = clerkDao.searchByFeeCreator(search);

		if (creatorSearchResult.isEmpty() || search.isBlank()) {

			model.addObject("Message", "No Result Found");
			model.setViewName("noClassResultFound");

		} else {

			PagedListHolder<Fee> List;
			if (page == null) {

				List = new PagedListHolder<Fee>();
				List.setSource(creatorSearchResult);
				List.setPageSize(5);
				request.getSession().setAttribute("fee", List);

			} else if (page.equals("prev")) {

				List = (PagedListHolder<Fee>) request.getSession().getAttribute("fee");
				List.previousPage();

			} else if (page.equals("next")) {

				List = (PagedListHolder<Fee>) request.getSession().getAttribute("fee");
				List.nextPage();

			} else {

				int pageNum = Integer.parseInt(page);
				List = (PagedListHolder<Fee>) request.getSession().getAttribute("fee");
				List.setPage(pageNum - 1);

			}
			String encode = URLEncoder.encode(search);
			model.addObject("search", encode);
			model.setViewName("searchFeeCreatorResult");
		}
		return model;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/studentPage", "/studentPage/{page}" })
	public ModelAndView studentPage(ModelAndView model, @PathVariable(required = false, name = "page") String page,
			HttpServletRequest request, HttpServletResponse responser) {

		PagedListHolder<UserPersonalDetails> list;
		List<UserPersonalDetails> studentList;
		if (page == null) {

			list = new PagedListHolder<UserPersonalDetails>();
			studentList = clerkDao.StudentList();
			list.setSource(studentList);
			list.setPageSize(5);
			request.getSession().setAttribute("student", list);

		} else if (page.equals("prev")) {

			list = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("student");
			list.previousPage();

		} else if (page.equals("next")) {

			list = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("student");
			list.nextPage();

		} else {

			int pageNum = Integer.parseInt(page);
			list = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("student");
			list.setPage(pageNum - 1);

		}
		model.setViewName("studentPage");
		return model;

	}

	@RequestMapping(value = "/viewStudent", method = RequestMethod.GET)
	public ModelAndView viewStudent(@RequestParam Integer userId) {

		UserPersonalDetails user = clerkDao.getStudentById(userId);
		ModelAndView model = new ModelAndView("viewStudentForm");
		model.addObject("student", user);
		return model;

	}

	@RequestMapping(value = "/getStudentPhoto/{userId}")
	public void getStudentPhoto(HttpServletResponse response, @PathVariable("userId") int userId) throws Exception {
		response.setContentType("image/jpeg, image/jpg, image/png");

		Blob photo = clerkDao.getPhotoById(userId);

		byte[] bytes = photo.getBytes(1, (int) photo.length());
		InputStream inputStream = new ByteArrayInputStream(bytes);
		IOUtils.copy(inputStream, response.getOutputStream());
	}

	@RequestMapping(value = "/searchStudent", method = RequestMethod.GET)
	public String searchStudent() {

		return "searchStudent";

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/searchStudentName", "/searchStudentName/{page}" }, method = RequestMethod.GET)
	public ModelAndView searchStudentName(ModelAndView model, @RequestParam("searchStudentName") String search,
			@PathVariable(required = false, name = "page") String page, HttpServletRequest request,
			HttpServletResponse response) {

		List<UserPersonalDetails> nameSearchResult = clerkDao.searchStudentByName(search);

		if (nameSearchResult.isEmpty() || search.isBlank()) {

			model.addObject("Message", "No Result Found");
			model.setViewName("noClassResultFound");

		} else {

			PagedListHolder<UserPersonalDetails> List;
			if (page == null) {

				List = new PagedListHolder<UserPersonalDetails>();
				List.setSource(nameSearchResult);
				List.setPageSize(10);
				request.getSession().setAttribute("student", List);

			} else if (page.equals("prev")) {

				List = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("student");
				List.previousPage();

			} else if (page.equals("next")) {

				List = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("student");
				List.nextPage();

			} else {

				int pageNum = Integer.parseInt(page);
				List = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("student");
				List.setPage(pageNum - 1);

			}
			String encode = URLEncoder.encode(search);
			model.addObject("search", encode);
			model.setViewName("searchStudentNameResult");
		}
		return model;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/searchStudentIC", "/searchStudentIC/{page}" }, method = RequestMethod.GET)
	public ModelAndView searchStudentIC(ModelAndView model, @RequestParam("searchStudentIC") String search,
			@PathVariable(required = false, name = "page") String page, HttpServletRequest request,
			HttpServletResponse response) {

		List<UserPersonalDetails> icSearchResult = clerkDao.searchStudentByIC(search);

		if (icSearchResult.isEmpty() || search.isBlank()) {

			model.addObject("Message", "No Result Found");
			model.setViewName("noClassResultFound");

		} else {

			PagedListHolder<UserPersonalDetails> List;
			if (page == null) {

				List = new PagedListHolder<UserPersonalDetails>();
				List.setSource(icSearchResult);
				List.setPageSize(10);
				request.getSession().setAttribute("student", List);

			} else if (page.equals("prev")) {

				List = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("student");
				List.previousPage();

			} else if (page.equals("next")) {

				List = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("student");
				List.nextPage();

			} else {

				int pageNum = Integer.parseInt(page);
				List = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("student");
				List.setPage(pageNum - 1);

			}
			String encode = URLEncoder.encode(search);
			model.addObject("search", encode);
			model.setViewName("searchStudentICResult");
		}
		return model;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/searchStudentEmail", "/searchStudentEmail/{page}" }, method = RequestMethod.GET)
	public ModelAndView searchStudentEmail(ModelAndView model, @RequestParam("searchStudentEmail") String search,
			@PathVariable(required = false, name = "page") String page, HttpServletRequest request,
			HttpServletResponse response) {

		List<UserPersonalDetails> emailSearchResult = clerkDao.searchStudentByEmail(search);

		if (emailSearchResult.isEmpty() || search.isBlank()) {

			model.addObject("Message", "No Result Found");
			model.setViewName("noClassResultFound");

		} else {

			PagedListHolder<UserPersonalDetails> List;
			if (page == null) {

				List = new PagedListHolder<UserPersonalDetails>();
				List.setSource(emailSearchResult);
				List.setPageSize(10);
				request.getSession().setAttribute("student", List);

			} else if (page.equals("prev")) {

				List = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("student");
				List.previousPage();

			} else if (page.equals("next")) {

				List = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("student");
				List.nextPage();

			} else {

				int pageNum = Integer.parseInt(page);
				List = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("student");
				List.setPage(pageNum - 1);

			}
			String encode = URLEncoder.encode(search);
			model.addObject("search", encode);
			model.setViewName("searchStudentEmailResult");
		}
		return model;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/paymentPage", "/paymentPage/{page}" }, method = RequestMethod.GET)
	public ModelAndView paymentPage(ModelAndView model, @PathVariable(required = false, name = "page") String page,
			HttpServletRequest request, HttpServletResponse response) {

		List<Payment> paymentList;
		PagedListHolder<Payment> list;
		if (page == null) {

			list = new PagedListHolder<Payment>();
			paymentList = clerkDao.paymentList();
			list.setSource(paymentList);
			list.setPageSize(10);
			request.getSession().setAttribute("paymentList", list);

		} else if (page.equals("prev")) {

			list = (PagedListHolder<Payment>) request.getSession().getAttribute("paymentList");
			list.previousPage();

		} else if (page.equals("next")) {

			list = (PagedListHolder<Payment>) request.getSession().getAttribute("paymentList");
			list.nextPage();

		} else {

			int pageNum = Integer.parseInt(page);
			list = (PagedListHolder<Payment>) request.getSession().getAttribute("paymentList");
			list.setPage(pageNum - 1);

		}

		model.setViewName("paymentPage");
		return model;
	}

	@RequestMapping(value = "/viewPayment", method = RequestMethod.GET)
	public ModelAndView viewPayment(@RequestParam Integer paymentId) {

		Payment payment = clerkDao.getPaymentById(paymentId);
		ModelAndView model = new ModelAndView("viewPayment");
		model.addObject("payment", payment);
		return model;

	}

	@RequestMapping(value = "/getPaymentProof/{paymentId}")
	public void getPaymentProof(HttpServletResponse response, @PathVariable("paymentId") int paymentId)
			throws Exception {
		response.setContentType("image/jpeg, image/jpg, image/png");

		Blob photo = clerkDao.getProofById(paymentId);

		byte[] bytes = photo.getBytes(1, (int) photo.length());
		InputStream inputStream = new ByteArrayInputStream(bytes);
		IOUtils.copy(inputStream, response.getOutputStream());
	}

	@RequestMapping(value = "/approvePayment", method = RequestMethod.GET)
	public ModelAndView approvePayment(@RequestParam Integer paymentId) {

		clerkDao.acceptPayment(paymentId);
		Payment payment = clerkDao.getPaymentById(paymentId);
		String email = clerkDao.getEmailById(payment.getUserId());
		ModelAndView model = new ModelAndView();

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(email);
		mailMessage.setSubject("Payment Approved!");
		mailMessage.setText("Integrated School Information Management and Monitoring System\n"+ 
				"________________________________________________________________________________\n\n"+
				"Thank you for your payment. \n" + "Please click the link below to view your receipt \n"
				+ "http://localhost:8080/ISIMM/viewReceipt/AVkM" + paymentId + "WvAtcZ0LTjm29WFN");
		mailSender.send(mailMessage);

		model.addObject("payment", payment);
		model.setViewName("redirect:/paymentPage");
		return model;

	}

	@RequestMapping(value = "/rejectPayment", method = RequestMethod.GET)
	public ModelAndView rejectPayment(@RequestParam Integer paymentId) {

		clerkDao.rejectPayment(paymentId);
		Payment payment = clerkDao.getPaymentById(paymentId);
		String email = clerkDao.getEmailById(payment.getUserId());
		ModelAndView model = new ModelAndView("viewPDF", "payment", payment);

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(email);
		mailMessage.setSubject("Payment Rejected!");
		mailMessage.setText("Integrated School Information Management and Monitoring System\n"+ 
				"________________________________________________________________________________\n\n"+
				"Sorry your payment has been rejected. \n" + "This might due to: \n" + "1. Unclear payment proof. \n"
						+ "2. Missing payment proof. \n\n" + "Please upload a new payment proof. ");
		mailSender.send(mailMessage);

		model.setViewName("redirect:/paymentPage");
		return model;

	}

	@RequestMapping(value = "/viewReceipt/AVkM{paymentId}WvAtcZ0LTjm29WFN")
	public ModelAndView viewReceipt(@PathVariable("paymentId") int paymentId,
			@ModelAttribute(name = "payment") Payment payment) {

		payment = clerkDao.getPaymentById(paymentId);
		return new ModelAndView("viewReceipt", "payment", payment);

	}

	@RequestMapping(value = "/searchPayment", method = RequestMethod.GET)
	public String searchPayment() {

		return "searchPayment";

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/searchPaymentByName", "/searchPaymentByName/{page}" }, method = RequestMethod.GET)
	public ModelAndView searchPaymentByName(ModelAndView model, @RequestParam("searchPaymentByName") String search,
			@PathVariable(required = false, name = "page") String page, HttpServletRequest request,
			HttpServletResponse response) {

		List<Payment> paymentSearchResult = clerkDao.searchPaymentByName(search);

		if (paymentSearchResult.isEmpty() || search.isBlank()) {

			model.addObject("Message", "No Result Found");
			model.setViewName("noClassResultFound");

		} else {

			PagedListHolder<Payment> List;
			if (page == null) {

				List = new PagedListHolder<Payment>();
				List.setSource(paymentSearchResult);
				List.setPageSize(10);
				request.getSession().setAttribute("payment", List);

			} else if (page.equals("prev")) {

				List = (PagedListHolder<Payment>) request.getSession().getAttribute("payment");
				List.previousPage();

			} else if (page.equals("next")) {

				List = (PagedListHolder<Payment>) request.getSession().getAttribute("payment");
				List.nextPage();

			} else {

				int pageNum = Integer.parseInt(page);
				List = (PagedListHolder<Payment>) request.getSession().getAttribute("payment");
				List.setPage(pageNum - 1);

			}
			String encode = URLEncoder.encode(search);
			model.addObject("search", encode);
			model.setViewName("searchPaymentByNameResult");
		}
		return model;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/searchPaymentByStatus", "/searchPaymentByStatus/{page}" }, method = RequestMethod.GET)
	public ModelAndView searchPaymentByStatus(ModelAndView model, @RequestParam("searchPaymentByStatus") String search,
			@PathVariable(required = false, name = "page") String page, HttpServletRequest request,
			HttpServletResponse response) {

		List<Payment> paymentSearchResult = clerkDao.searchPaymentByStatus(search);

		if (paymentSearchResult.isEmpty() || search.isBlank()) {

			model.addObject("Message", "No Result Found");
			model.setViewName("noClassResultFound");

		} else {

			PagedListHolder<Payment> List;
			if (page == null) {

				List = new PagedListHolder<Payment>();
				List.setSource(paymentSearchResult);
				List.setPageSize(10);
				request.getSession().setAttribute("payment", List);

			} else if (page.equals("prev")) {

				List = (PagedListHolder<Payment>) request.getSession().getAttribute("payment");
				List.previousPage();

			} else if (page.equals("next")) {

				List = (PagedListHolder<Payment>) request.getSession().getAttribute("payment");
				List.nextPage();

			} else {

				int pageNum = Integer.parseInt(page);
				List = (PagedListHolder<Payment>) request.getSession().getAttribute("payment");
				List.setPage(pageNum - 1);

			}
			String encode = URLEncoder.encode(search);
			model.addObject("search", encode);
			model.setViewName("searchPaymentByStatusResult");
		}
		return model;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/searchPaymentByFeeId", "/searchPaymentByFeeId/{page}" }, method = RequestMethod.GET)
	public ModelAndView searchPaymentByFeeId(ModelAndView model, @RequestParam("searchPaymentByFeeId") String search,
			@PathVariable(required = false, name = "page") String page, HttpServletRequest request,
			HttpServletResponse response) {

		List<Payment> paymentSearchResult = clerkDao.searchPaymentByFeeId(search);

		if (paymentSearchResult.isEmpty() || search.isBlank()) {

			model.addObject("Message", "No Result Found");
			model.setViewName("noClassResultFound");

		} else {

			PagedListHolder<Payment> List;
			if (page == null) {

				List = new PagedListHolder<Payment>();
				List.setSource(paymentSearchResult);
				List.setPageSize(10);
				request.getSession().setAttribute("payment", List);

			} else if (page.equals("prev")) {

				List = (PagedListHolder<Payment>) request.getSession().getAttribute("payment");
				List.previousPage();

			} else if (page.equals("next")) {

				List = (PagedListHolder<Payment>) request.getSession().getAttribute("payment");
				List.nextPage();

			} else {

				int pageNum = Integer.parseInt(page);
				List = (PagedListHolder<Payment>) request.getSession().getAttribute("payment");
				List.setPage(pageNum - 1);

			}
			String encode = URLEncoder.encode(search);
			model.addObject("search", encode);
			model.setViewName("searchPaymentByFeeIdResult");
		}
		return model;

	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/teacherPage", "/teacherPage/{page}" })
	public ModelAndView teacherPage(ModelAndView model, @PathVariable(required = false, name = "page") String page,
			HttpServletRequest request, HttpServletResponse responser) {

		PagedListHolder<UserPersonalDetails> list;
		List<UserPersonalDetails> teacherList;
		if (page == null) {

			list = new PagedListHolder<UserPersonalDetails>();
			teacherList = clerkDao.getTeacherList();
			list.setSource(teacherList);
			list.setPageSize(10);
			request.getSession().setAttribute("teacher", list);

		} else if (page.equals("prev")) {

			list = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("teacher");
			list.previousPage();

		} else if (page.equals("next")) {

			list = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("teacher");
			list.nextPage();

		} else {

			int pageNum = Integer.parseInt(page);
			list = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("teacher");
			list.setPage(pageNum - 1);

		}
		model.setViewName("teacherPage");
		return model;

	}

	@RequestMapping(value = "/viewTeacher", method = RequestMethod.GET)
	public ModelAndView viewTeacher(@RequestParam Integer userId) {

		UserPersonalDetails user = clerkDao.getTeacherById(userId);
		ModelAndView model = new ModelAndView("viewTeacherForm");
		model.addObject("teacher", user);
		return model;

	}


	@RequestMapping(value = "/searchTeacher", method = RequestMethod.GET)
	public String searchTeacher() {

		return "searchTeacher";

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/searchNameOfTeacher", "/searchNameOfTeacher/{page}" }, method = RequestMethod.GET)
	public ModelAndView searchNameOfTeacher(ModelAndView model, @RequestParam("searchNameOfTeacher") String search,
			@PathVariable(required = false, name = "page") String page, HttpServletRequest request,
			HttpServletResponse response) {

		List<UserPersonalDetails> nameSearchResult = clerkDao.searchTeacherByName(search);

		if (nameSearchResult.isEmpty() || search.isBlank()) {

			model.addObject("Message", "No Result Found");
			model.setViewName("noClassResultFound");

		} else {

			PagedListHolder<UserPersonalDetails> List;
			if (page == null) {

				List = new PagedListHolder<UserPersonalDetails>();
				List.setSource(nameSearchResult);
				List.setPageSize(1);
				request.getSession().setAttribute("teacher", List);

			} else if (page.equals("prev")) {

				List = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("teacher");
				List.previousPage();

			} else if (page.equals("next")) {

				List = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("teacher");
				List.nextPage();

			} else {

				int pageNum = Integer.parseInt(page);
				List = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("teacher");
				List.setPage(pageNum - 1);

			}
			String encode = URLEncoder.encode(search);
			model.addObject("search", encode);
			model.setViewName("searchNameOfTeacherResult");
		}
		return model;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/searchTeacherIC", "/searchTeacherIC/{page}" }, method = RequestMethod.GET)
	public ModelAndView searchTeacherIC(ModelAndView model, @RequestParam("searchTeacherIC") String search,
			@PathVariable(required = false, name = "page") String page, HttpServletRequest request,
			HttpServletResponse response) {

		List<UserPersonalDetails> icSearchResult = clerkDao.searchTeacherByIC(search);

		if (icSearchResult.isEmpty() || search.isBlank()) {

			model.addObject("Message", "No Result Found");
			model.setViewName("noClassResultFound");

		} else {

			PagedListHolder<UserPersonalDetails> List;
			if (page == null) {

				List = new PagedListHolder<UserPersonalDetails>();
				List.setSource(icSearchResult);
				List.setPageSize(1);
				request.getSession().setAttribute("teacher", List);

			} else if (page.equals("prev")) {

				List = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("teacher");
				List.previousPage();

			} else if (page.equals("next")) {

				List = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("teacher");
				List.nextPage();

			} else {

				int pageNum = Integer.parseInt(page);
				List = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("teacher");
				List.setPage(pageNum - 1);

			}
			String encode = URLEncoder.encode(search);
			model.addObject("search", encode);
			model.setViewName("searchTeacherICResult");
		}
		return model;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/searchTeacherEmail", "/searchTeacherEmail/{page}" }, method = RequestMethod.GET)
	public ModelAndView searchTeacherEmail(ModelAndView model, @RequestParam("searchTeacherEmail") String search,
			@PathVariable(required = false, name = "page") String page, HttpServletRequest request,
			HttpServletResponse response) {

		List<UserPersonalDetails> emailSearchResult = clerkDao.searchTeacherByEmail(search);

		if (emailSearchResult.isEmpty() || search.isBlank()) {

			model.addObject("Message", "No Result Found");
			model.setViewName("noClassResultFound");

		} else {

			PagedListHolder<UserPersonalDetails> List;
			if (page == null) {

				List = new PagedListHolder<UserPersonalDetails>();
				List.setSource(emailSearchResult);
				List.setPageSize(1);
				request.getSession().setAttribute("teacher", List);

			} else if (page.equals("prev")) {

				List = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("teacher");
				List.previousPage();

			} else if (page.equals("next")) {

				List = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("teacher");
				List.nextPage();

			} else {

				int pageNum = Integer.parseInt(page);
				List = (PagedListHolder<UserPersonalDetails>) request.getSession().getAttribute("teacher");
				List.setPage(pageNum - 1);

			}
			String encode = URLEncoder.encode(search);
			model.addObject("search", encode);
			model.setViewName("searchTeacherEmailResult");
		}
		return model;

	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/subjectPage", "/subjectPage/{page}" })
	public ModelAndView subjectPage(ModelAndView model, @PathVariable(required = false, name = "page") String page,
			HttpServletRequest request, HttpServletResponse responser) {

		PagedListHolder<Subject> list;
		List<Subject> subjectList;
		if (page == null) {

			list = new PagedListHolder<Subject>();
			subjectList = clerkDao.subjectList();
			list.setSource(subjectList);
			list.setPageSize(10);
			request.getSession().setAttribute("subject", list);

		} else if (page.equals("prev")) {

			list = (PagedListHolder<Subject>) request.getSession().getAttribute("subject");
			list.previousPage();

		} else if (page.equals("next")) {

			list = (PagedListHolder<Subject>) request.getSession().getAttribute("subject");
			list.nextPage();

		} else {

			int pageNum = Integer.parseInt(page);
			list = (PagedListHolder<Subject>) request.getSession().getAttribute("subject");
			list.setPage(pageNum - 1);

		}
		model.setViewName("subjectPage");
		return model;

	}
	
	@RequestMapping(value = "/deleteSubject", method = RequestMethod.GET)
	public ModelAndView deleteSubject(@RequestParam Integer subjectId) {

		clerkDao.deleteSubject(subjectId);

		return new ModelAndView("redirect:/subjectPage");

	}
	
	
	@RequestMapping(value={"/subjectPage/addSubject","/addSubject"},method = RequestMethod.POST)
	public ModelAndView addSubject(ModelAndView model,Subject subject) {
		
		clerkDao.AddSubject(subject);
		model.setViewName("redirect:/subjectPage");
		return model;
		
	}
	
	@RequestMapping(value="/profile",method=RequestMethod.GET)
	public ModelAndView profile(ModelAndView model,Principal principal) {
		
		UserPersonalDetails user = clerkDao.getUserByUsername(principal.getName());
		model.addObject("user",user);
		model.setViewName("profile");	
		return model;
	}
	
	@RequestMapping(value="/updateProfile",method= RequestMethod.POST)
	public ModelAndView updateProfile(ModelAndView model, @ModelAttribute(name="user")UserPersonalDetails user,
			Principal principal,MultipartFile photo) {
		
		int userId=clerkDao.getUserIdByUsername(principal.getName());
		try {
			
			clerkDao.updateProfile(user, userId, photo);
			model.setViewName("redirect:/profile");
			return model;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return model;
		}
		
	}
	
	@RequestMapping(value="/viewAttendance",method=RequestMethod.GET)
	public ModelAndView viewAttendance(@RequestParam(name="userId")int userId) {
		
		UserPersonalDetails user= clerkDao.getStudentById(userId);
		String className= clerkDao.getClassNameByUserId(userId);
		List<Attendance> attendance = clerkDao.AttendanceById(userId);
		double totalAttendance=clerkDao.getTotalAttendanceCount(userId);
		double presentCount=clerkDao.getPresentCount(userId);
		int absentCount= (int) (totalAttendance-presentCount);
		double attendancePercentage=(presentCount/totalAttendance)*100;
		
		ModelAndView model = new ModelAndView("viewAttendance");
		model.addObject("attendance",attendance);
		model.addObject("user",user);
		model.addObject("className",className);
		model.addObject("absent",absentCount);
		model.addObject("percentage",attendancePercentage);
		model.addObject("userId",userId);
		return model;
		
	}
	
	@RequestMapping(value = "/printAttendance", method=RequestMethod.GET )
	public ModelAndView printAttendance(@RequestParam(name="userId")int userId) {

		UserPersonalDetails user= clerkDao.getStudentById(userId);
		String className= clerkDao.getClassNameByUserId(userId);
		List<Attendance> attendance = clerkDao.AttendanceById(userId);
		Double totalAttendance=clerkDao.getTotalAttendanceCount(userId);
		Double presentCount=clerkDao.getPresentCount(userId);
		int absentCount= (int) (totalAttendance-presentCount);
		double attendancePercentage=(presentCount/totalAttendance)*100;
		
		ModelAndView model = new ModelAndView("printAttendance");
		model.addObject("attendance",attendance);
		model.addObject("user",user);
		model.addObject("className",className);
		model.addObject("absent",absentCount);
		model.addObject("percentage",attendancePercentage);
		return model;
		
	}
	
	@RequestMapping(value="/viewReportCard",method=RequestMethod.GET)
	public ModelAndView viewReportCard(@RequestParam(name="userId")int userId) {
		
		UserPersonalDetails user= clerkDao.getStudentById(userId);
		String className= clerkDao.getClassNameByUserId(userId);
		List<ReportCard> report = clerkDao.reportCardById(userId);
		String comment=clerkDao.teacherComment(userId);
		Double attendance=clerkDao.attendancePercentage(userId);
		
		ModelAndView model = new ModelAndView("viewReportCard");
		model.addObject("user",user);
		model.addObject("className",className);
		model.addObject("userId",userId);
		model.addObject("report",report);
		model.addObject("comment",comment);
		model.addObject("attendance",attendance);
		return model;
		
	}
	
	@RequestMapping(value="/printReportCard",method=RequestMethod.GET)
	public ModelAndView printReportCard(@RequestParam(name="userId")int userId) {
		
		UserPersonalDetails user= clerkDao.getStudentById(userId);
		String className= clerkDao.getClassNameByUserId(userId);
		List<ReportCard> report = clerkDao.reportCardById(userId);
		String comment=clerkDao.teacherComment(userId);
		Double attendance=clerkDao.attendancePercentage(userId);
		
		ModelAndView model = new ModelAndView("printReportCard");
		model.addObject("user",user);
		model.addObject("className",className);
		model.addObject("userId",userId);
		model.addObject("report",report);
		model.addObject("comment",comment);
		model.addObject("attendance",attendance);
		return model;
		
	}
	
	

}
