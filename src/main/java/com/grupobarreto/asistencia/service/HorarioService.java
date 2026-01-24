package com.grupobarreto.asistencia.service;

import com.grupobarreto.asistencia.model.Horario;
import com.grupobarreto.asistencia.repository.HorarioEmpleadoRepository;
import com.grupobarreto.asistencia.repository.HorarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;
    @Autowired
    private HorarioEmpleadoRepository horarioEmpleadoRepository;

    public List<Horario> listar() {
        return horarioRepository.findByActivoTrue();
    }

    public Horario buscar(Long id) {
        return horarioRepository.findById(id).orElse(null);
    }

    public Horario crear(Horario h) {
        h.setActivo(true);
        return horarioRepository.save(h);
    }


    public Horario actualizar(Long id, Horario datos) {
        Horario h = horarioRepository.findById(id).orElse(null);
        if (h == null) return null;

        h.setHoraEntrada(datos.getHoraEntrada());
        h.setHoraSalida(datos.getHoraSalida());
        h.setToleranciaMinutos(datos.getToleranciaMinutos());
        h.setDescripcion(datos.getDescripcion());

        if (datos.getActivo() != null) {
            h.setActivo(datos.getActivo());
        }

        return horarioRepository.save(h);
    }


    public boolean eliminar(Long id) {
        Horario h = horarioRepository.findById(id).orElse(null);
        if (h == null) return false;

        boolean usado = horarioEmpleadoRepository.existsByHorario(h);

        if (usado) {
            if (Boolean.TRUE.equals(h.getActivo())) {
                h.setActivo(false);
                horarioRepository.save(h);
            }
        } else {
            horarioRepository.delete(h);
        }

        return true;
    }

}
