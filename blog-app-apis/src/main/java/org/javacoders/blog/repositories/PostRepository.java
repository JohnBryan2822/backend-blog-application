package org.javacoders.blog.repositories;

import java.util.List;

import org.javacoders.blog.entities.Category;
import org.javacoders.blog.entities.Post;
import org.javacoders.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
	
	List<Post> findByUser(User user);
	List<Post> findByCategory(Category category);
	
	List<Post> findByTitleContaining(String title);
	// if the above line is not working
//	@Query("Select p from Post p where p.title like :key")
//	List<Post> searchByTitle(@Param("key") String title);
}
