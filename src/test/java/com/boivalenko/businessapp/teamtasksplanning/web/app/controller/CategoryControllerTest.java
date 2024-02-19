package com.boivalenko.businessapp.teamtasksplanning.web.app.controller;

import com.boivalenko.businessapp.teamtasksplanning.web.app.entity.Category;
import com.boivalenko.businessapp.teamtasksplanning.web.app.search.CategorySearchValues;
import com.boivalenko.businessapp.teamtasksplanning.web.app.service.CategoryService;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.utils.CookieUtils;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.utils.JwtUtils;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
@WebMvcTest(controllers = CategoryController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @MockBean
    CookieUtils cookieUtils;

    @MockBean
    JwtUtils jwtUtils;

    @MockBean
    CategoryService categoryService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Category can be saved")
    void save() throws Exception {
        Category category = new Category("musterTitle", 999L, 999L, null);
        HttpStatus httpStatus = HttpStatus.OK;

        ResponseEntity<Category> response = new ResponseEntity<>(category, httpStatus);
        when(this.categoryService.save(any(Category.class)))
                .thenReturn(response);
        ObjectMapper mapper = new ObjectMapper();

        String contentRequestResponse = mapper.writeValueAsString(category);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.post("/category/add")
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
        Category categoryJson = mapper.readValue(contentAsString, Category.class);

        assertEquals(categoryJson.getTitle(), category.getTitle());
        assertEquals(categoryJson.getCompletedCount(), category.getCompletedCount());
        assertEquals(categoryJson.getUncompletedCount(), category.getUncompletedCount());

        assertEquals(HttpStatus.OK, httpStatus);

        verify(this.categoryService, times(1)).save(any(Category.class));
    }


    @Test
    @DisplayName("Category can be updated")
    void update() throws Exception {
        Category category = new Category("musterTitle", 999L, 999L, null);
        HttpStatus httpStatus = HttpStatus.OK;

        ResponseEntity<Category> response = new ResponseEntity<>(category, httpStatus);
        when(this.categoryService.update(any(Category.class)))
                .thenReturn(response);
        ObjectMapper mapper = new ObjectMapper();

        String contentRequestResponse = mapper.writeValueAsString(category);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.put("/category/update")
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
        Category categoryJson = mapper.readValue(contentAsString, Category.class);

        assertEquals(categoryJson.getTitle(), category.getTitle());
        assertEquals(categoryJson.getCompletedCount(), category.getCompletedCount());
        assertEquals(categoryJson.getUncompletedCount(), category.getUncompletedCount());

        assertEquals(HttpStatus.OK, httpStatus);

        verify(this.categoryService, times(1)).update(any(Category.class));
    }

    @Test
    @DisplayName("Category can be deleted")
    void deleteById() throws Exception {
        Category category = new Category("musterTitle", 999L, 999L, null);
        Long id = 1L;
        category.setId(id);
        HttpStatus httpStatus = HttpStatus.OK;

        ResponseEntity<Category> response = new ResponseEntity<>(category, httpStatus);
        when(this.categoryService.deleteById(any(Long.class)))
                .thenReturn(response);
        ObjectMapper mapper = new ObjectMapper();

        String contentResponse = mapper.writeValueAsString(category);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.delete("/category/delete/" + id)
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
        Category categoryJson = mapper.readValue(contentAsString, Category.class);

        assertEquals(categoryJson.getTitle(), category.getTitle());
        assertEquals(categoryJson.getCompletedCount(), category.getCompletedCount());
        assertEquals(categoryJson.getUncompletedCount(), category.getUncompletedCount());

        assertEquals(HttpStatus.OK, httpStatus);

        verify(this.categoryService, times(1)).deleteById(any(Long.class));
    }

    @Test
    @DisplayName("Category can be found")
    void findById() throws Exception {
        Category category = new Category("musterTitle", 999L, 999L, null);
        Long id = 1L;
        category.setId(id);
        HttpStatus httpStatus = HttpStatus.OK;

        ResponseEntity<Category> response = new ResponseEntity<>(category, httpStatus);
        when(this.categoryService.findById(any(Long.class)))
                .thenReturn(response);
        ObjectMapper mapper = new ObjectMapper();

        String contentRequest = String.valueOf(id);
        String contentResponse = mapper.writeValueAsString(category);


        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.post("/category/id")
                        .content(contentRequest)
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
        Category categoryJson = mapper.readValue(contentAsString, Category.class);

        assertEquals(categoryJson.getTitle(), category.getTitle());
        assertEquals(categoryJson.getCompletedCount(), category.getCompletedCount());
        assertEquals(categoryJson.getUncompletedCount(), category.getUncompletedCount());

        assertEquals(HttpStatus.OK, httpStatus);

        verify(this.categoryService, times(1)).findById(any(Long.class));
    }

    @Test
    @DisplayName("Find all Categories by Login")
    void findAllByEmail() throws Exception {
        Category category = new Category("musterTitle", 999L, 999L, null);
        Category category2 = new Category("musterTitle2", 9999L, 9999L, null);
        Category category3 = new Category("musterTitle3", 99999L, 99999L, null);
        List<Category> categories = List.of(category, category2, category3);

        HttpStatus httpStatus = HttpStatus.OK;

        ResponseEntity<List<Category>> response = new ResponseEntity<>(categories, httpStatus);

        when(this.categoryService.findAllByLogin(any(String.class)))
                .thenReturn(response);
        ObjectMapper mapper = new ObjectMapper();

        String contentRequest = "musterLogin_Andrey_login";
        String contentResponse = mapper.writeValueAsString(categories);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.post("/category/all")
                        .content(contentRequest)
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
        List<Category> categoriesOutput = mapper.readValue(contentAsString, new TypeReference<>() {
        });

        assertFalse(categoriesOutput.isEmpty());
        assertEquals(categoriesOutput.size(), categories.size());

        assertEquals(HttpStatus.OK, httpStatus);

        verify(this.categoryService, times(1)).findAllByLogin(any(String.class));
    }


    @Test
    @DisplayName("Find all Categories by EmailQuery")
    void findAllByEmailQuery() throws Exception {
        Category category = new Category("musterTitle", 999L, 999L, null);
        Category category2 = new Category("musterTitle2", 9999L, 9999L, null);
        Category category3 = new Category("musterTitle3", 99999L, 99999L, null);
        List<Category> categories = List.of(category, category2, category3);

        CategorySearchValues categorySearchValues = new CategorySearchValues("musterTitle","email");

        HttpStatus httpStatus = HttpStatus.OK;

        ResponseEntity<List<Category>> response = new ResponseEntity<>(categories, httpStatus);

        when(this.categoryService.findAllByEmailQuery(any(String.class), any(String.class)))
                .thenReturn(response);
        ObjectMapper mapper = new ObjectMapper();

        String contentRequest = mapper.writeValueAsString(categorySearchValues);
        String contentResponse = mapper.writeValueAsString(categories);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.post("/category/findAllByEmailQuery")
                        .content(contentRequest)
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
        List<Category> categoriesOutput = mapper.readValue(contentAsString, new TypeReference<>() {
        });

        assertFalse(categoriesOutput.isEmpty());
        assertEquals(categoriesOutput.size(), categories.size());

        assertEquals(HttpStatus.OK, httpStatus);

        verify(this.categoryService, times(1)).findAllByEmailQuery(any(String.class), any(String.class));
    }

}
