package com.app.tdd.posts.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.tdd.posts.model.PostEntity;
import com.app.tdd.posts.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

	@Mock
	private PostRepository postRepository;
	
	@InjectMocks
	private PostServiceImpl postServiceImpl;
	
	private List<PostEntity> posts;
	
	@BeforeEach
	void setUp() {
		posts = List.of(
					new PostEntity(1, "First Post", "This is my First post", 22, List.of("First Tag, Second Tag"), 2),
					new PostEntity(2, "Second Post", "This is my Second post", 22, List.of("First Tag, Second Tag"), 2)
				);
	}
	
	@Test
	void givenListPost_whenFindAll_thenReturnListPost() {
		// given setup mock for repository method
		given(postRepository.findAll()).willReturn(posts);
		
		// when call actual method to test
		List<PostEntity> findAll = postServiceImpl.findAll();
		
		// then verify the op
		assertThat(findAll).isNotEmpty();
		assertIterableEquals(posts, findAll);
	}
	
	@Test
	void givenListEmpty_whenFindAll_thenReturnEmptyList() {
		// given setup mock for empty list
		given(postRepository.findAll()).willReturn(Collections.emptyList());
		
		// when call actual method to test
		List<PostEntity> findAll = postServiceImpl.findAll();
		
		// then verify the op
		assertThat(findAll).isEmpty();
	}
	
	@Test
	void givenValidId_whenFindPostById_thenReturnPost() {
		// given setup mock for findPostById method
		given(postRepository.findById(anyInt())).willReturn(Optional.of(posts.get(0)));
		
		// when call actual method to test
		PostEntity postEntity = postServiceImpl.findPostById(1);
		
		// then verify the op
		assertThat(postEntity).isEqualTo(posts.get(0));
	}
	
	@Test
	void givenInvalidId_whenFindPostById_thenThrowException() {
		// given setup mock which causes exception
		BDDMockito.willThrow(new RuntimeException("Invalid ID: 0")).given(postRepository).findById(anyInt());
		//given(postRepository.findById(anyInt())).willThrow(new RuntimeException("Invalid ID: 0"));
		
		// when call assertThrows
		RuntimeException re = assertThrows(RuntimeException.class, () -> postServiceImpl.findPostById(0));
		
		// then verify the op
		assertThat(re.getMessage()).isEqualTo("Invalid ID: 0");
	}
	
	@Test
	void givenPost_whenSavePost_thenCreatePost() {
		// given setup mock for save
		given(postRepository.save(any(PostEntity.class))).willReturn(posts.get(0));
		
		// when call actual method to test
		PostEntity savedPost = postServiceImpl.savePost(posts.get(0));
		
		// then verify the op
		assertThat(savedPost).isEqualTo(posts.get(0));
	}
	
	@Test
	void givenValidIdPost_whenUpdatePost_thenReturnUpdatedPost() {
		// given setup mock for repo method
		given(postRepository.findById(anyInt())).willReturn(Optional.ofNullable(posts.get(0)));
		
		PostEntity expectedPost = new PostEntity(1, "First Updated Post", "This is my First updated post", 22, List.of("First Updated Tag, Second Updated Tag"), 3);
		
		given(postRepository.save(any(PostEntity.class))).willReturn(expectedPost);
		
		// when call actual method to test
		PostEntity updatedPost = postServiceImpl.updatePost(posts.get(0).getId(), expectedPost);
		
		// then verify the op
		assertThat(updatedPost).isEqualTo(expectedPost);
	}
	
	@Test
	void givenInvalidIdPost_whenUpdatePost_thenThrowsException() {
		// given setup mock which causes execption
		given(postRepository.findById(anyInt())).willReturn(Optional.empty());
		
		// when call assertThrows
		assertThrows(RuntimeException.class, () -> postServiceImpl.updatePost(posts.get(0).getId(), posts.get(0)));
		
		// then verify the op
		verify(postRepository, never()).save(any(PostEntity.class));
	}
}
