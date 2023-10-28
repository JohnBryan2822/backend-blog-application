package org.javacoders.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.javacoders.blog.config.AppConstants;
import org.javacoders.blog.entities.Role;
import org.javacoders.blog.entities.User;
import org.javacoders.blog.exceptions.ResourceNotFoundException;
import org.javacoders.blog.payloads.UserDto;
import org.javacoders.blog.repositories.RoleRepository;
import org.javacoders.blog.repositories.UserRepository;
import org.javacoders.blog.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
	
	private UserRepository userRepository;
	private ModelMapper modelMapper;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	
	public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDto createUser(UserDto userDto) {
		User user = this.modelMapper.map(userDto, User.class);
		User savedUser = this.userRepository.save(user);
		return this.modelMapper.map(savedUser, UserDto.class);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		user.setAbout(userDto.getAbout());
		
		User updatedUser = this.userRepository.save(user);
		return this.modelMapper.map(updatedUser, UserDto.class);
	}

	@Override
	public UserDto getUserById(Integer userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
		
		return this.modelMapper.map(user, UserDto.class);
	}

	@Override
	public List<UserDto> getAllUsers() {
		
		List<User> users = this.userRepository.findAll();
		
		List<UserDto> userDtos = users.stream().map(user -> this.modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
		
		return userDtos;
	}

	@Override
	public void deleteUser(Integer userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
		this.userRepository.delete(user);
	}

	@Override
	public UserDto registerNewUser(UserDto userDto) {
		User user = this.modelMapper.map(userDto, User.class);
		
		// encoded password
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		
		// roles
		Role role = this.roleRepository.findById(AppConstants.NORMAL_USER).get();
		user.getRoles().add(role);
		
		User newUser = this.userRepository.save(user);
		
		return this.modelMapper.map(newUser, UserDto.class);
	}
}

















