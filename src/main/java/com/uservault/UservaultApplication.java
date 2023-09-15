package com.uservault;

import com.uservault.model.Role;
import com.uservault.model.User;
import com.uservault.repository.RoleRepository;
import com.uservault.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
public class UservaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(UservaultApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository,
                          UserRepository userRepository, PasswordEncoder passwordEncoder){
		return args -> {

			List<Role> allRolesList = roleRepository.findAll();

			Set<Role> userRole = allRolesList.stream()
					.filter(role -> role.getAuthority().equalsIgnoreCase("USER"))
					.findFirst()
					.map(Set::of)
					.orElse(Set.of());

			Set<Role> adminRoles = allRolesList.stream()
					.filter(role -> role.getAuthority().equalsIgnoreCase("ADMIN")
							|| role.getAuthority().equalsIgnoreCase("USER"))
					.collect(Collectors.toSet());

			Set<Role> auditorRoles = allRolesList.stream()
					.filter(role -> role.getAuthority().equalsIgnoreCase("AUDITOR")
							|| role.getAuthority().equalsIgnoreCase("USER"))
					.collect(Collectors.toSet());

			User user = new User(null, "user@email", "user",
					passwordEncoder.encode("password"), userRole);

			User admin = new User(null, "admin@email", "admin",
					passwordEncoder.encode("password"), adminRoles);

			User auditor = new User(null, "auditor@email", "auditor",
					passwordEncoder.encode("password"), auditorRoles);

			if(userRepository.findByEmail("user@email").isEmpty()){
				userRepository.save(user);

			if(userRepository.findByEmail("admin@email").isEmpty()){
				userRepository.save(admin);
			}

			if(userRepository.findByEmail("auditor@email").isEmpty()){
				userRepository.save(auditor);
			}
			}
		};
	}
}
