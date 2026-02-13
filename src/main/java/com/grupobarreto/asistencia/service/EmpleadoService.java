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
    
    public List<Empleado> buscarPorNombre(String nombre) {

        if (nombre == null || nombre.isBlank()) {
            throw new RuntimeException("Debe ingresar un nombre para buscar");
        }

        return empleadoRepository
                .findByActivoTrueAndNombresContainingIgnoreCase(nombre);
    }

    public Empleado crear(Empleado empleado) {

        if (empleado.getCodigoEmpleado() == null || empleado.getCodigoEmpleado().isBlank()
                || empleado.getNombres() == null || empleado.getNombres().isBlank()
                || empleado.getApellidos() == null || empleado.getApellidos().isBlank()) {

            throw new RuntimeException("Datos obligatorios incompletos");
        }

        if (empleadoRepository.existsByCodigoEmpleado(empleado.getCodigoEmpleado())) {
            throw new RuntimeException("Código de empleado ya existe");
        }

        empleado.setActivo(true);
        return empleadoRepository.save(empleado);
    }

    public Empleado actualizar(Long id, Empleado datos) {

        Empleado emp = empleadoRepository.findById(id).orElse(null);
        if (emp == null) return null;


        if (datos.getCodigoEmpleado() == null || datos.getCodigoEmpleado().isBlank()
                || datos.getNombres() == null || datos.getNombres().isBlank()
                || datos.getApellidos() == null || datos.getApellidos().isBlank()) {

            throw new RuntimeException("Datos obligatorios incompletos");
        }


        if (!emp.getCodigoEmpleado().equals(datos.getCodigoEmpleado())
                && empleadoRepository.existsByCodigoEmpleado(datos.getCodigoEmpleado())) {

            throw new RuntimeException("Código de empleado ya existe");
        }

        emp.setCodigoEmpleado(datos.getCodigoEmpleado());
        emp.setNombres(datos.getNombres());
        emp.setApellidos(datos.getApellidos());
        emp.setFechaIngreso(datos.getFechaIngreso());
        emp.setActivo(datos.isActivo());

        return empleadoRepository.save(emp);
    }

    public boolean eliminar(Long id) {

        Empleado emp = empleadoRepository.findById(id).orElse(null);
        if (emp == null) return false;

        emp.setActivo(false);
        empleadoRepository.save(emp);
        return true;
    }
}


