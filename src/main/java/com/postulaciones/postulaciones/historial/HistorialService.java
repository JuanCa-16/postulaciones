package com.postulaciones.postulaciones.historial;

import org.springframework.stereotype.Service;

import com.postulaciones.postulaciones.postulacion.Postulacion;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistorialService {
    private final HistorialRepository historialRepository;

    public void registrarCambio(
            Postulacion postulacion,
            String campo,
            String valorAntiguo,
            String valorNuevo) {

        Historial historial = new Historial();

        historial.setPostulacion(postulacion);
        historial.setCampoActualizado(campo);
        historial.setValorAntiguo(valorAntiguo);
        historial.setValorNuevo(valorNuevo);

        historialRepository.save(historial);
    }
}
