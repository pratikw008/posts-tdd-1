package com.app.tdd.posts.utils;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.app.tdd.posts.model.PostEntity;
import com.app.tdd.posts.repository.PostRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PostDataLoaderUtil implements CommandLineRunner {

	private PostRepository postRepository;
	
	public PostDataLoaderUtil(PostRepository postRepository) {
		super();
		this.postRepository = postRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		if(postRepository.count() == 0) {
			String fileName = "/data/posts.json";
			List<PostEntity> posts = JsonFileConverterUtil.convertJsonToList(fileName, PostEntity.class);
			log.info("Posts Data: {}", posts);
			postRepository.saveAll(posts);
		}
	}

}
