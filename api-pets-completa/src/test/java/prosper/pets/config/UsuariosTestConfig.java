package prosper.pets.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@TestConfiguration
public class UsuariosTestConfig {

    @Bean
    @Primary
    public UserDetailsService criarUsuariosTeste() {
        UserDetails usuario = User.builder()
                .username("usuario")
                .password("{noop}s")
                .roles("usuario")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}a")
                .roles("admin")
                .build();

        return new InMemoryUserDetailsManager(usuario, admin);
    }

}
