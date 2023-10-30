package com.app.tdd.posts.repository;

import org.springframework.data.repository.ListCrudRepository;

import com.app.tdd.posts.model.PostEntity;

public interface PostRepository extends ListCrudRepository<PostEntity, Integer> {

}
