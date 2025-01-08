package org.juanrobledo.springsecurityjwt;

import org.juanrobledo.springsecurityjwt.models.ERole;
import org.juanrobledo.springsecurityjwt.models.RoleEntity;
import org.juanrobledo.springsecurityjwt.models.UserEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class SpringSecurityJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityJwtApplication.class, args);
    }

    @Bean
    CommandLineRunner init(PasswordEncoder passwordEncoder){
        return args -> {
            UserEntity user = UserEntity.builder()
                    .email("juan@gmail.com")
                    .username("juan")
                    .password(passwordEncoder.encode("123456"))
                    .role(Set.of(RoleEntity.builder()
                            .name(ERole.valueOf(ERole.INVITED.name()))
                            .build()
                    ))
                    .build();
        };
    }
}
