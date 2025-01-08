package org.juanrobledo.springsecurityjwt.security;

import org.juanrobledo.springsecurityjwt.security.filters.JwtAuthenticationFilter;
import org.juanrobledo.springsecurityjwt.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailsService userDetailsService ;

    // Configura la cadena de filtros de seguridad
    @Bean // Marca este metodo como un componente gestionado por Spring (un bean).
    public SecurityFilterChain securityFilterChain(HttpSecurity http,AuthenticationManager authenticationManager) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);


        return http
                .csrf(config -> config.disable())
                // Desactiva la protección contra ataques CSRF (Cross-Site Request Forgery).
                // Se suele desactivar en APIs REST porque no usan formularios HTML tradicionales.

                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/hello").permitAll();
                    // Permite el acceso público a la ruta "/hello" sin autenticación.

                    auth.anyRequest().authenticated();
                    // Requiere autenticación para cualquier otra ruta no configurada explícitamente.
                })

                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    // Configura la política de sesiones como STATELESS.
                    // Esto significa que no se guarda el estado de la sesión en el servidor.
                    // Es útil para APIs REST que suelen depender de JWT o tokens para autenticación.
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                //.httpBasic(basic -> {
                    // Habilita la autenticación HTTP básica.
                    // Esto permite enviar credenciales en los encabezados HTTP (username y password).
                //})

                .build();
        // Construye y retorna la cadena de filtros configurada.
    }

    // Configura un servicio de usuarios en memoria
    //@Bean
    //UserDetailsService userDetailsService() {
    //    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    //    // `InMemoryUserDetailsManager` es una implementación de `UserDetailsService`.
    //    // Guarda usuarios en memoria (no en una base de datos).
    //
    //    manager.createUser(
    //            User.withUsername("santiago")
    //                    // Define un usuario con el nombre "santiago".
    //                    .password("1234")
    //                    // Asigna la contraseña "1234" al usuario.
    //
    //                    .roles()
    //                    // Se pueden asignar roles al usuario. Aquí está vacío (sin roles específicos).
    //
    //                    .build());
    //    // Construye el objeto UserDetails y lo agrega al manager.

    //    return manager;
    // Retorna el `UserDetailsService` para que Spring Security lo use.
    //}

    // Configura el codificador de contraseñas
    @Bean
    PasswordEncoder passwordEncoder() {
        //return NoOpPasswordEncoder.getInstance();
        // Usa un codificador de contraseñas sin cifrado (NoOpPasswordEncoder).
        // Esto **no debe usarse en producción**, ya que almacena las contraseñas en texto plano.
        // En entornos reales, utiliza `BCryptPasswordEncoder` o similares.

        return new BCryptPasswordEncoder();
    }

    // Configura el administrador de autenticación
    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception {
        return (AuthenticationManager) httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                // Indica que el administrador de autenticación usará el `UserDetailsService` configurado.
                .passwordEncoder(passwordEncoder);
        // Configura el codificador de contraseñas.
        // Retorna el objeto `AuthenticationManager` configurado.
    }
    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }
}
