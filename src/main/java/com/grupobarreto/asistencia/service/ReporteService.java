package com.grupobarreto.asistencia.service;

import com.grupobarreto.asistencia.dto.*;
import com.grupobarreto.asistencia.repository.ReporteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    private final ReporteRepository repo;

    public ReporteService(ReporteRepository repo) {
        this.repo = repo;
    }

    // ===========================
    // DASHBOARD RESUMEN
    // ===========================

    public DashboardResumenDTO resumen(LocalDate inicio, LocalDate fin) {

        List<Object[]> result = repo.resumenDashboard(inicio, fin);

        if (result.isEmpty()) {
            return new DashboardResumenDTO();
        }

        Object[] r = result.get(0);

        DashboardResumenDTO dto = new DashboardResumenDTO();
        dto.setTotalAsistencias(((Number) r[0]).longValue());
        dto.setTotalTardanzas(((Number) r[1]).longValue());
        dto.setTotalFaltas(((Number) r[2]).longValue());
        dto.setTotalJustificadas(((Number) r[3]).longValue());

        return dto;
    }

    // ===========================
    // RESUMEN POR ESTADO (GRÁFICO)
    // ===========================

    public List<AsistenciaPorEstadoDTO> resumenPorEstado(LocalDate inicio, LocalDate fin) {
        return repo.resumenPorEstado(inicio, fin).stream()
            .map(row -> new AsistenciaPorEstadoDTO(
                (String) row[0],
                ((Number) row[1]).longValue()
            ))
            .collect(Collectors.toList());
    }

    // ===========================
    // TARDANZAS POR MES
    // ===========================

    public List<TardanzaPorMesDTO> tardanzasPorMes() {
        return repo.tardanzasPorMes().stream()
            .map(row -> new TardanzaPorMesDTO(
                ((String) row[0]).trim(),          // mes como texto
                ((Number) row[1]).longValue()       // total
            ))
            .collect(Collectors.toList());
    }

    // ===========================
    // ASISTENCIA POR EMPLEADO
    // ===========================

    public List<AsistenciaEmpleadoDTO> asistenciaPorEmpleado(LocalDate inicio, LocalDate fin) {
        return repo.asistenciaPorEmpleado(inicio, fin).stream()
            .map(row -> new AsistenciaEmpleadoDTO(
                ((Number) row[0]).longValue(),
                (String) row[1],
                (String) row[2],
                ((Number) row[3]).longValue(),
                ((Number) row[4]).longValue(),
                ((Number) row[5]).longValue(),
                ((Number) row[6]).longValue()
            ))
            .collect(Collectors.toList());
    }
}