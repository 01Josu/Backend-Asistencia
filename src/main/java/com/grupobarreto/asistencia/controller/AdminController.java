package com.grupobarreto.asistencia.controller;

import com.grupobarreto.asistencia.dto.EmpleadoRequest;
import com.grupobarreto.asistencia.dto.HorarioEmpleadoRequest;
import com.grupobarreto.asistencia.dto.UsuarioRequest;
import com.grupobarreto.asistencia.model.*;
import com.grupobarreto.asistencia.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private HorarioService horarioService;

    @Autowired
    private HorarioEmpleadoService horarioEmpleadoService;

    @Autowired
    private JustificacionService justificacionService;

    // ================= EMPLEADOS =================

    @GetMapping("/empleados")
    public List<Empleado> listarEmpleados() {
        return empleadoService.listar();
    }

    @GetMapping("/empleados/{id}")
    public Empleado obtenerEmpleado(@PathVariable Long id) {
        return empleadoService.buscar(id);
    }

    @PostMapping("/empleados")
    public Empleado crearEmpleado(@RequestBody EmpleadoRequest request) {

        if (request.getCodigoEmpleado() == null || request.getCodigoEmpleado().isBlank()) {
            throw new RuntimeException("El código de empleado es obligatorio");
        }

        Empleado emp = new Empleado();
        emp.setCodigoEmpleado(request.getCodigoEmpleado());
        emp.setNombres(request.getNombres());
        emp.setApellidos(request.getApellidos());
        emp.setFechaIngreso(request.getFechaIngreso());
        emp.setActivo(true);

        return empleadoService.crear(emp);
    }

    @PutMapping("/empleados/{id}")
    public Empleado actualizarEmpleado(@PathVariable Long id,
                                       @RequestBody EmpleadoRequest request) {

        Empleado emp = new Empleado();
        emp.setCodigoEmpleado(request.getCodigoEmpleado());
        emp.setNombres(request.getNombres());
        emp.setApellidos(request.getApellidos());
        emp.setFechaIngreso(request.getFechaIngreso());
        emp.setActivo(request.getActivo() != null ? request.getActivo() : true);

        return empleadoService.actualizar(id, emp);
    }

    @DeleteMapping("/empleados/{id}")
    public String eliminarEmpleado(@PathVariable Long id) {
        return empleadoService.eliminar(id)
                ? "Empleado eliminado"
                : "Empleado no encontrado";
    }

    // ================= USUARIOS =================

    @GetMapping("/usuarios")
    public List<Usuario> listarUsuarios() {
        return usuarioService.listar();
    }

    @GetMapping("/usuarios/{id}")
    public Usuario obtenerUsuario(@PathVariable Long id) {
        return usuarioService.buscar(id);
    }

    @PostMapping("/usuarios")
    public Usuario crearUsuario(@RequestBody UsuarioRequest request) {
        return usuarioService.crearUsuario(
                request.getIdEmpleado(),
                request.getUsuario(),
                request.getPassword(),
                request.getRol()
        );
    }

    @PutMapping("/usuarios/{id}")
    public Usuario actualizarUsuario(@PathVariable Long id,
                                     @RequestBody UsuarioRequest request) {
        return usuarioService.actualizar(id, request);
    }

    @DeleteMapping("/usuarios/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        return usuarioService.eliminar(id)
                ? "Usuario eliminado"
                : "Usuario no encontrado";
    }

    // ================= HORARIOS =================

    @GetMapping("/horarios")
    public List<Horario> listarHorarios() {
        return horarioService.listar();
    }

    @PostMapping("/horarios")
    public Horario crearHorario(@RequestBody Horario horario) {
        return horarioService.crear(horario);
    }

    @PutMapping("/horarios/{id}")
    public Horario actualizarHorario(@PathVariable Long id,
                                     @RequestBody Horario horario) {
        return horarioService.actualizar(id, horario);
    }

    @DeleteMapping("/horarios/{id}")
    public String eliminarHorario(@PathVariable Long id) {
        return horarioService.eliminar(id)
                ? "Horario eliminado"
                : "Horario no encontrado";
    }

    // ================= HORARIO - EMPLEADO =================

    @GetMapping("/horario-empleado")
    public List<HorarioEmpleado> listarAsignaciones() {
        return horarioEmpleadoService.listar();
    }

    @PostMapping("/horario-empleado")
    public HorarioEmpleado asignarHorario(
            @RequestBody HorarioEmpleadoRequest request) {

        return horarioEmpleadoService.asignar(
                request.getIdEmpleado(),
                request.getIdHorario()
        );
    }

    @PutMapping("/horario-empleado/{id}/cerrar")
    public HorarioEmpleado cerrarAsignacion(@PathVariable Long id) {
        return horarioEmpleadoService.cerrar(id);
    }


    @DeleteMapping("/horario-empleado/{id}")
    public String eliminarAsignacion(@PathVariable Long id) {
        return horarioEmpleadoService.eliminar(id)
                ? "Asignación eliminada"
                : "No existe";
    }

    // ================= JUSTIFICACIONES =================

    @GetMapping("/justificaciones")
    public List<Justificacion> listarJustificaciones() {
        return justificacionService.listar();
    }

    @PutMapping("/justificaciones/{id}/aprobar")
    public String aprobar(@PathVariable Long id,
                          @RequestParam boolean aprobado) {
        return justificacionService.aprobarJustificacion(id, aprobado);
    }
}
