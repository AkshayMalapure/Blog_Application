package com.blog.app.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="comments")
@Getter
@Setter
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int id;
	
	private String content;
	
	@ManyToOne
	private Post post;	 
	
	@ManyToOne
	private User user;
	
	@OneToMany(mappedBy = "user" ,cascade =CascadeType.ALL)
	private List<Post> posts=new ArrayList<>();
}
