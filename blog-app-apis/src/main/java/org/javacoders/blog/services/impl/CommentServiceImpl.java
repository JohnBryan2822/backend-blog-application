package org.javacoders.blog.services.impl;

import org.javacoders.blog.entities.Comment;
import org.javacoders.blog.entities.Post;
import org.javacoders.blog.exceptions.ResourceNotFoundException;
import org.javacoders.blog.payloads.CommentDto;
import org.javacoders.blog.repositories.CommentRepository;
import org.javacoders.blog.repositories.PostRepository;
import org.javacoders.blog.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
	
	private PostRepository postRepository;
	private CommentRepository commentRepository;
	private ModelMapper modelMapper;

	public CommentServiceImpl(PostRepository postRepository, CommentRepository commentRepository,
			ModelMapper modelMapper) {
		this.postRepository = postRepository;
		this.commentRepository = commentRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId) {
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));
		Comment comment = this.modelMapper.map(commentDto, Comment.class);
		comment.setPost(post);
		Comment savedComment = this.commentRepository.save(comment);
		return this.modelMapper.map(savedComment, CommentDto.class);
	}

	@Override
	public void deleteComment(Integer commentId) {
		Comment comment = this.commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "comment id", commentId));
		this.commentRepository.delete(comment);
	}

}











