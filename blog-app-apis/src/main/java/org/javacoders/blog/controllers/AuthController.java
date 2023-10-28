package org.javacoders.blog.controllers;

import org.javacoders.blog.exceptions.ApiException;
import org.javacoders.blog.payloads.JwtAuthRequest;
import org.javacoders.blog.payloads.JwtAuthResponse;
import org.javacoders.blog.payloads.UserDto;
import org.javacoders.blog.repositories.UserRepository;
import org.javacoders.blog.security.JwtTokenHelper;
import org.javacoders.blog.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	private JwtTokenHelper jwtTokenHelper;
	private AuthenticationManager authenticationManager;
	private UserRepository userRepository;
	private UserService userService;
	
	public AuthController(JwtTokenHelper jwtTokenHelper, AuthenticationManager authenticationManager,
			UserRepository userRepository, UserService userService) {
		this.jwtTokenHelper = jwtTokenHelper;
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.userService = userService;
	}

	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) throws Exception {
		this.authenticate(request.getUsername(), request.getPassword());
		
		UserDetails userDetails = this.userRepository
				.findByEmail(request.getUsername())
				.orElseThrow();
		String token = this.jwtTokenHelper.generateToken(userDetails);
		JwtAuthResponse response = new JwtAuthResponse();
		response.setToken(token);
		
		return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
	}
	
	@PostMapping("/register") 
	public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
		
		UserDto registeredUser = this.userService.registerNewUser(userDto);
		
		return new ResponseEntity<UserDto>(registeredUser, HttpStatus.CREATED);
	}

	private void authenticate(String username, String password) throws Exception {
		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(username, password);
		try {
			this.authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			throw new ApiException("Invalid Username or password !!");
		}
		
	}
}
















