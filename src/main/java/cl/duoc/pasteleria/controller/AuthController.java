package cl.duoc.pasteleria.controller;

import cl.duoc.pasteleria.dto.LoginRequest;
import cl.duoc.pasteleria.model.Usuario;
import cl.duoc.pasteleria.repository.UsuarioRepository;
import cl.duoc.pasteleria.service.UsuarioService;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authentication;
    private final JwtEncoder encoder;
    private final UsuarioRepository usuarios;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest in) {
        try {
            authentication.authenticate(new UsernamePasswordAuthenticationToken(in.correo(), in.clave()));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body(Map.of("message", "Correo o clave incorrectos"));
        }
        return ResponseEntity.ok(crearToken(usuarios.findByCorreo(in.correo()).get()));
    }

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario registro(@Valid @RequestBody Usuario u) {
        return usuarioService.registrar(u);
    }

    // renueva el token
    @PostMapping("/refresh")
    public Map<String, Object> refresh(Authentication auth) {
        Usuario u = usuarios.findByCorreo(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no existe"));
        return crearToken(u);
    }

    private Map<String, Object> crearToken(Usuario u) {
        Instant ahora = Instant.now();
        long segundos = 900;
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("pasteleria-api")
                .subject(u.getCorreo())
                .issuedAt(ahora)
                .expiresAt(ahora.plusSeconds(segundos))
                .claim("rol", u.getRol())
                .claim("nombre", u.getNombre())
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return Map.of("accessToken", token, "tokenType", "Bearer", "expiresIn", segundos);
    }
}
