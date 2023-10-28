package org.javacoders.blog.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.javacoders.blog.entities.Category;
import org.javacoders.blog.entities.Post;
import org.javacoders.blog.entities.User;
import org.javacoders.blog.exceptions.ResourceNotFoundException;
import org.javacoders.blog.payloads.PostDto;
import org.javacoders.blog.payloads.PostResponse;
import org.javacoders.blog.repositories.CategoryRepository;
import org.javacoders.blog.repositories.PostRepository;
import org.javacoders.blog.repositories.UserRepository;
import org.javacoders.blog.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {
	
	private PostRepository postRepository;
	private ModelMapper modelMapper;
	private UserRepository userRepository;
	private CategoryRepository categoryRepository;
	
	public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper, UserRepository userRepository,
			CategoryRepository categoryRepository) {
		this.postRepository = postRepository;
		this.modelMapper = modelMapper;
		this.userRepository = userRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "user id", userId));
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));
		
		Post post = this.modelMapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);
		
		Post savedPost = this.postRepository.save(post);
		return this.modelMapper.map(savedPost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());
		
		Post updatedPost = this.postRepository.save(post);
		return this.modelMapper.map(updatedPost, PostDto.class);
	}

	@Override
	public void deletePost(Integer postId) {
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));
		this.postRepository.delete(post);
	}

	@Override
	public PostResponse getAllPosts(Integer pageNumber, Integer pageSize,
			String sortBy, String sortDir) {
		
		Sort sort = sortDir.equalsIgnoreCase("desc")?
				Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
		
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		
		Page<Post> pagePost = this.postRepository.findAll(p);
		
		List<Post> allPosts = pagePost.getContent();
		
		List<PostDto> postDtos = allPosts.stream().map(post -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		PostResponse postResponse = new PostResponse();
		postResponse.setContent(postDtos);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());
		
		return postResponse;
	}

	@Override
	public PostDto getPostById(Integer postId) {
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));
		return this.modelMapper.map(post, PostDto.class);
	}

	@Override
	public List<PostDto> getPostsByCategory(Integer categoryId) {
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));
		List<Post> posts = this.postRepository.findByCategory(category);
		List<PostDto> postDtos = posts.stream().map(post -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		return postDtos;
	}

	@Override
	public List<PostDto> getPostsByUser(Integer userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "user id", userId));
		List<Post> posts = this.postRepository.findByUser(user);
		
		List<PostDto> postDtos = posts.stream().map(post -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		return postDtos;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		List<Post> posts = this.postRepository.findByTitleContaining(keyword);
		List<PostDto> postDtos = posts.stream().map(post -> this.modelMapper
				.map(post, PostDto.class))
				.collect(Collectors.toList());
		return postDtos;
	}

}
