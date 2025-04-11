package com.example.spring_final_project.web;

import com.example.spring_final_project.Appointment.model.Appointment;
import com.example.spring_final_project.Appointment.service.AppointmentService;
import com.example.spring_final_project.User.model.User;
import com.example.spring_final_project.User.service.UserService;
import com.example.spring_final_project.security.UserAuthenticationData;
import com.example.spring_final_project.web.controllers.IndexController;
import com.example.spring_final_project.web.dto.UserLoginRequest;
import com.example.spring_final_project.web.dto.UserRegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private IndexController controller;

    private final UUID userId = UUID.randomUUID();

    @Test
    void testGetIndexPage() {
        assertEquals("index", controller.getIndexPage());
    }

    @Test
    void testGetRegisterPage() {
        ModelAndView modelAndView = controller.getRegisterPage();

        assertEquals("register", modelAndView.getViewName());
        assertTrue(modelAndView.getModel().get("userRegisterRequest") instanceof UserRegisterRequest);
    }

    @Test
    void testRegisterNewUser_WithErrors() {
        UserRegisterRequest request = new UserRegisterRequest();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        ModelAndView modelAndView = controller.registerNewUser(request, bindingResult);

        assertEquals("register", modelAndView.getViewName());
    }

    @Test
    void testRegisterNewUser_Successful() {
        UserRegisterRequest request = new UserRegisterRequest();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        ModelAndView modelAndView = controller.registerNewUser(request, bindingResult);

        verify(userService).register(request);
        assertEquals("redirect:/login", modelAndView.getViewName());
    }

    @Test
    void testGetLoginPage_WithoutErrorParam() {
        ModelAndView modelAndView = controller.getLoginPage(null);

        assertEquals("login", modelAndView.getViewName());
        assertTrue(modelAndView.getModel().get("userLoginRequest") instanceof UserLoginRequest);
        assertFalse(modelAndView.getModel().containsKey("errorMessage"));
    }

    @Test
    void testGetLoginPage_WithErrorParam() {
        ModelAndView modelAndView = controller.getLoginPage("error");

        assertEquals("login", modelAndView.getViewName());
        assertEquals("Incorrect username or password!", modelAndView.getModel().get("errorMessage"));
    }

    @Test
    void testGetHomePage_WithAppointments() {
        User user = new User(); user.setId(userId);
        Appointment appointment1 = new Appointment();
        List<Appointment> appointments = List.of(appointment1);

        UserAuthenticationData auth = mock(UserAuthenticationData.class);
        when(auth.getUserId()).thenReturn(userId);
        when(userService.getById(userId)).thenReturn(user);
        when(appointmentService.getAllActiveAppointmentsForUser(userId)).thenReturn(appointments);

        ModelAndView modelAndView = controller.getHomePage(auth);

        assertEquals("home", modelAndView.getViewName());
        assertEquals(user, modelAndView.getModel().get("user"));
        assertEquals(user.getPatientCard(), modelAndView.getModel().get("patientCard"));
        assertEquals(appointment1, modelAndView.getModel().get("appointment"));
        assertEquals(1L, modelAndView.getModel().get("numberOfAppointments"));
    }

    @Test
    void testGetHomePage_NoAppointments() {
        User user = new User(); user.setId(userId);
        List<Appointment> appointments = List.of();

        UserAuthenticationData auth = mock(UserAuthenticationData.class);
        when(auth.getUserId()).thenReturn(userId);
        when(userService.getById(userId)).thenReturn(user);
        when(appointmentService.getAllActiveAppointmentsForUser(userId)).thenReturn(appointments);

        ModelAndView modelAndView = controller.getHomePage(auth);

        assertEquals("home", modelAndView.getViewName());
        assertEquals(0L, modelAndView.getModel().get("numberOfAppointments"));
        assertTrue(modelAndView.getModel().get("appointment") instanceof Appointment);
    }

}