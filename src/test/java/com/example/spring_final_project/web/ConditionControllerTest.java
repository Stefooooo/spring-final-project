package com.example.spring_final_project.web;

import com.example.spring_final_project.Condition.model.Condition;
import com.example.spring_final_project.Condition.service.ConditionService;
import com.example.spring_final_project.User.model.User;
import com.example.spring_final_project.User.service.UserService;
import com.example.spring_final_project.security.UserAuthenticationData;
import com.example.spring_final_project.web.controllers.ConditionController;
import com.example.spring_final_project.web.dto.ConditionAddRequest;
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
class ConditionControllerTest {

    @Mock
    private ConditionService conditionService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ConditionController controller;

    private final UUID userId = UUID.randomUUID();
    private final UUID conditionId = UUID.randomUUID();

    @Test
    void testGetConditionsPage() {
        User user = new User(); user.setId(userId);
        List<Condition> conditions = List.of(new Condition(), new Condition());

        UserAuthenticationData auth = mock(UserAuthenticationData.class);
        when(auth.getUserId()).thenReturn(userId);
        when(userService.getById(userId)).thenReturn(user);
        when(conditionService.getAllActiveConditions()).thenReturn(conditions);

        ModelAndView modelAndView = controller.getConditionsPage(auth);

        assertEquals("conditions", modelAndView.getViewName());
        assertEquals(conditions, modelAndView.getModel().get("conditions"));
        assertEquals(user, modelAndView.getModel().get("user"));
    }

    @Test
    void testGetConditionsAddPage() {
        User user = new User(); user.setId(userId);
        UserAuthenticationData auth = mock(UserAuthenticationData.class);
        when(auth.getUserId()).thenReturn(userId);
        when(userService.getById(userId)).thenReturn(user);

        ModelAndView modelAndView = controller.getConditionsAddPage(auth);

        assertEquals("add-condition", modelAndView.getViewName());
        assertTrue(modelAndView.getModel().get("conditionAddRequest") instanceof ConditionAddRequest);
        assertEquals(user, modelAndView.getModel().get("user"));
    }

    @Test
    void testAddCondition_WithValidationErrors() {
        User user = new User(); user.setId(userId);
        ConditionAddRequest request = new ConditionAddRequest();
        BindingResult bindingResult = mock(BindingResult.class);
        UserAuthenticationData auth = mock(UserAuthenticationData.class);
        when(auth.getUserId()).thenReturn(userId);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(userService.getById(userId)).thenReturn(user);

        ModelAndView modelAndView = controller.addCondition(auth, request, bindingResult);

        assertEquals("add-condition", modelAndView.getViewName());
        assertEquals(request, modelAndView.getModel().get("conditionAddRequest"));
        assertEquals(user, modelAndView.getModel().get("user"));
    }

    @Test
    void testAddCondition_ValidRequest() {
        ConditionAddRequest request = new ConditionAddRequest();
        BindingResult bindingResult = mock(BindingResult.class);
        UserAuthenticationData auth = mock(UserAuthenticationData.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        ModelAndView modelAndView = controller.addCondition(auth, request, bindingResult);

        verify(conditionService).saveCondition(request);
        assertEquals("redirect:/conditions", modelAndView.getViewName());
    }

    @Test
    void testDeleteCondition() {
        UserAuthenticationData auth = mock(UserAuthenticationData.class);

        ModelAndView modelAndView = controller.deleteCondition(auth, conditionId);

        verify(conditionService).deleteCondition(conditionId);
        assertEquals("redirect:/conditions", modelAndView.getViewName());
    }

}