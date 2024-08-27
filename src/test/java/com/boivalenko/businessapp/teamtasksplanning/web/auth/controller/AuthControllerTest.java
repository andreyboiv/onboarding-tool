package com.boivalenko.businessapp.teamtasksplanning.web.auth.controller;

import com.boivalenko.businessapp.teamtasksplanning.web.auth.entity.Activity;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.entity.Employee;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.service.EmployeeService;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.utils.CookieUtils;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.utils.JwtUtils;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.viewmodel.EmployeeVm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.servlet.server.Encoding;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//To exclude security
// filters in your MockMvc tests,
// set @AutoConfigureMockMvc(addFilters = false) or set
// @WebMvcTest(controllers = GenreController.class,
//        excludeAutoConfiguration = {SecurityAutoConfiguration.class})

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @MockBean
    CookieUtils cookieUtils;

    @MockBean
    JwtUtils jwtUtils;

    @MockBean
    EmployeeService employeeService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Employee can be register")
    void register() throws Exception {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("musterLogin");
        employeeVm.setPassword("musterPassword");
        employeeVm.setEmail("musteremail@email.de");
        HttpStatus httpStatus = HttpStatus.OK;

        ResponseEntity response = new ResponseEntity<>(employeeVm, httpStatus);
        when(this.employeeService.register(any(EmployeeVm.class)))
                .thenReturn(response);
        ObjectMapper mapper = new ObjectMapper();

        String contentRequestResponse = mapper.writeValueAsString(employeeVm);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.put("/auth/register")
                        .content(contentRequestResponse)
                        .characterEncoding(Encoding.DEFAULT_CHARSET)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = this.mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(print())
                .andExpectAll(
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json(contentRequestResponse),
                        status().isOk()
                ).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString(Encoding.DEFAULT_CHARSET);
        EmployeeVm employeeOutput = mapper.readValue(contentAsString, EmployeeVm.class);

        assertEquals(employeeOutput.getLogin(), employeeVm.getLogin());
        assertEquals(employeeOutput.getEmail(), employeeVm.getEmail());
        assertEquals(employeeOutput.getPassword(), employeeVm.getPassword());

        assertEquals(HttpStatus.OK, httpStatus);

        verify(this.employeeService, times(1)).register(any(EmployeeVm.class));
    }


    @Test
    @DisplayName("Account von einem Employee can be activate")
    void activateEmployee() throws Exception {
        String uuid = UUID.randomUUID().toString();
        Activity activity = new Activity(uuid, false, null);
        HttpStatus httpStatus = HttpStatus.OK;

        ResponseEntity response = new ResponseEntity<>(activity, httpStatus);
        when(this.employeeService.activateEmployee(uuid))
                .thenReturn(response);
        ObjectMapper mapper = new ObjectMapper();

        String contentRequestResponse = mapper.writeValueAsString(activity);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.post("/auth/activate-account")
                        .content(uuid)
                        .characterEncoding(Encoding.DEFAULT_CHARSET)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = this.mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(print())
                .andExpectAll(
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json(contentRequestResponse),
                        status().isOk()
                ).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString(Encoding.DEFAULT_CHARSET);
        Activity activityOutput = mapper.readValue(contentAsString, Activity.class);

        assertEquals(HttpStatus.OK, httpStatus);
        assertEquals(activityOutput.getUuid(), activity.getUuid());


        verify(this.employeeService, times(1)).activateEmployee(uuid);
    }


    @Test
    @DisplayName("Employee kann sich einloggen")
    void logIn() throws Exception {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("musterLogin");
        employeeVm.setPassword("musterPassword");
        employeeVm.setEmail("musteremail@email.de");
        HttpStatus httpStatus = HttpStatus.OK;

        ResponseEntity response = new ResponseEntity<>(employeeVm, httpStatus);
        when(this.employeeService.logIn(any(EmployeeVm.class)))
                .thenReturn(response);
        ObjectMapper mapper = new ObjectMapper();

        String contentRequestResponse = mapper.writeValueAsString(employeeVm);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.post("/auth/login")
                        .content(contentRequestResponse)
                        .characterEncoding(Encoding.DEFAULT_CHARSET)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = this.mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(print())
                .andExpectAll(
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json(contentRequestResponse),
                        status().isOk()
                ).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString(Encoding.DEFAULT_CHARSET);
        EmployeeVm employeeOutput = mapper.readValue(contentAsString, EmployeeVm.class);

        assertEquals(employeeOutput.getLogin(), employeeVm.getLogin());
        assertEquals(employeeOutput.getEmail(), employeeVm.getEmail());
        assertEquals(employeeOutput.getPassword(), employeeVm.getPassword());

        assertEquals(HttpStatus.OK, httpStatus);

        verify(this.employeeService, times(1)).logIn(any(EmployeeVm.class));
    }


    @Test
    @DisplayName("Employee kann sich auslogen")
    void logOut() throws Exception {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("musterLogin");
        employeeVm.setPassword("musterPassword");
        employeeVm.setEmail("musteremail@email.de");
        HttpStatus httpStatus = HttpStatus.OK;

        ResponseEntity response = new ResponseEntity<>(employeeVm, httpStatus);
        when(this.employeeService.logOut())
                .thenReturn(response);
        ObjectMapper mapper = new ObjectMapper();

        String contentResponse = mapper.writeValueAsString(employeeVm);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.post("/auth/logout")
                        .characterEncoding(Encoding.DEFAULT_CHARSET)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = this.mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(print())
                .andExpectAll(
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json(contentResponse),
                        status().isOk()
                ).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString(Encoding.DEFAULT_CHARSET);
        EmployeeVm employeeOutput = mapper.readValue(contentAsString, EmployeeVm.class);

        assertEquals(employeeOutput.getLogin(), employeeVm.getLogin());
        assertEquals(employeeOutput.getEmail(), employeeVm.getEmail());
        assertEquals(employeeOutput.getPassword(), employeeVm.getPassword());

        assertEquals(HttpStatus.OK, httpStatus);

        verify(this.employeeService, times(1)).logOut();
    }

    @Test
    @DisplayName("Employee kann sein password ändern")
    void updatePassword() throws Exception {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("musterLogin");
        employeeVm.setPassword("musterPassword");
        employeeVm.setEmail("musteremail@email.de");
        HttpStatus httpStatus = HttpStatus.OK;

        ResponseEntity response = new ResponseEntity<>(employeeVm, httpStatus);
        when(this.employeeService.updatePassword(employeeVm.getPassword()))
                .thenReturn(response);
        ObjectMapper mapper = new ObjectMapper();

        String contentResponse = mapper.writeValueAsString(employeeVm);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.post("/auth/update-password")
                        .content(employeeVm.getPassword())
                        .characterEncoding(Encoding.DEFAULT_CHARSET)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = this.mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(print())
                .andExpectAll(
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json(contentResponse),
                        status().isOk()
                ).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString(Encoding.DEFAULT_CHARSET);
        EmployeeVm employeeOutput = mapper.readValue(contentAsString, EmployeeVm.class);

        assertEquals(employeeOutput.getLogin(), employeeVm.getLogin());
        assertEquals(employeeOutput.getEmail(), employeeVm.getEmail());
        assertEquals(employeeOutput.getPassword(), employeeVm.getPassword());

        assertEquals(HttpStatus.OK, httpStatus);

        verify(this.employeeService, times(1)).updatePassword(employeeVm.getPassword());
    }


    @Test
    @DisplayName("Employee kann deaktiviert werden")
    void deActivateEmployee() throws Exception {
        String uuid = UUID.randomUUID().toString();
        Activity activity = new Activity(uuid, false, null);
        Employee employee = new Employee();
        employee.setId(1L);
        activity.setEmployeeToActivity(employee);

        HttpStatus httpStatus = HttpStatus.OK;

        ResponseEntity response = new ResponseEntity<>(activity, httpStatus);
        when(this.employeeService.deActivateEmployee(activity.getEmployeeToActivity().getId()))
                .thenReturn(response);
        ObjectMapper mapper = new ObjectMapper();

        String contentResponse = mapper.writeValueAsString(activity);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.post("/auth/account-deactivate")
                        .content(String.valueOf(activity.getEmployeeToActivity().getId()))
                        .characterEncoding(Encoding.DEFAULT_CHARSET)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = this.mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(print())
                .andExpectAll(
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json(contentResponse),
                        status().isOk()
                ).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString(Encoding.DEFAULT_CHARSET);
        //  Activity activityOutput = mapper.readValue(contentAsString, Activity.class);

        //    assertEquals(activity.getEmployeeToActivity().getId(), activityOutput.getEmployeeToActivity().getId());
        assertEquals(HttpStatus.OK, httpStatus);

        verify(this.employeeService, times(1)).deActivateEmployee(activity.getEmployeeToActivity().getId());
    }


    @Test
    @DisplayName("Es kann eine E-mail mit der Aktivierung an einen Employee gesendet werden")
    void resendActivateEmail() throws Exception {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("musterLogin");
        employeeVm.setPassword("musterPassword");
        employeeVm.setEmail("musteremail@email.de");
        HttpStatus httpStatus = HttpStatus.OK;

        ResponseEntity response = new ResponseEntity<>(employeeVm, httpStatus);
        when(this.employeeService.resendActivateEmail(employeeVm.getEmail()))
                .thenReturn(response);
        ObjectMapper mapper = new ObjectMapper();

        String contentResponse = mapper.writeValueAsString(employeeVm);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.post("/auth/resend-activate-email")
                        .content(employeeVm.getEmail())
                        .characterEncoding(Encoding.DEFAULT_CHARSET)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = this.mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(print())
                .andExpectAll(
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json(contentResponse),
                        status().isOk()
                ).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString(Encoding.DEFAULT_CHARSET);
        EmployeeVm employeeOutput = mapper.readValue(contentAsString, EmployeeVm.class);

        assertEquals(employeeOutput.getEmail(), employeeVm.getEmail());
        assertEquals(HttpStatus.OK, httpStatus);

        verify(this.employeeService, times(1)).resendActivateEmail(employeeOutput.getEmail());
    }

    @Test
    @DisplayName("Es kann eine E-mail mit dem ResetPassword an einen Employee gesendet werden")
    void sendResetPasswordEmail() throws Exception {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("musterLogin");
        employeeVm.setPassword("musterPassword");
        employeeVm.setEmail("musteremail@email.de");
        HttpStatus httpStatus = HttpStatus.OK;

        ResponseEntity response = new ResponseEntity<>(employeeVm, httpStatus);
        when(this.employeeService.sendResetPasswordEmail(employeeVm.getEmail()))
                .thenReturn(response);
        ObjectMapper mapper = new ObjectMapper();

        String contentResponse = mapper.writeValueAsString(employeeVm);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.post("/auth/send-reset-password-email")
                        .content(employeeVm.getEmail())
                        .characterEncoding(Encoding.DEFAULT_CHARSET)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = this.mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(print())
                .andExpectAll(
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json(contentResponse),
                        status().isOk()
                ).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString(Encoding.DEFAULT_CHARSET);
        EmployeeVm employeeOutput = mapper.readValue(contentAsString, EmployeeVm.class);

        assertEquals(employeeOutput.getEmail(), employeeVm.getEmail());
        assertEquals(HttpStatus.OK, httpStatus);

        verify(this.employeeService, times(1)).sendResetPasswordEmail(employeeOutput.getEmail());
    }
}
