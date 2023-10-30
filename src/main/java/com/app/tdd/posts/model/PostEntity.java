package com.app.tdd.posts.model;

import java.util.List;

import com.app.tdd.posts.utils.StringListConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts_tab")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class PostEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String title;
	
	@Lob
	@Column(columnDefinition = "TEXT")
	private String body;
	
	private Integer userId;
	
	@Convert(converter = StringListConverter.class)
	private List<String> tags;
	
	private Integer reactions;

	public PostEntity(String title, String body, Integer userId, List<String> tags, Integer reactions) {
		super();
		this.title = title;
		this.body = body;
		this.userId = userId;
		this.tags = tags;
		this.reactions = reactions;
	}
}
