package com.backend.service;
import com.backend.dto.entada.LoginDto;
import com.backend.dto.entada.RegistroUsuarioDTO;
import com.backend.dto.salida.UsuarioDto;
import com.backend.entity.Usuario;
import com.backend.repository.UsuarioRepository;
import com.backend.security.JwtTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public AuthService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioDto registerUser(RegistroUsuarioDTO registerUserDto) {
        Usuario usuario = modelMapper.map(registerUserDto, Usuario.class);
        usuario.setContrasena(bCryptPasswordEncoder.encode(registerUserDto.getContrasena()));
        usuario.setFechaRegistro(new Date());
        // Aquí puedes asignar un rol por defecto
        // usuario.setRol(rolRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Role not found")));
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return modelMapper.map(savedUsuario, UsuarioDto.class);
    }

    public String loginUser(LoginDto loginDto) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(loginDto.getEmail());
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        if (!bCryptPasswordEncoder.matches(loginDto.getContrasena(), usuario.getContrasena())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return jwtTokenProvider.generateToken((Authentication) usuario);
    }
}