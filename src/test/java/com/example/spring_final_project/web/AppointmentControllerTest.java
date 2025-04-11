package com.example.spring_final_project.web;

import com.example.spring_final_project.Appointment.model.Appointment;
import com.example.spring_final_project.Appointment.service.AppointmentService;
import com.example.spring_final_project.Doctor.model.Doctor;
import com.example.spring_final_project.Doctor.service.DoctorService;
import com.example.spring_final_project.User.model.User;
import com.example.spring_final_project.User.service.UserService;
import com.example.spring_final_project.security.UserAuthenticationData;
import com.example.spring_final_project.web.controllers.AppointmentController;
import com.example.spring_final_project.web.dto.AppointmentRegisterRequest;
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
class AppointmentControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private DoctorService doctorService;

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private AppointmentController controller;

    private final UUID userId = UUID.randomUUID();
    private final UUID doctorId = UUID.randomUUID();

    @Test
    void testGetAppointmentCreatePage_ReturnsModelAndView() {
        User user = new User(); user.setId(userId);
        Doctor doctor = new Doctor(); doctor.setId(doctorId);

        when(userService.getById(userId)).thenReturn(user);
        when(doctorService.getById(doctorId)).thenReturn(doctor);

        UserAuthenticationData auth = mock(UserAuthenticationData.class);
        when(auth.getUserId()).thenReturn(userId);

        ModelAndView modelAndView = controller.getAppointmentCreatePage(auth, doctorId);

        assertEquals("add-appointment", modelAndView.getViewName());
        assertEquals(user, modelAndView.getModel().get("user"));
        assertEquals(doctor, modelAndView.getModel().get("doctor"));
        assertTrue(modelAndView.getModel().get("appointmentRegisterRequest") instanceof AppointmentRegisterRequest);
    }

    @Test
    void testCreateAppointment_WithErrors_ReturnsForm() {
        User user = new User(); user.setId(userId);
        when(userService.getById(userId)).thenReturn(user);

        UserAuthenticationData auth = mock(UserAuthenticationData.class);
        when(auth.getUserId()).thenReturn(userId);

        AppointmentRegisterRequest request = new AppointmentRegisterRequest();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        ModelAndView modelAndView = controller.createAppointment(auth, doctorId, request, bindingResult);

        assertEquals("add-appointment", modelAndView.getViewName());
        assertEquals(user, modelAndView.getModel().get("user"));
        assertEquals(request, modelAndView.getModel().get("appointmentRegisterRequest"));
    }

    @Test
    void testCreateAppointment_ValidRequest_RedirectsToHome() {
        User user = new User(); user.setId(userId);
        when(userService.getById(userId)).thenReturn(user);

        UserAuthenticationData auth = mock(UserAuthenticationData.class);
        when(auth.getUserId()).thenReturn(userId);

        AppointmentRegisterRequest request = new AppointmentRegisterRequest();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        ModelAndView modelAndView = controller.createAppointment(auth, doctorId, request, bindingResult);

        verify(appointmentService).registerAppointment(request, userId, doctorId);
        assertEquals("redirect:/home", modelAndView.getViewName());
    }

    @Test
    void testGetAllAppointmentsForUser_ReturnsAppointmentsView() {
        User user = new User(); user.setId(userId);
        List<Appointment> appointments = List.of(new Appointment(), new Appointment());

        when(userService.getById(userId)).thenReturn(user);
        when(appointmentService.getAllActiveAppointmentsForUser(userId)).thenReturn(appointments);

        UserAuthenticationData auth = mock(UserAuthenticationData.class);
        when(auth.getUserId()).thenReturn(userId);

        ModelAndView modelAndView = controller.getAllAppointmentsForUser(auth);

        assertEquals("appointments", modelAndView.getViewName());
        assertEquals(user, modelAndView.getModel().get("user"));
        assertEquals(appointments, modelAndView.getModel().get("appointments"));
    }

}