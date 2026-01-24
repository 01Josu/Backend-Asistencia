package com.grupobarreto.asistencia.scheduler;

import com.grupobarreto.asistencia.model.Asistencia;
import com.grupobarreto.asistencia.model.Empleado;
import com.grupobarreto.asistencia.repository.AsistenciaRepository;
import com.grupobarreto.asistencia.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class FaltaScheduler {

    private static final ZoneId ZONA_PERU = ZoneId.of("America/Lima");

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    // Ejecuta todos los días a las 23:59 (hora Perú)
    @Scheduled(cron = "0 59 23 * * *", zone = "America/Lima")
    public void marcarFaltasDelDia() {

        LocalDate hoy = LocalDate.now(ZONA_PERU);

        if (hoy.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return;
        }

        List<Empleado> empleados = empleadoRepository.findByActivoTrue();

        for (Empleado emp : empleados) {

            Asistencia asistencia = asistenciaRepository
                    .findByEmpleadoAndFecha(emp, hoy)
                    .orElse(null);

            if (asistencia == null) {
                Asistencia a = new Asistencia();
                a.setEmpleado(emp);
                a.setFecha(hoy);
                a.setEstadoAsistencia("FALTA");
                asistenciaRepository.save(a);
                continue;
            }

            if (asistencia.getHoraEntradaReal() != null && asistencia.getHoraSalidaReal() == null) {
                asistencia.setHoraSalidaReal(LocalTime.of(23, 59));
                asistencia.setEstadoAsistencia("SALIDA_AUTOMATICA");
                asistenciaRepository.save(asistencia);
            }
        }
    }
}
