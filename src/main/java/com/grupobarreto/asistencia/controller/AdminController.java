package com.grupobarreto.asistencia.controller;

import com.grupobarreto.asistencia.dto.EmpleadoRequest;
import com.grupobarreto.asistencia.dto.HorarioEmpleadoRequest;
import com.grupobarreto.asistencia.dto.UsuarioRequest;
import com.grupobarreto.asistencia.model.*;
import com.grupobarreto.asistencia.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;


import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;

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

    @GetMapping("/empleados/buscar-por-nombre")
    public ResponseEntity<?> buscarPorNombre(@RequestParam String nombre) {

        List<Empleado> lista = empleadoService.buscarPorNombre(nombre);

        if (lista.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No se encontraron empleados con ese nombre"));
        }

        return ResponseEntity.ok(lista);
    }
    
    @GetMapping("/empleados/{id}")
    public ResponseEntity<?> obtenerEmpleado(@PathVariable Long id) {

        Empleado emp = empleadoService.buscar(id);

        if (emp == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "message", "Empleado no encontrado"
                    ));
        }

        return ResponseEntity.ok(emp);
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
    public ResponseEntity<?> eliminarEmpleado(@PathVariable Long id) {
        boolean eliminado = empleadoService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.ok(Map.of("message", "Empleado desactivado correctamente"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Map.of("message", "Empleado no encontrado"));
        }
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

    @GetMapping("/usuarios/buscar-por-nombre")
    public ResponseEntity<?> buscarPorNombreU(@RequestParam String nombre) {

        List<Usuario> lista = usuarioService.buscarPorNombre(nombre);

        if (lista.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No se encontraron usuarios con ese nombre"));
        }

        return ResponseEntity.ok(lista);
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
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {

        boolean eliminado = usuarioService.eliminar(id);

        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Map.of("message", "Usuario no encontrado"));
        }

        return ResponseEntity.ok(Map.of("message", "Usuario eliminado correctamente"));
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
    public ResponseEntity<String> eliminarHorario(@PathVariable Long id) {
        String mensaje = horarioService.eliminar(id);
        if (mensaje.equals("Horario no encontrado")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
        }
        return ResponseEntity.ok(mensaje);
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
    public ResponseEntity<?> eliminarAsignacion(@PathVariable Long id) {
        boolean eliminado = horarioEmpleadoService.eliminar(id);

        if (eliminado) {
            return ResponseEntity.ok().body(Map.of("message", "Asignación eliminada"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No existe"));
        }
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
