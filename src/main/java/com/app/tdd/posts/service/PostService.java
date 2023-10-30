package com.app.tdd.posts.service;

import java.util.List;

import com.app.tdd.posts.model.PostEntity;

public interface PostService {

	public List<PostEntity> findAll();
	
	public PostEntity findPostById(int id);
	
	public PostEntity savePost(PostEntity postEntity);
	
	public PostEntity updatePost(int id, PostEntity postEntity);
}
