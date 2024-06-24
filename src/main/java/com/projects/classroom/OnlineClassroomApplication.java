package com.projects.classroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class OnlineClassroomApplication{
	
	public static void main(String[] args) {
		SpringApplication.run(OnlineClassroomApplication.class, args);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	//TODO: 
	//Handle user password encryption through Spring and Database. Better method for encrypting other than PasswordEncoder
	
	//TODO:
	//See if Student and Teacher entities are created successfully in the database after User entities are registered
	//If not, save Student and Teacher entities directly rather than through User first
	
	//TODO:
	//See if all JPA Derived methods function properly, especially for Classroom, Teacher, Student, and Submission
	
	//TODO:
	//LogInController.attemptLogin()
	//SignUpController.attemptSignUp()
	//validation messages for incorrect credentials?
	
	//TODO:
	//AnnouncementController.createAnnouncement()
	//POST mapping instead of GET
	//Exception handling? Invalid Announcement object?
	//attributes.addFlashAttribute("errorOccured",true);
	//return "redirect:/write-post-announcement";
	
	//TODO:
	//AssignmentController.viewAssignmentPage()
	//Exception handling for assignment retrieval
	//model.addAttribute("errorOccured",true);
	
	//AssignmentController.createAssignment()
	//SAME AS createAnnouncement()
    //return "redirect:/write-post-assignment";
	
	//TODO:
	//ClassroomController.showClassroomPage()
	//model.addAttribute("errorOccured",true);
	
	//ClassroomController.classroomJoin()
	//should teachers be able to join classrooms? as students or teachers?
	
	//ClassroomController.sendClassroomJoinRequest()
	//POST method?
	//Error/Exception handling
	//attributes.addFlashAttribute("failedRegistration",true);
    //return "redirect:/send-join-request-" + classroomId;
	
	//ClassroomController.selectClass()
	//POST method?
	//Error/Exception handling
	//return "redirect:/my-classrooms";
	
	//ClassroomController.createClass()
	//model.addAttribute("errorOccured",true);
    //return "new-classroom";
	
	//ClassroomController.showMyClassroomPage(), searchClassrooms()
	//ClassroomButton? Replace? Move to another package?
	
	//TODO:
	//GradesController.viewGradesPage()
	//error/exception handling:
	//model.addAttribute("errorOccured",true);
	//
	
	//TODO:
	//GradeController.submiteGrade()
	//error/exception handling?
	//attributes.addFlashAttribute("errorOccured",true);
	
	//TODO:
	//viewGradesForAssignment()
	//error/exception handling?
	//model.addAttribute("errorOccured",true);
	
	//TODO:
	//LessonController.viewLessonPage()
	//model.addAttribute("errorOccured",true);
	
	//TODO:
	//LessonController.createLesson()
	//attributes.addFlashAttribute("errorOccured",true);
	//return "redirect:/write-post-lesson";
	
	
	//TODO:
	//MembersController.viewMembersPage()
	//model.addAttribute("errorOccured",true);
	
	//TODO:
	//SubmissionController.viewSubmissionPage()
	//error/exception handling
	//model.addAttribute("errorOccured",true);
	
	//TODO:
	//SubmissionController.submitAssignment()
	//if Submission is graded, don't allow to resubmit?
	//update submission lists from respective Student and Assignment objects? Or JPA handles automatically?
	//error/exception handling
	//attributes.addFlashAttribute("errorOccured",true);
	
	//TODO:
	//SubmissionController.viewStudentSubmissions()
	//error/exception handling
	//model.addAttribute("errorOccured",true);
	
	//TODO:
	//SumbissionController.viewSubmission()
	//error/exception handling
	//model.addAttribute("errorOccured",true);
	//return "redirect:/view-submissions-"+assignmentId;
	
	//TODO:
	//Verify @Valid annotation works. Implement manual validation?
	
	//TODO:
	//Remove unnecessary dependencies?
	
	//TODO:
	//replace studentId and teacherId uses in code with userId
	
	//TODO:
	//LogInController.viewHomePage()
	//studentId==null, teacherId==null
	//if a null object is cast to type Long will it still be null?
	
	//SQL Constraint Violation Exception handling:
    /*
     * javax.validation.ConstraintViolationException: Validation failed for classes
     * [com.projects.classroom.model.Lesson] during persist time for groups
     * [javax.validation.groups.Default, ] List of constraint violations:[
     * ConstraintViolationImpl{interpolatedMessage='must not be blank',
     * propertyPath=title, rootBeanClass=class com.projects.classroom.model.Lesson,
     * messageTemplate='{javax.validation.constraints.NotBlank.message}'} ]
     */
	
}
