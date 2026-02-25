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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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

        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        wb.setCompressTempFiles(true);
        
        // Estilo con borde (tabla y bloques)
        CellStyle styleBorde = wb.createCellStyle();
        styleBorde.setAlignment(HorizontalAlignment.CENTER);
        styleBorde.setVerticalAlignment(VerticalAlignment.CENTER);
        styleBorde.setBorderTop(BorderStyle.THICK);
        styleBorde.setBorderBottom(BorderStyle.THICK);
        styleBorde.setBorderLeft(BorderStyle.THICK);
        styleBorde.setBorderRight(BorderStyle.THICK);

        Font fontBold = wb.createFont();
        fontBold.setBold(true);
        styleBorde.setFont(fontBold);

        // Estilo título grande (18)
        CellStyle styleTitulo = wb.createCellStyle();
        styleTitulo.setAlignment(HorizontalAlignment.CENTER);
        styleTitulo.setVerticalAlignment(VerticalAlignment.CENTER);
        styleTitulo.setBorderTop(BorderStyle.THICK);
        styleTitulo.setBorderBottom(BorderStyle.THICK);
        styleTitulo.setBorderLeft(BorderStyle.THICK);
        styleTitulo.setBorderRight(BorderStyle.THICK);

        Font fontTitulo = wb.createFont();
        fontTitulo.setBold(true);
        fontTitulo.setFontHeightInPoints((short) 18);
        styleTitulo.setFont(fontTitulo);

        // Estilo header (tabla principal)
        CellStyle styleHeader = wb.createCellStyle();
        Font fontHeader = wb.createFont();
        fontHeader.setBold(true);
        styleHeader.setFont(fontHeader);
        styleHeader.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeader.setAlignment(HorizontalAlignment.CENTER);
        styleHeader.setBorderBottom(BorderStyle.THIN);
        styleHeader.setBorderTop(BorderStyle.THIN);
        styleHeader.setBorderLeft(BorderStyle.THIN);
        styleHeader.setBorderRight(BorderStyle.THIN);

        // Estilo celda normal
        CellStyle styleCell = wb.createCellStyle();
        styleCell.setAlignment(HorizontalAlignment.CENTER);
        styleCell.setVerticalAlignment(VerticalAlignment.CENTER);
        styleCell.setBorderBottom(BorderStyle.THIN);
        styleCell.setBorderTop(BorderStyle.THIN);
        styleCell.setBorderLeft(BorderStyle.THIN);
        styleCell.setBorderRight(BorderStyle.THIN);

        // Estilo hora real (para PDTE)
        CellStyle styleHora = wb.createCellStyle();
        styleHora.cloneStyleFrom(styleCell);

        DataFormat formatHora = wb.createDataFormat();
        styleHora.setDataFormat(formatHora.getFormat("[h]:mm:ss"));
        
        Month mes = fin.getMonth();
        String nombreMes = mes.getDisplayName(TextStyle.FULL, new Locale("es")).toUpperCase();

        var datos = excelService.reporte(inicio, fin);
        var horasExtras = excelService.reporteHorasExtras(inicio, fin);

        var porEmpleado = datos.stream()
                .collect(Collectors.groupingBy(AsistenciaExcelDTO::getEmpleado));

        for (var entry : porEmpleado.entrySet()) {

            String empleado = entry.getKey();
            String nombreHoja = empleado.length() > 30 ? empleado.substring(0, 30) : empleado;
            Sheet sheet = wb.createSheet(nombreHoja);

            int rowNum = 0;

            // =============================
            // TITULO (2 filas combinadas B-G)
            // =============================

            Row titulo1 = sheet.createRow(rowNum++);
            Cell cellTitulo = titulo1.createCell(1);
            cellTitulo.setCellValue("CONTROL DE ASISTENCIA");
            cellTitulo.setCellStyle(styleTitulo);

            for (int i = 2; i <= 6; i++) {
                Cell c = titulo1.createCell(i);
                c.setCellStyle(styleBorde);
            }

            Row titulo2 = sheet.createRow(rowNum++);
            for (int i = 1; i <= 6; i++) {
                Cell c = titulo2.createCell(i);
                c.setCellStyle(styleBorde);
            }

            sheet.addMergedRegion(new CellRangeAddress(
                    titulo1.getRowNum(),
                    titulo2.getRowNum(),
                    1,
                    6
            ));

            // =============================
            // NOMBRE (B-G, centrado)
            // =============================
            Row info = sheet.createRow(rowNum++);

            Cell cNombre = info.createCell(1);
            cNombre.setCellValue("NOMBRE: " + empleado);
            cNombre.setCellStyle(styleBorde);

            for (int i = 2; i <= 6; i++) {
                Cell c = info.createCell(i);
                c.setCellStyle(styleBorde);
            }

            sheet.addMergedRegion(new CellRangeAddress(
                    info.getRowNum(),
                    info.getRowNum(),
                    1,
                    6
            ));

            // =============================
            // HORARIO (B-G, centrado)
            // =============================
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

            Row horario = sheet.createRow(rowNum++);

            Cell cHorario = horario.createCell(1);
            cHorario.setCellValue(textoHorario);
            cHorario.setCellStyle(styleBorde);

            for (int i = 2; i <= 6; i++) {
                Cell c = horario.createCell(i);
                c.setCellStyle(styleBorde);
            }

            sheet.addMergedRegion(new CellRangeAddress(
                    horario.getRowNum(),
                    horario.getRowNum(),
                    1,
                    6
            ));

            rowNum++;

            // =============================
            // MES
            // =============================
            Row mesRow = sheet.createRow(rowNum++);
            Cell cMes = mesRow.createCell(0);
            cMes.setCellValue(nombreMes);
            cMes.setCellStyle(styleBorde);

            rowNum++;

            // =============================
            // TABLA PRINCIPAL (con estilo)
            // =============================
            Row header = sheet.createRow(rowNum++);
            String[] heads = {
                    "FECHA","HORA ENTRADA","HORA INGRESO","TARDANZA","X",
                    "HORA SALIDA","CANTIDAD HORAS LABORADAS","HORAS DIARIAS",
                    "EXTRA","PDTE"
            };

            for (int i = 0; i < heads.length; i++) {
                Cell h = header.createCell(i);
                h.setCellValue(heads[i]);
                h.setCellStyle(styleHeader);
            }
            
            int filaInicioDatos = rowNum;

            for (var dto : entry.getValue()) {

                Row row = sheet.createRow(rowNum++);

                for (int i = 0; i <= 9; i++) {
                    Cell c = row.createCell(i);
                    c.setCellStyle(styleCell);
                }

                row.getCell(0).setCellValue(dto.getFecha());
                row.getCell(1).setCellValue(dto.getHoraEntradaReal());
                row.getCell(2).setCellValue(dto.getHoraIngreso());
                row.getCell(3).setCellValue(dto.getTardanza());
                row.getCell(4).setCellValue(dto.getMarcaTardanza());
                row.getCell(5).setCellValue(dto.getHoraSalida());
                row.getCell(6).setCellValue(dto.getHorasLaboradas());
                row.getCell(7).setCellValue(dto.getHorasDiarias());
                row.getCell(8).setCellValue(dto.getExtra());
                // ===== PDTE como hora real =====
                String pendiente = dto.getPendiente();
                Cell cellPdte = row.getCell(9);

                if (pendiente != null && !pendiente.isBlank()) {

                    java.time.LocalTime time = java.time.LocalTime.parse(pendiente);

                    double excelTime = time.toSecondOfDay() / 86400.0;

                    cellPdte.setCellValue(excelTime);
                    cellPdte.setCellStyle(styleHora);

                } else {
                    cellPdte.setCellValue(0);
                    cellPdte.setCellStyle(styleHora);
                }
            }
            
            int filaFinDatos = rowNum - 1;

            // ===== FILA TOTAL PDTE =====
            Row filaTotal = sheet.createRow(rowNum++);

            // Texto TOTAL
            Cell cTextoTotal = filaTotal.createCell(8); // columna I
            cTextoTotal.setCellValue("TOTAL:");
            cTextoTotal.setCellStyle(styleHeader);

            // Celda suma columna J (PDTE)
            Cell cSuma = filaTotal.createCell(9);

            // Fórmula SUM (Excel usa filas base 1)
            String formula = String.format(
                    "SUM(J%d:J%d)",
                    filaInicioDatos + 1,
                    filaFinDatos + 1
            );

            cSuma.setCellFormula(formula);

            // Formato especial para horas acumuladas
            CellStyle styleHoraTotal = wb.createCellStyle();
            styleHoraTotal.cloneStyleFrom(styleHeader);

            DataFormat format = wb.createDataFormat();
            styleHoraTotal.setDataFormat(format.getFormat("[h]:mm:ss"));

            cSuma.setCellStyle(styleHoraTotal);

            // =============================
            // RESUMEN DERECHO (centrado)
            // =============================
            int colResumen = 12;

            long diasMes = java.time.temporal.ChronoUnit.DAYS.between(inicio, fin) + 1;
            long tardanzas = entry.getValue().stream()
                    .filter(d -> "TARDANZA".equalsIgnoreCase(d.getMarcaTardanza()))
                    .count();

            Row resumenTitulo = sheet.getRow(3);
            if (resumenTitulo == null) resumenTitulo = sheet.createRow(3);
            Cell rTitulo = resumenTitulo.createCell(colResumen);
            rTitulo.setCellValue("PERIODO");
            rTitulo.setCellStyle(styleBorde);

            Row r1 = sheet.getRow(4);
            if (r1 == null) r1 = sheet.createRow(4);

            Cell cPeriodo1 = r1.createCell(colResumen);
            cPeriodo1.setCellValue("DIAS DEL MES");
            cPeriodo1.setCellStyle(styleBorde);

            Cell cPeriodo1Val = r1.createCell(colResumen + 1);
            cPeriodo1Val.setCellValue(diasMes);
            cPeriodo1Val.setCellStyle(styleBorde);


            Row r2 = sheet.getRow(5);
            if (r2 == null) r2 = sheet.createRow(5);

            Cell cPeriodo2 = r2.createCell(colResumen);
            cPeriodo2.setCellValue("TARDANZAS");
            cPeriodo2.setCellStyle(styleBorde);

            Cell cPeriodo2Val = r2.createCell(colResumen + 1);
            cPeriodo2Val.setCellValue(tardanzas);
            cPeriodo2Val.setCellStyle(styleBorde);

            // =============================
            // HORAS EXTRAS (tabla con borde)
            // =============================
            rowNum += 2;

            Row tituloHE = sheet.createRow(rowNum++);
            Cell th = tituloHE.createCell(0);
            th.setCellValue("HORAS EXTRAS MAYORES A 30 MINUTOS");
            th.setCellStyle(styleBorde);

            Row headerHE = sheet.createRow(rowNum++);
            String[] headsHE = {"FECHA","SOBRETIEMPO","MOTIVO"};

            for (int i = 0; i < headsHE.length; i++) {
                Cell h = headerHE.createCell(i);
                h.setCellValue(headsHE[i]);
                h.setCellStyle(styleHeader);
            }
            int filaInicioExtras = rowNum;

            var extrasEmpleado = horasExtras.stream()
                    .filter(h -> h.getEmpleado().equalsIgnoreCase(empleado))
                    .toList();

            for (var he : extrasEmpleado) {
                Row rowHE = sheet.createRow(rowNum++);

                for (int i = 0; i <= 2; i++) {
                    Cell c = rowHE.createCell(i);
                    c.setCellStyle(styleCell);
                }

                rowHE.getCell(0).setCellValue(he.getFecha());
                // ===== SOBRETIEMPO como hora real =====
                String sobretiempo = he.getSobretiempo();
                Cell cellSobre = rowHE.getCell(1);

                if (sobretiempo != null && !sobretiempo.isBlank()) {

                    java.time.LocalTime time = java.time.LocalTime.parse(sobretiempo);

                    double excelTime = time.toSecondOfDay() / 86400.0;

                    cellSobre.setCellValue(excelTime);
                    cellSobre.setCellStyle(styleHora);

                } else {
                    cellSobre.setCellValue(0);
                    cellSobre.setCellStyle(styleHora);
                }
                rowHE.getCell(2).setCellValue(he.getMotivo());
            }
            int filaFinExtras = rowNum - 1;

            // ===== FILA TOTAL HORAS EXTRAS =====
            Row filaTotalHE = sheet.createRow(rowNum++);

            Cell textoTotalHE = filaTotalHE.createCell(0);
            textoTotalHE.setCellValue("TOTAL HORAS EXTRAS:");
            textoTotalHE.setCellStyle(styleHeader);

            Cell sumaHE = filaTotalHE.createCell(1);

            String formulaHE = String.format(
                    "SUM(B%d:B%d)",
                    filaInicioExtras + 1,
                    filaFinExtras + 1
            );

            sumaHE.setCellFormula(formulaHE);

            // formato hora acumulada
            CellStyle styleHoraTotalHE = wb.createCellStyle();
            styleHoraTotalHE.cloneStyleFrom(styleHeader);

            DataFormat formatHE = wb.createDataFormat();
            styleHoraTotalHE.setDataFormat(formatHE.getFormat("[h]:mm:ss"));

            sumaHE.setCellStyle(styleHoraTotalHE);
            
            int[] anchos = {
                4000, // FECHA
                4500, // HORA ENTRADA
                4500, // HORA INGRESO
                3500, // TARDANZA
                2500, // X
                4500, // HORA SALIDA
                6000, // CANTIDAD HORAS LABORADAS
                4000, // HORAS DIARIAS
                3000, // EXTRA
                3000, // PDTE
                2000,
                2000,
                5000  // RESUMEN
            };

            for (int i = 0; i < anchos.length; i++) {
                sheet.setColumnWidth(i, anchos[i]);
            }
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wb.write(out);
        wb.close();
        wb.dispose();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=REPORT DE ASISTENCIAS.xlsx")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(out.toByteArray());
    }
    
}