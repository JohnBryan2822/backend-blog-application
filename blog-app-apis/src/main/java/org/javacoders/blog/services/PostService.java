package org.javacoders.blog.services;

import java.util.List;

import org.javacoders.blog.payloads.PostDto;
import org.javacoders.blog.payloads.PostResponse;
import org.springframework.stereotype.Service;

@Service
public interface PostService {
	
	PostDto createPost(PostDto postDto, Integer userId, Integer categoryId);
	PostDto updatePost(PostDto postDto, Integer postId);
	void deletePost(Integer postId);
	PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
	PostDto getPostById(Integer postId);
	List<PostDto> getPostsByCategory(Integer categoryId);
	List<PostDto> getPostsByUser(Integer userId);
	List<PostDto> searchPosts(String keyword);
}
