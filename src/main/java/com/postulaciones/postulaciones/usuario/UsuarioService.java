package com.postulaciones.postulaciones.usuario;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.postulaciones.postulaciones.estado.Estado;
import com.postulaciones.postulaciones.estado.EstadoRepository;
import com.postulaciones.postulaciones.exception.CredencialesInvalidasException;
import com.postulaciones.postulaciones.exception.RecursoYaExisteException;
import com.postulaciones.postulaciones.security.JwtService;
import com.postulaciones.postulaciones.usuario.dto.LoginDto;
import com.postulaciones.postulaciones.usuario.dto.LoginResponseDto;
import com.postulaciones.postulaciones.usuario.dto.RegistroDto;
import com.postulaciones.postulaciones.usuario.dto.UsuarioResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EstadoRepository estadoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public UsuarioResponseDto registar(RegistroDto dto) {

        // 1. Comprobar si ya existe el correo
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new RecursoYaExisteException("El correo ya esta registrado");
        }

        // 2. Crear Usuario
        Usuario usuario = new Usuario();

        usuario.setCorreo(dto.getCorreo());
        usuario.setNombre(dto.getNombre());

        // 3. Hashear Clave
        usuario.setClave(
                passwordEncoder.encode(dto.getClave()));

        // 4. Guardar Usuario
        usuarioRepository.save(usuario);

        // 5. Crear estados iniciales
        crearEstadosIniciales(usuario);

        // 6. Crear Respuesta
        UsuarioResponseDto respuesta = new UsuarioResponseDto();

        respuesta.setId(usuario.getId());
        respuesta.setCorreo(usuario.getCorreo());
        respuesta.setNombre(usuario.getNombre());

        return respuesta;
    }

    private void crearEstadosIniciales(Usuario usuario) {

        Estado aplicado = new Estado();
        aplicado.setUsuario(usuario);
        aplicado.setNombre("Aplicado");
        aplicado.setColor("#3B82F6");
        aplicado.setPorDefecto(true);

        Estado enProceso = new Estado();
        enProceso.setUsuario(usuario);
        enProceso.setNombre("En Proceso");
        enProceso.setColor("#F59E0B");
        enProceso.setPorDefecto(false);

        Estado rechazado = new Estado();
        rechazado.setUsuario(usuario);
        rechazado.setNombre("Rechazado");
        rechazado.setColor("#EF4444");
        rechazado.setPorDefecto(false);

        Estado aceptado = new Estado();
        aceptado.setUsuario(usuario);
        aceptado.setNombre("Aceptado");
        aceptado.setColor("#10B981");
        aceptado.setPorDefecto(false);

        estadoRepository.save(aplicado);
        estadoRepository.save(enProceso);
        estadoRepository.save(rechazado);
        estadoRepository.save(aceptado);

    }

    public LoginResponseDto login(LoginDto dto) {

        // 1. Verificar que exista el usuario
        Usuario usuario = usuarioRepository
                .findByCorreo(dto.getCorreo())
                .orElseThrow(() -> new CredencialesInvalidasException("Correo o contraseña incorrectos"));

        // 2. Verificar que la clave hasheada coincida
        if (!passwordEncoder.matches(dto.getClave(), usuario.getClave())) {
            throw new CredencialesInvalidasException("Correo o contraseña incorrectos");
        }

        String token = jwtService.generarToken(usuario.getCorreo());

        UsuarioResponseDto respuesta = new UsuarioResponseDto();

        respuesta.setId(usuario.getId());
        respuesta.setCorreo(usuario.getCorreo());
        respuesta.setNombre(usuario.getNombre());

        return new LoginResponseDto(token, respuesta);

    }
}
