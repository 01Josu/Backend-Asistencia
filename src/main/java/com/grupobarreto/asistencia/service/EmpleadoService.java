package com.grupobarreto.asistencia.service;

import com.grupobarreto.asistencia.model.Empleado;
import com.grupobarreto.asistencia.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    public List<Empleado> listar() {
        return empleadoRepository.findAll();
    }

    public Empleado buscar(Long id) {
        return empleadoRepository.findById(id).orElse(null);
    }
}
