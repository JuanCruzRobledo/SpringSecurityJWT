package org.juanrobledo.springsecurityjwt.controllers;

import jakarta.validation.Valid;
import org.juanrobledo.springsecurityjwt.controllers.request.CreateUserDTO;
import org.juanrobledo.springsecurityjwt.models.ERole;
import org.juanrobledo.springsecurityjwt.models.RoleEntity;
import org.juanrobledo.springsecurityjwt.models.UserEntity;
import org.juanrobledo.springsecurityjwt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class PrincipalController {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public PrincipalController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/helloSecured")
    public String helloSecured(){
        return "Hello Secured";
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        // Metodo HTTP POST que expone la URL "/createUser".
        // Este metodo recibe un objeto JSON en el cuerpo de la petición, lo convierte en un objeto `CreateUserDTO`
        // y valida sus campos gracias a la anotación `@Valid`.

        Set<RoleEntity> roles = createUserDTO.getRoles().stream()
                // Convierte la lista de roles obtenida del DTO (List<String>) en un flujo de datos (stream),
                // permitiendo procesar cada elemento de forma funcional.

                .map(role -> RoleEntity.builder()
                        .name(ERole.valueOf(role))
                        // Convierte cada cadena (como "ADMIN" o "USER") en un valor del enum `ERole`.
                        // `ERole.valueOf(role)` lanza una excepción si el rol no coincide con un valor del enum.
                        .build())
                // Crea una instancia de `RoleEntity` con el valor del enum como atributo `name`.
                .collect(Collectors.toSet());
        // Recolecta las instancias de `RoleEntity` en un `Set`, eliminando posibles duplicados.

        UserEntity user = UserEntity.builder()
                .username(createUserDTO.getUsername()) // Establece el nombre de usuario obtenido del DTO.
                .password(passwordEncoder.encode(createUserDTO.getPassword())) 
                .email(createUserDTO.getEmail()) // Establece el correo electrónico obtenido del DTO.
                .role(roles) // Asigna el conjunto de roles previamente construido al usuario.
                .build();
        // Crea una nueva instancia de `UserEntity` utilizando el patrón Builder,
        // con los datos proporcionados en el `createUserDTO`.

        userRepository.save(user);
        // Guarda el nuevo usuario en la base de datos utilizando el repositorio de usuarios.

        return ResponseEntity.ok().body(user);
        // Devuelve una respuesta HTTP 200 (OK) con el objeto `user` en el cuerpo de la respuesta.
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestParam String id){
        userRepository.deleteById(Long.parseLong(id));
        return ResponseEntity.ok().build();
    }
}
