package com.grupobarreto.asistencia.service;

import com.grupobarreto.asistencia.model.Empleado;
import com.grupobarreto.asistencia.model.Horario;
import com.grupobarreto.asistencia.model.HorarioEmpleado;
import com.grupobarreto.asistencia.repository.EmpleadoRepository;
import com.grupobarreto.asistencia.repository.HorarioEmpleadoRepository;
import com.grupobarreto.asistencia.repository.HorarioRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HorarioEmpleadoService {

    @Autowired
    private HorarioEmpleadoRepository horarioEmpleadoRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private HorarioRepository horarioRepository;

    public List<HorarioEmpleado> listar() {
        return horarioEmpleadoRepository.findAllConRelaciones();
    }

    public HorarioEmpleado buscar(Long id) {
        return horarioEmpleadoRepository.findById(id).orElse(null);
    }

    public HorarioEmpleado asignar(Long idEmpleado, Long idHorario) {

        Empleado emp = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new RuntimeException("Empleado no existe"));

        Horario hor = horarioRepository.findById(idHorario)
                .orElseThrow(() -> new RuntimeException("Horario no existe"));

        if (!Boolean.TRUE.equals(hor.getActivo())) {
            throw new RuntimeException("No se puede asignar un horario inactivo");
        }

        boolean yaAsignado = horarioEmpleadoRepository
                .existsByEmpleadoAndHorarioAndFechaFinIsNull(emp, hor);

        if (yaAsignado) {
            throw new RuntimeException("El empleado ya tiene este horario asignado");
        }

        horarioEmpleadoRepository
                .findByEmpleadoAndFechaFinIsNull(emp)
                .ifPresent(actual -> {
                    actual.setFechaFin(LocalDate.now());
                    horarioEmpleadoRepository.save(actual);
                });

        HorarioEmpleado nuevo = new HorarioEmpleado();
        nuevo.setEmpleado(emp);
        nuevo.setHorario(hor);
        nuevo.setFechaInicio(LocalDate.now());

        return horarioEmpleadoRepository.save(nuevo);
    }

    public HorarioEmpleado cerrar(Long id) {
        HorarioEmpleado he = horarioEmpleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignación no existe"));

        if (he.getFechaFin() != null) {
            throw new RuntimeException("La asignación ya está cerrada");
        }

        he.setFechaFin(LocalDate.now());
        return horarioEmpleadoRepository.save(he);
    }


    public boolean eliminar(Long id) {
        if (!horarioEmpleadoRepository.existsById(id)) return false;
        horarioEmpleadoRepository.deleteById(id);
        return true;
    }
}
