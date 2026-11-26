package cl.duoc.pasteleria.config;

import cl.duoc.pasteleria.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import java.util.Base64;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // los usuarios se leen de la base de datos
    @Bean
    UserDetailsService users(UsuarioRepository repo) {
        return correo -> repo.findByCorreo(correo)
                .map(u -> User.withUsername(u.getCorreo()).password(u.getClave()).roles(u.getRol()).build())
                .orElseThrow(() -> new UsernameNotFoundException(correo));
    }

    @Bean
    AuthenticationManager authenticationManager(UserDetailsService users, PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(users);
        provider.setPasswordEncoder(encoder);
        return new ProviderManager(provider);
    }

    @Bean
    SecretKey jwtKey(@Value("${jwt.secret}") String secret) {
        return new SecretKeySpec(Base64.getDecoder().decode(secret), "HmacSHA256");
    }

    @Bean
    JwtEncoder jwtEncoder(SecretKey key) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(key));
    }

    @Bean
    JwtDecoder jwtDecoder(SecretKey key) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer("pasteleria-api"));
        return decoder;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, ObjectMapper mapper) throws Exception {
        AuthenticationEntryPoint sinToken = (request, response, ex) -> {
            response.setStatus(401);
            response.setContentType("application/json");
            mapper.writeValue(response.getOutputStream(), Map.of("message", "Inicia sesion con un token valido"));
        };

        // el rol viene dentro del token
        JwtGrantedAuthoritiesConverter roles = new JwtGrantedAuthoritiesConverter();
        roles.setAuthoritiesClaimName("rol");
        roles.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter convertidor = new JwtAuthenticationConverter();
        convertidor.setJwtGrantedAuthoritiesConverter(roles);

        http.cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/v1/auth/login", "/api/v1/auth/registro", "/error",
                        "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/productos/**", "/api/v1/categorias/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/contacto").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/boletas").hasAnyRole("ADMIN", "VENDEDOR")
                .requestMatchers("/api/v1/boletas/**", "/api/v1/auth/refresh").authenticated()
                .requestMatchers("/api/v1/productos/**", "/api/v1/categorias/**",
                        "/api/v1/usuarios/**", "/api/v1/contacto").hasRole("ADMIN")
                .anyRequest().denyAll())
            .exceptionHandling(e -> e.authenticationEntryPoint(sinToken))
            .oauth2ResourceServer(o -> o
                .jwt(j -> j.jwtAuthenticationConverter(convertidor))
                .authenticationEntryPoint(sinToken));
        return http.build();
    }
}
