package com.app.tdd.posts.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.tdd.posts.model.PostEntity;
import com.app.tdd.posts.repository.PostRepository;
import com.app.tdd.posts.service.PostService;

@Service
public class PostServiceImpl implements PostService {

	private PostRepository postRepository;
	
	public PostServiceImpl(PostRepository postRepository) {
		super();
		this.postRepository = postRepository;
	}

	@Override
	public List<PostEntity> findAll() {
		return postRepository.findAll();
	}

	@Override
	public PostEntity findPostById(int id) {
		return postRepository.findById(id)
							 .orElseThrow(() -> new RuntimeException("Invalid ID: "+id));
	}
	
	@Override
	public PostEntity savePost(PostEntity postEntity) {
		return postRepository.save(postEntity);
	}
	
	@Override
	public PostEntity updatePost(int id, PostEntity postEntity) {
		return postRepository.findById(id)
					.map(post -> updatePost(post, postEntity))
					.orElseThrow(() -> new RuntimeException("Invalid ID: "+id));
	}
	
	private PostEntity updatePost(PostEntity existing, PostEntity updatingPost) {
		if(updatingPost.getBody() != null || !updatingPost.getBody().isEmpty()) {
			existing.setBody(updatingPost.getBody());
		}
		
		if(updatingPost.getTitle() != null || !updatingPost.getTitle().isEmpty()) {
			existing.setTitle(updatingPost.getTitle());
		}
		
		if(updatingPost.getTags() != null || !updatingPost.getTags().isEmpty()) {
			existing.setTags(updatingPost.getTags());
		}
		
		if(updatingPost.getReactions() != null) {
			existing.setReactions(updatingPost.getReactions());
		}
		
		return postRepository.save(existing);
	}
}
