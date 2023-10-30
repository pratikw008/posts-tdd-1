package com.app.tdd.posts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.app.tdd.posts.model.PostEntity;
import com.app.tdd.posts.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = PostController.class)
class PostControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PostService postService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private List<PostEntity> posts;
	
	@BeforeEach
	void setUp() {
		posts = List.of(
					new PostEntity(1, "First Post", "This is my First post", 22, List.of("First Tag, Second Tag"), 2),
					new PostEntity(2, "Second Post", "This is my Second post", 22, List.of("First Tag, Second Tag"), 2)
				);
	}
	
	@Test
	void givenPostList_whenFindAll_thenRetuenPostList() throws Exception {
		// given setup mock for service method
		given(postService.findAll()).willReturn(posts);
		
		// when call actual using mockMvc
		ResultActions resultActions = mockMvc.perform(get("/api/posts"));
		
		// then verify the op
		MvcResult result = resultActions.andExpect(MockMvcResultMatchers.status().isOk())
										.andExpect(jsonPath("$.size()", CoreMatchers.is(posts.size())))
					 					.andDo(print())
					 					.andReturn();
					 
		assertThat(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(posts));
	}

	@Test
	void givenValidId_whenFindById_thenRetunPost() throws Exception {
		// given setup mock for service method
		given(postService.findPostById(anyInt())).willReturn(posts.get(0));
		
		// when call actual method using mockMvc
		ResultActions resultActions = mockMvc.perform(get("/api/posts/{id}", posts.get(0).getId()));
		
		// then verify the op
		MvcResult mvcResult = resultActions.andExpect(MockMvcResultMatchers.status().isOk())
										   .andDo(print())
										   .andReturn();
				
		assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(posts.get(0)));
	}
	
	@Test
	void givenPost_whenCreatePost_thenReturnCreatedPost() throws JsonProcessingException, Exception {
		// given setup mock for service method
		given(postService.savePost(any(PostEntity.class))).willReturn(posts.get(0));
		
		// when call actual method using mockMvc
		ResultActions resultActions = mockMvc.perform(post("/api/posts").contentType(MediaType.APPLICATION_JSON)
										  .content(objectMapper.writeValueAsString(posts.get(0))));
		
		// then verify the op
		resultActions.andExpect(status().isCreated())
					 .andExpect(jsonPath("$.id", CoreMatchers.is(posts.get(0).getId())))
					 .andDo(print());
	}
	
	@Test
	void givenValidId_whenUpdatePost_thenReturnUpdatedPost() throws JsonProcessingException, Exception {
		// given setup mock for service method
		PostEntity expectedPost = new PostEntity(1, "First Updated Post", "This is my First updated post", 22, List.of("First Updated Tag, Second Updated Tag"), 3);
		
		given(postService.updatePost(anyInt(), any(PostEntity.class))).willReturn(expectedPost);
		
		// when call actual method using mockMvc
		ResultActions resultActions = mockMvc.perform(put("/api/posts/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(expectedPost)));
		
		// then verify the op
		MvcResult result = resultActions.andExpect(status().isOk())
					 .andDo(print())
					 .andReturn();

		assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), PostEntity.class)).isEqualTo(expectedPost);
	}
}
