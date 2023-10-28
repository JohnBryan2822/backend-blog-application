package org.javacoders.blog;

import java.util.List;

import org.javacoders.blog.config.AppConstants;
import org.javacoders.blog.entities.Role;
import org.javacoders.blog.repositories.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BlogAppApisApplication implements CommandLineRunner {
	
	private RoleRepository roleRepository;

	public BlogAppApisApplication(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(BlogAppApisApplication.class, args);
	}
	
	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			Role role1 = new Role();
			role1.setId(AppConstants.ADMIN_USER);
			role1.setName("ADMIN_USER");
			
			Role role2 = new Role();
			role2.setId(AppConstants.NORMAL_USER);
			role2.setName("NORMAL_USER");
			
			List<Role> result = this.roleRepository.saveAll(List.of(role1, role2));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}












