package com.app.tdd.posts.controller.integration;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.app.tdd.posts.containers.AbstractTestContainer;
import com.app.tdd.posts.model.PostEntity;
import com.app.tdd.posts.repository.PostRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PostControllerIntegrationTest extends AbstractTestContainer{
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private List<PostEntity> posts;
	
	@BeforeEach
	void setUp() {
		postRepository.deleteAll();
		posts = List.of(
				new PostEntity("First Post", "This is my First post", 22, List.of("First Tag, Second Tag"), 2),
				new PostEntity("Second Post", "This is my Second post", 22, List.of("First Tag, Second Tag"), 2)
			);
	}
	
	@Test
	void givenPostList_whenFindAll_thenRetuenPostList() throws Exception {
		// given setup 
		List<PostEntity> expected = postRepository.saveAll(posts);

		// when call actual using mockMvc
		ResultActions resultActions = mockMvc.perform(get("/api/posts"));
		
		// then verify the op
		resultActions.andExpect(MockMvcResultMatchers.status().isOk())
										.andExpect(jsonPath("$.size()", CoreMatchers.is(expected.size())))
								        .andExpect(jsonPath("$[0].title", CoreMatchers.is("First Post")))
								        .andExpect(jsonPath("$[0].body", CoreMatchers.is("This is my First post")))
								        .andExpect(jsonPath("$[1].title", CoreMatchers.is("Second Post")))
								        .andExpect(jsonPath("$[1].body", CoreMatchers.is("This is my Second post")))
					 					.andDo(print());
	}
	
	@Test
	void givenValidId_whenFindById_thenRetunPost() throws Exception {
		// given setup post
		PostEntity expected = postRepository.save(posts.get(0));
		
		// when call actual method using 
		ResultActions resultActions = mockMvc.perform(get("/api/posts/{id}", expected.getId()));
		
		// then verify the op
		resultActions.andExpect(MockMvcResultMatchers.status().isOk())
					 .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
					 .andDo(print());
	}
	
	@Test
	void givenPost_whenCreatePost_thenReturnCreatedPost() throws JsonProcessingException, Exception {
		
		// when call actual method using mockMvc
		ResultActions resultActions = mockMvc.perform(post("/api/posts")
											 .contentType(MediaType.APPLICATION_JSON)
											 .content(objectMapper.writeValueAsString(posts.get(0))));
		
		// then verify the op
		resultActions.andExpect(status().isCreated())
					 .andExpect(jsonPath("$.id").exists())
					 .andDo(print())
					 .andReturn();
	}
	
	@Test
	void givenValidId_whenUpdatePost_thenReturnUpdatedPost() throws JsonProcessingException, Exception {
		// given setup 
		PostEntity saved = postRepository.save(posts.get(0));
		PostEntity expectedPost = new PostEntity("First Updated Post", "This is my First updated post", 22, List.of("First Updated Tag, Second Updated Tag"), 3);
		expectedPost.setId(saved.getId());
		
		// when call actual method using mockMvc
		ResultActions resultActions = mockMvc.perform(put("/api/posts/{id}", saved.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(expectedPost)));
		
		// then verify the op
		MvcResult result = resultActions.andExpect(status().isOk())
					 .andDo(print())
					 .andReturn();

		assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), PostEntity.class)).isEqualTo(expectedPost);
	}
	
}
