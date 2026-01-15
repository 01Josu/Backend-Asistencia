package com.grupobarreto.asistencia.service;

import com.grupobarreto.asistencia.dto.MarcarAsistenciaRequest;
import com.grupobarreto.asistencia.dto.MarcarAsistenciaResponse;

import com.grupobarreto.asistencia.model.*;
import com.grupobarreto.asistencia.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AsistenciaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private HorarioEmpleadoRepository horarioEmpleadoRepository;

    public MarcarAsistenciaResponse marcarEntrada(MarcarAsistenciaRequest request) {

        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElse(null);

        if (usuario == null) {
            return new MarcarAsistenciaResponse(false, "Usuario no existe", null, null, null);
        }

        Empleado empleado = usuario.getEmpleado();

        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        Asistencia asistencia = asistenciaRepository
                .findByEmpleadoAndFecha(empleado, hoy)
                .orElseGet(() -> {
                    Asistencia a = new Asistencia();
                    a.setEmpleado(empleado);
                    a.setFecha(hoy);
                    return a;
                });

        if (asistencia.getHoraEntradaReal() != null) {
            return new MarcarAsistenciaResponse(false, "Ya registraste tu entrada hoy", null, null, null);
        }

        asistencia.setHoraEntradaReal(ahora);

        horarioEmpleadoRepository
                .findByEmpleadoAndFechaFinIsNull(empleado)
                .ifPresent(he -> {
                    LocalTime esperado = he.getHorario().getHoraEntrada();
                    int tolerancia = he.getHorario().getToleranciaMinutos();

                    if (ahora.isAfter(esperado.plusMinutes(tolerancia))) {
                        asistencia.setEstadoAsistencia("TARDANZA");
                    } else {
                        asistencia.setEstadoAsistencia("PRESENTE");
                    }
                });

        asistenciaRepository.save(asistencia);

        return new MarcarAsistenciaResponse(
                true,
                "Entrada registrada",
                hoy.toString(),
                ahora.toString(),
                "Entrada"
        );
    }

    public MarcarAsistenciaResponse marcarSalida(MarcarAsistenciaRequest request) {

        Usuario usuario = usuarioRepository.findById(request.getIdUsuario()).orElse(null);
        if (usuario == null) {
            return new MarcarAsistenciaResponse(false, "Usuario no existe", null, null, null);
        }

        Empleado empleado = usuario.getEmpleado();

        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        Asistencia asistencia = asistenciaRepository
                .findByEmpleadoAndFecha(empleado, hoy)
                .orElse(null);

        if (asistencia == null || asistencia.getHoraEntradaReal() == null) {
            return new MarcarAsistenciaResponse(false, "Primero debes marcar entrada", null, null, null);
        }

        if (asistencia.getHoraSalidaReal() != null) {
            return new MarcarAsistenciaResponse(false, "Ya registraste tu salida hoy", null, null, null);
        }

        asistencia.setHoraSalidaReal(ahora);
        asistenciaRepository.save(asistencia);

        return new MarcarAsistenciaResponse(
                true,
                "Salida registrada",
                hoy.toString(),
                ahora.toString(),
                "Salida"
        );
    }
    
    public List<Asistencia> listarPorEmpleado(Long idEmpleado) {

        Empleado empleado = empleadoRepository.findById(idEmpleado)
                .orElse(null);

        if (empleado == null) {
            return List.of(); // lista vacía si no existe
        }

        return asistenciaRepository.findAllByEmpleado(empleado);
    }

}
