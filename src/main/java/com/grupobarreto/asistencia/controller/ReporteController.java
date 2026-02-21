package com.grupobarreto.asistencia.controller;

import com.grupobarreto.asistencia.dto.*;
import com.grupobarreto.asistencia.service.ReporteService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/reportes")
public class ReporteController {

    private final ReporteService service;

    public ReporteController(ReporteService service) {
        this.service = service;
    }

    // ===========================
    // DASHBOARD
    // ===========================

    @GetMapping("/dashboard")
    public DashboardResumenDTO dashboard(
        @RequestParam LocalDate inicio,
        @RequestParam LocalDate fin
    ) {
        return service.resumen(inicio, fin);
    }

    @GetMapping("/dashboard/estado")
    public List<AsistenciaPorEstadoDTO> resumenPorEstado(
        @RequestParam LocalDate inicio,
        @RequestParam LocalDate fin
    ) {
        return service.resumenPorEstado(inicio, fin);
    }

    // ===========================
    // TARDANZAS POR MES
    // ===========================

    @GetMapping("/tardanzas/mes")
    public List<TardanzaPorMesDTO> tardanzasPorMes() {
        return service.tardanzasPorMes();
    }

    // ===========================
    // POR EMPLEADO
    // ===========================

    @GetMapping("/empleados")
    public List<AsistenciaEmpleadoDTO> asistenciaPorEmpleado(
        @RequestParam LocalDate inicio,
        @RequestParam LocalDate fin
    ) {
        return service.asistenciaPorEmpleado(inicio, fin);
    }
    
}