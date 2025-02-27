package com.backend.dto.entada;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginDto {

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Correo electrónico no válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}