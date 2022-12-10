package prosper.pets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class UsuariosConfig {

    @Bean
    public UserDetailsService users() {
        UserDetails usuario1 = User.builder()
                .username("usuario1")
                .password("{noop}s1")
                .roles("usuario")
                .build();

        UserDetails usuario2 = User.builder()
                .username("usuario2")
                .password("{noop}s2")
                .roles("usuario")
                .build();

        UserDetails usuario3 = User.builder()
                .username("admin")
                .password("{noop}admin")
                .roles("admin")
                .build();

        return new InMemoryUserDetailsManager(usuario1, usuario2, usuario3);
    }
}
