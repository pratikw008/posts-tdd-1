package com.app.tdd.posts.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.app.tdd.posts.model.PostEntity;
import com.app.tdd.posts.service.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {
	
	private PostService postService;

	public PostController(PostService postService) {
		super();
		this.postService = postService;
	}
	
	@GetMapping
	public List<PostEntity> findAll() {
		return postService.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PostEntity> findPostById(@PathVariable("id") int id) {
		PostEntity postEntity = postService.findPostById(id);
		return ResponseEntity.ok(postEntity);
	}
	
	@PostMapping
	public ResponseEntity<PostEntity> createPost(@RequestBody PostEntity postEntity) {
		PostEntity createdPost = postService.savePost(postEntity);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
												  .path("/{id}")
												  .buildAndExpand(createdPost.getId())
												  .toUri();
		return ResponseEntity.created(location).body(createdPost);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PostEntity> updatePost(@PathVariable("id")int id, @RequestBody PostEntity postEntity) {
		PostEntity updatedPost = postService.updatePost(id, postEntity);
		return ResponseEntity.ok(updatedPost);
	}
}
 