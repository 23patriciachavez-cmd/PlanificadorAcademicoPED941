package sv.edu.udb.ped941.seguridad;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;

import sv.edu.udb.ped941.modelo.Usuario;
import sv.edu.udb.ped941.servicio.AutenticacionService;

@Configuration
public class SeguridadWebConfig {

    @Bean
    public AuthenticationProvider proveedorAutenticacion() {

        AutenticacionService servicio =
                new AutenticacionService();

        return new AuthenticationProvider() {

            @Override
            public Authentication authenticate(
                    Authentication authentication) {

                String nombre =
                        authentication.getName();

                String password =
                        String.valueOf(
                                authentication.getCredentials());

                Usuario usuario =
                        servicio.iniciarSesion(
                                nombre,
                                password);

                if (usuario == null) {

                    throw new BadCredentialsException(
                            "Credenciales incorrectas.");
                }

                String rol =
                        "ROLE_" + usuario.getRol().name();

                return UsernamePasswordAuthenticationToken.authenticated(
                        nombre,
                        null,
                        List.of(
                                new SimpleGrantedAuthority(rol)));
            }

            @Override
            public boolean supports(
                    Class<?> authentication) {

                return UsernamePasswordAuthenticationToken.class
                        .isAssignableFrom(authentication);
            }
        };
    }

    @Bean
    public SecurityFilterChain configurarSeguridad(
            HttpSecurity http,
            AuthenticationProvider proveedor) throws Exception {

        http
                .authenticationProvider(proveedor)

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/login",
                                "/css/**",
                                "/error")
                        .permitAll()

                        .requestMatchers(
                                "/inicio")
                        .authenticated()

                        .requestMatchers(
                                "/admin",
                                "/admin/**")
                        .hasRole("ADMINISTRADOR")

                        .requestMatchers(
                                "/",
                                "/materias/**",
                                "/electiva/**")
                        .hasRole("ESTUDIANTE")

                        .anyRequest()
                        .denyAll()
                )

                .formLogin(form -> form

                        .loginPage("/login")

                        .loginProcessingUrl("/login")

                        .defaultSuccessUrl(
                                "/inicio",
                                true)

                        .failureUrl("/login?error")

                        .permitAll()
                )

                .logout(logout -> logout

                        .logoutUrl("/logout")

                        .logoutSuccessUrl("/login?logout")

                        .invalidateHttpSession(true)

                        .deleteCookies("JSESSIONID")

                        .permitAll()
                );

        return http.build();
    }
}
