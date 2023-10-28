package org.javacoders.blog.services;

import java.util.List;

import org.javacoders.blog.payloads.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
	
	UserDto registerNewUser(UserDto userDto);
	UserDto createUser(UserDto user);
	UserDto updateUser(UserDto user, Integer userId);
	UserDto getUserById(Integer userId);
	List<UserDto> getAllUsers();
	void deleteUser(Integer userId);
}
