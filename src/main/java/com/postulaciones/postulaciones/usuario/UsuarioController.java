package com.postulaciones.postulaciones.usuario;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postulaciones.postulaciones.common.dto.RespuestaDto;
import com.postulaciones.postulaciones.usuario.dto.LoginDto;
import com.postulaciones.postulaciones.usuario.dto.LoginResponseDto;
import com.postulaciones.postulaciones.usuario.dto.RegistroDto;
import com.postulaciones.postulaciones.usuario.dto.UsuarioResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<RespuestaDto<UsuarioResponseDto>> registrar(@Valid @RequestBody RegistroDto dto) {

        UsuarioResponseDto usuario = usuarioService.registar(dto);

        RespuestaDto<UsuarioResponseDto> respuesta = new RespuestaDto<UsuarioResponseDto>(
                false,
                "Usuario registrado correctamente",
                usuario);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);

    }

    @PostMapping("/login")
    public ResponseEntity<RespuestaDto<LoginResponseDto>> login(@Valid @RequestBody LoginDto dto) {

        LoginResponseDto usuario = usuarioService.login(dto);

        RespuestaDto<LoginResponseDto> respuesta = new RespuestaDto<>(
                false,
                "Inicio de sesión correcto",
                usuario);

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/prueba")
    public ResponseEntity<RespuestaDto<String>> prueba() {

        return ResponseEntity.ok(
                new RespuestaDto<>(
                        false,
                        "Acceso autorizado",
                        "El token JWT es válido"));
    }

}
