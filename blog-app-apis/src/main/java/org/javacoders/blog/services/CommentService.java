package org.javacoders.blog.services;

import org.javacoders.blog.payloads.CommentDto;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
	CommentDto createComment(CommentDto commentDto, Integer postId);
	void deleteComment(Integer commentId);
}
