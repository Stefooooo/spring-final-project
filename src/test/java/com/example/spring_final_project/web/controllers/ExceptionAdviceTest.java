package com.example.spring_final_project.web.controllers;

import com.example.spring_final_project.exception.NotificationServiceFeignCallException;
import com.example.spring_final_project.exception.UsernameAlreadyExistException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


class ExceptionAdviceTest {

    private final ExceptionAdvice handler = new ExceptionAdvice();

    @Test
    void testHandleUsernameAlreadyExist() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        RedirectAttributes mockRedirect = mock(RedirectAttributes.class);
        UsernameAlreadyExistException exception = new UsernameAlreadyExistException("Username taken");

        when(mockRedirect.addFlashAttribute(eq("usernameAlreadyExistMessage"), any())).thenReturn(mockRedirect);

        String result = handler.handleUsernameAlreadyExist(mockRequest, mockRedirect, exception);

        assertEquals("redirect:/register", result);
        verify(mockRedirect).addFlashAttribute("usernameAlreadyExistMessage", "Username taken");
    }

    @Test
    void testHandleNotificationFeignCallException() {
        RedirectAttributes mockRedirect = mock(RedirectAttributes.class);
        NotificationServiceFeignCallException exception = new NotificationServiceFeignCallException("Service failed");

        when(mockRedirect.addFlashAttribute(eq("clearHistoryErrorMessage"), any())).thenReturn(mockRedirect);

        String result = handler.handleNotificationFeignCallException(mockRedirect, exception);

        assertEquals("redirect:/notifications", result);
        verify(mockRedirect).addFlashAttribute("clearHistoryErrorMessage", "Service failed");
    }

    @Test
    void testHandleNotFoundExceptions() {
        Exception exception = new NoResourceFoundException(HttpMethod.GET, "/some-url"); // or any other method

        ModelAndView modelAndView = handler.handleNotFoundExceptions(exception);

        assertNotNull(modelAndView);
        assertEquals("not-found", modelAndView.getViewName());
    }

    @Test
    void testHandleAnyException() {
        Exception exception = new IllegalStateException("Something bad happened");

        ModelAndView modelAndView = handler.handleAnyException(exception);

        assertNotNull(modelAndView);
        assertEquals("internal-server-error", modelAndView.getViewName());
        assertEquals("IllegalStateException", modelAndView.getModel().get("errorMessage"));
    }
}