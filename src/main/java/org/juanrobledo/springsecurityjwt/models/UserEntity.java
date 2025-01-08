package org.juanrobledo.springsecurityjwt.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users") // Indica que esta clase está mapeada a la tabla "user" en la base de datos.
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único del usuario, generado automáticamente por la base de datos.

    @Email // Valida que este campo contenga un formato de correo electrónico válido.
    @NotBlank // Asegura que el campo no sea nulo ni contenga solo espacios en blanco.
    @Size(min = 5, max = 50) // Limita la longitud del correo electrónico entre 5 y 50 caracteres.
    private String email; // Correo electrónico del usuario, usado para autenticación y comunicación.

    @NotBlank // Asegura que el campo no sea nulo ni contenga solo espacios en blanco.
    private String password; // Contraseña del usuario. Se almacena generalmente encriptada para mayor seguridad.

    @NotBlank // Asegura que el campo no sea nulo ni contenga solo espacios en blanco.
    @Size(min = 5, max = 50) // Limita la longitud del nombre de usuario entre 5 y 50 caracteres.
    private String username; // Nombre de usuario único para identificación.

    @ManyToMany(
            fetch = FetchType.EAGER, // Carga los roles asociados al usuario inmediatamente junto con el usuario.
            targetEntity = RoleEntity.class, // Especifica que el mapeo está relacionado con la entidad RoleEntity.
            cascade = CascadeType.PERSIST // Propaga la operación de persistencia a los roles asociados.
    )
    @JoinTable(
            name = "users_roles", // Nombre de la tabla intermedia que une usuarios y roles.
            joinColumns = @JoinColumn(name = "user_id"), // Columna que referencia al usuario.
            inverseJoinColumns = @JoinColumn(name = "role_id") // Columna que referencia al rol.
    )
    private Set<RoleEntity> role; // Conjunto de roles asociados al usuario. Se utiliza Set para evitar duplicados.
}
