package org.javacoders.blog.services;

import org.javacoders.blog.payloads.CommentDto;

public interface CommentService {
	
	CommentDto createComments(CommentDto commentDto, Integer postId);
	void deleteComment(Integer commentId);

}
