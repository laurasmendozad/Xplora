package com.backend.controller;
import com.backend.dto.entada.LoginDto;
import com.backend.dto.entada.RegistroUsuarioDTO;
import com.backend.dto.salida.UsuarioDto;
import com.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<UsuarioDto> registerUser(@Valid @RequestBody RegistroUsuarioDTO registerUserDto) {
        UsuarioDto usuarioDto = authService.registerUser(registerUserDto);
        return new ResponseEntity<>(usuarioDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody LoginDto loginDto) {
        String token = authService.loginUser(loginDto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser() {
        // Implementar logout aquí, el token se invalidará en el frontend
        return ResponseEntity.ok("Cierre de sesión exitoso");
    }
}