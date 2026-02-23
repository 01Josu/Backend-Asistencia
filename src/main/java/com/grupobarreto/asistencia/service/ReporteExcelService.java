package com.grupobarreto.asistencia.service;

import com.grupobarreto.asistencia.dto.AsistenciaExcelDTO;
import com.grupobarreto.asistencia.dto.HorasExtrasDTO;
import com.grupobarreto.asistencia.repository.AsistenciaReporteRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteExcelService {

    private final AsistenciaReporteRepository repo;

    public ReporteExcelService(AsistenciaReporteRepository repo) {
        this.repo = repo;
    }

    public List<AsistenciaExcelDTO> reporte(LocalDate inicio, LocalDate fin) {

        return repo.reporteExcel(inicio, fin)
                .stream()
                .map(row -> {

                    AsistenciaExcelDTO dto = new AsistenciaExcelDTO();

                    dto.setFecha(toStr(row[0]));
                    dto.setHoraEntradaReal(toStr(row[1]));
                    dto.setHoraIngreso(toStr(row[2]));
                    dto.setTardanza(formatInterval(row[3]));
                    
                    dto.setHoraSalida(toStr(row[4]));
                    dto.setHorasLaboradas(formatInterval(row[5]));
                    dto.setHorasDiarias(formatInterval(row[6]));
                    dto.setExtra(formatInterval(row[7]));
                    dto.setPendiente(formatInterval(row[8]));
                    dto.setHoraEntradaProgramada(toStr(row[9]));
                    dto.setHoraSalidaProgramada(toStr(row[10]));
                    
                    dto.setMarcaTardanza(toStr(row[11]));
                    
                    dto.setEmpleado(toStr(row[12]));
                    dto.setDescripcionHorario(toStr(row[13]));

                    return dto;
                })
                .collect(Collectors.toList());
    }

    private String toStr(Object obj) {
        return obj == null ? "" : obj.toString();
    }
    
    private String formatInterval(Object obj) {

        if (obj == null) return "00:00:00";

        String raw = obj.toString();

        // Extraer horas, minutos y segundos del texto
        int hours = extract(raw, "hours");
        int mins = extract(raw, "mins");
        int secs = extract(raw, "secs");

        return String.format("%02d:%02d:%02d", hours, mins, secs);
    }

    private int extract(String raw, String unit) {
        try {
            int index = raw.indexOf(unit);
            if (index == -1) return 0;

            String before = raw.substring(0, index).trim();
            String[] parts = before.split(" ");
            return (int) Double.parseDouble(parts[parts.length - 1]);
        } catch (Exception e) {
            return 0;
        }
    }
    
    public List<HorasExtrasDTO> reporteHorasExtras(LocalDate inicio, LocalDate fin) {

        return repo.reporteHorasExtras(inicio, fin)
                .stream()
                .map(row -> {

                    HorasExtrasDTO dto = new HorasExtrasDTO();

                    dto.setIdEmpleado(
                            row[0] == null ? null : ((Number) row[0]).longValue()
                    );

                    dto.setEmpleado(
                            row[1] == null ? "" : row[1].toString()
                    );

                    dto.setFecha(toStr(row[2]));
                    dto.setSobretiempo(formatInterval(row[3]));
                    dto.setMotivo(
                            row[4] == null ? "" : row[4].toString()
                    );

                    return dto;
                })
                .collect(Collectors.toList());
    }
}