package com.grupobarreto.asistencia.controller;

import com.grupobarreto.asistencia.dto.*;
import com.grupobarreto.asistencia.service.ReporteExcelService;
import com.grupobarreto.asistencia.service.ReporteService;
import java.io.ByteArrayOutputStream;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@RestController
@RequestMapping("/api/admin/reportes")
public class ReporteController {

    private final ReporteService service;
    private final ReporteExcelService excelService;

    public ReporteController(ReporteService service, ReporteExcelService excelService) {
        this.service = service;
        this.excelService = excelService;
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
    
    // ===========================
    // DESCARGAR EXCEL
    // ===========================

    @GetMapping("/excel")
    public ResponseEntity<byte[]> descargarExcel(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fin
    ) throws Exception {

        Workbook wb = new XSSFWorkbook();

        Month mes = fin.getMonth();
        String nombreMes = mes.getDisplayName(TextStyle.FULL, new Locale("es"))
                .toUpperCase();

        var datos = excelService.reporte(inicio, fin);
        var horasExtras = excelService.reporteHorasExtras(inicio, fin);

        var porEmpleado = datos.stream()
                .collect(Collectors.groupingBy(AsistenciaExcelDTO::getEmpleado));

        for (var entry : porEmpleado.entrySet()) {

            String empleado = entry.getKey();

            String nombreHoja = empleado.length() > 30
                    ? empleado.substring(0, 30)
                    : empleado;

            Sheet sheet = wb.createSheet(nombreHoja);

            int rowNum = 0;

            Row titulo = sheet.createRow(rowNum++);
            titulo.createCell(0).setCellValue("CONTROL DE ASISTENCIA");

            rowNum++;

            Row info = sheet.createRow(rowNum++);
            info.createCell(0).setCellValue("NOMBRE:");
            info.createCell(1).setCellValue(empleado);

            rowNum++;

            Row horario = sheet.createRow(rowNum++);
            horario.createCell(0).setCellValue("HORARIO DE TRABAJO:");

            var primer = entry.getValue().get(0);

            String entrada = primer.getHoraEntradaProgramada();
            String salida = primer.getHoraSalidaProgramada();

            String textoHorario = "SIN HORARIO";

            if (entrada != null && salida != null && !entrada.isBlank()) {

                java.time.LocalTime tEntrada = java.time.LocalTime.parse(entrada);
                java.time.LocalTime tSalida = java.time.LocalTime.parse(salida);

                long horas = java.time.Duration.between(tEntrada, tSalida).toHours();

                textoHorario =
                        "L - V: " + entrada + " - " + salida +
                        " = " + horas + " HORAS | " +
                        "SÁBADO: 09:00 - 13:00 = 4 HORAS";
            }

            horario.createCell(1).setCellValue(textoHorario);

            rowNum++;

            Row mesRow = sheet.createRow(rowNum++);
            mesRow.createCell(0).setCellValue(nombreMes);

            rowNum++;

            Row header = sheet.createRow(rowNum++);
            header.createCell(0).setCellValue("FECHA");
            header.createCell(1).setCellValue("HORA ENTRADA");
            header.createCell(2).setCellValue("HORA INGRESO");
            header.createCell(3).setCellValue("TARDANZA");
            header.createCell(4).setCellValue("X");
            header.createCell(5).setCellValue("HORA SALIDA");
            header.createCell(6).setCellValue("CANTIDAD HORAS LABORADAS");
            header.createCell(7).setCellValue("HORAS DIARIAS");
            header.createCell(8).setCellValue("EXTRA");
            header.createCell(9).setCellValue("PDTE");

            for (var dto : entry.getValue()) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(dto.getFecha());
                row.createCell(1).setCellValue(dto.getHoraEntradaReal());
                row.createCell(2).setCellValue(dto.getHoraIngreso());
                row.createCell(3).setCellValue(dto.getTardanza());
                row.createCell(4).setCellValue(dto.getMarcaTardanza());
                row.createCell(5).setCellValue(dto.getHoraSalida());
                row.createCell(6).setCellValue(dto.getHorasLaboradas());
                row.createCell(7).setCellValue(dto.getHorasDiarias());
                row.createCell(8).setCellValue(dto.getExtra());
                row.createCell(9).setCellValue(dto.getPendiente());
            }

            // ======================
            // RESUMEN DERECHO
            // ======================

            int colResumen = 12;

            long diasMes = java.time.temporal.ChronoUnit.DAYS.between(inicio, fin) + 1;

            long tardanzas = entry.getValue().stream()
                    .filter(d -> "TARDANZA".equalsIgnoreCase(d.getMarcaTardanza()))
                    .count();

            Row resumenTitulo = sheet.getRow(3);
            if (resumenTitulo == null) resumenTitulo = sheet.createRow(3);
            resumenTitulo.createCell(colResumen).setCellValue("PERIODO");

            Row r1 = sheet.getRow(4);
            if (r1 == null) r1 = sheet.createRow(4);
            r1.createCell(colResumen).setCellValue("DIAS DEL MES");
            r1.createCell(colResumen + 1).setCellValue(diasMes);

            Row r2 = sheet.getRow(5);
            if (r2 == null) r2 = sheet.createRow(5);
            r2.createCell(colResumen).setCellValue("TARDANZAS");
            r2.createCell(colResumen + 1).setCellValue(tardanzas);

            // ======================
            // HORAS EXTRAS > 30 MIN
            // ======================

            rowNum += 2;

            Row tituloHE = sheet.createRow(rowNum++);
            tituloHE.createCell(0).setCellValue("HORAS EXTRAS MAYORES A 30 MINUTOS");

            Row headerHE = sheet.createRow(rowNum++);
            headerHE.createCell(0).setCellValue("FECHA");
            headerHE.createCell(1).setCellValue("SOBRETIEMPO");
            headerHE.createCell(2).setCellValue("MOTIVO");

            var extrasEmpleado = horasExtras.stream()
                    .filter(h -> h.getEmpleado().equalsIgnoreCase(empleado))
                    .toList();

            for (var he : extrasEmpleado) {

                Row rowHE = sheet.createRow(rowNum++);
                rowHE.createCell(0).setCellValue(he.getFecha());
                rowHE.createCell(1).setCellValue(he.getSobretiempo());
                rowHE.createCell(2).setCellValue(he.getMotivo());
            }

            for (int i = 0; i <= 12; i++) {
                sheet.autoSizeColumn(i);
            }
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wb.write(out);
        wb.close();

        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=REPORT DE ASISTENCIAS.xlsx")
                .header("Content-Type",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(out.toByteArray());
    }
    
}