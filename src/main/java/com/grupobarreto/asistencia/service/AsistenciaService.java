package com.grupobarreto.asistencia.service;

import com.grupobarreto.asistencia.dto.MarcarAsistenciaRequest;
import com.grupobarreto.asistencia.dto.MarcarAsistenciaResponse;
import com.grupobarreto.asistencia.model.*;
import com.grupobarreto.asistencia.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class AsistenciaService {

    private static final ZoneId ZONA_PERU = ZoneId.of("America/Lima");

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private HorarioEmpleadoRepository horarioEmpleadoRepository;

    @Autowired
    private GeoLocationService geoLocationService;

    public MarcarAsistenciaResponse marcarEntrada(MarcarAsistenciaRequest request) {

        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElse(null);

        if (usuario == null) {
            return new MarcarAsistenciaResponse(false, "Usuario no existe", null, null, null);
        }

        Empleado empleado = usuario.getEmpleado();
        if (empleado == null) {
            return new MarcarAsistenciaResponse(
                    false,
                    "Este usuario no está asociado a un empleado",
                    null,
                    null,
                    null
            );
        }

        Sede sedeValida = geoLocationService.validarUbicacion(
                request.getLatitud(),
                request.getLongitud()
        );

        if (sedeValida == null) {
            return new MarcarAsistenciaResponse(
                    false,
                    "No estás dentro de ninguna sede registrada",
                    null,
                    null,
                    null
            );
        }

        LocalDate hoy = LocalDate.now(ZONA_PERU);
        LocalTime ahora = LocalTime.now(ZONA_PERU);

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

        var horarioEmpleadoOpt = horarioEmpleadoRepository
                .findByEmpleadoAndFechaFinIsNull(empleado);

        if (horarioEmpleadoOpt.isEmpty()) {
            return new MarcarAsistenciaResponse(
                    false,
                    "Usted no tiene horario asignado",
                    null,
                    null,
                    null
            );
        }

        HorarioEmpleado he = horarioEmpleadoOpt.get();

        asistencia.setHoraEntradaReal(ahora);

        LocalTime esperado = he.getHorario().getHoraEntrada();
        int tolerancia = he.getHorario().getToleranciaMinutos();

        if (ahora.isAfter(esperado.plusMinutes(tolerancia))) {
            asistencia.setEstadoAsistencia("TARDANZA");
        } else {
            asistencia.setEstadoAsistencia("PRESENTE");
        }

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
        if (empleado == null) {
            return new MarcarAsistenciaResponse(
                    false,
                    "Este usuario no está asociado a un empleado",
                    null,
                    null,
                    null
            );
        }

        Sede sedeValida = geoLocationService.validarUbicacion(
                request.getLatitud(),
                request.getLongitud()
        );

        if (sedeValida == null) {
            return new MarcarAsistenciaResponse(
                    false,
                    "No estás dentro de ninguna sede registrada",
                    null,
                    null,
                    null
            );
        }

        LocalDate hoy = LocalDate.now(ZONA_PERU);
        LocalTime ahora = LocalTime.now(ZONA_PERU);

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

    public void marcarFaltasDelDia() {

        LocalDate hoy = LocalDate.now(ZONA_PERU);

        List<Empleado> empleadosActivos = empleadoRepository.findByActivoTrue();

        for (Empleado empleado : empleadosActivos) {

            boolean yaTieneAsistencia = asistenciaRepository
                    .findByEmpleadoAndFecha(empleado, hoy)
                    .isPresent();

            if (!yaTieneAsistencia) {
                Asistencia falta = new Asistencia();
                falta.setEmpleado(empleado);
                falta.setFecha(hoy);
                falta.setEstadoAsistencia("FALTA");

                asistenciaRepository.save(falta);
            }
        }
    }

}
