package org.javacoders.blog.security;

import org.javacoders.blog.entities.User;
import org.javacoders.blog.exceptions.ResourceNotFoundException;
import org.javacoders.blog.repositories.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

	private UserRepo userRepo;
	
	public CustomUserDetailService(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// loading user from database by username
		User user = this.userRepo.findByEmail(username)
			.orElseThrow(() -> new ResourceNotFoundException("User", " email : " + username, 0));
		return user;
	}
}
