package com.grupobarreto.asistencia.controller;

import com.grupobarreto.asistencia.model.Empleado;
import com.grupobarreto.asistencia.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@CrossOrigin(origins = "http://localhost:4200")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public List<Empleado> listar() {
        return empleadoService.listar();
    }

    @GetMapping("/{id}")
    public Empleado obtener(@PathVariable Long id) {
        return empleadoService.buscar(id);
    }

    @PostMapping
    public String crear() {
        return "La creación de empleados está deshabilitada. Use la base de datos.";
    }

    @PutMapping("/{id}")
    public String actualizar(@PathVariable Long id) {
        return "La edición de empleados está deshabilitada. Use la base de datos.";
    }

    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        return "La eliminación de empleados está deshabilitada. Use la base de datos.";
    }
}
