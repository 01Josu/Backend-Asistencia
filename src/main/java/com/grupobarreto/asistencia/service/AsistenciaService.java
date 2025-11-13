package com.grupobarreto.asistencia.service;

import com.grupobarreto.asistencia.model.RegistroAsistencia;
import com.grupobarreto.asistencia.model.RegistroResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class AsistenciaService {

    private static final String CARPETA = "C:/Asistencias/";

    private String getArchivoDelMes() {
        LocalDate fecha = LocalDate.now();
        return CARPETA + "asistencias_" + fecha.getYear() + "_" + String.format("%02d", fecha.getMonthValue()) + ".xlsx";
    }

    private void crearExcelSiNoExiste(String archivoPath) throws Exception {
        File file = new File(archivoPath);
        if (!file.exists()) {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Asistencias");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Nombre");
            header.createCell(2).setCellValue("Fecha");
            header.createCell(3).setCellValue("Hora");
            header.createCell(4).setCellValue("Tipo");

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
            workbook.close();
        }
    }

    private boolean yaRegistrado(String idEmpleado, String fecha, String tipo) throws Exception {
        String archivo = getArchivoDelMes();
        File file = new File(archivo);
        if (!file.exists()) return false;

        try (FileInputStream fis = new FileInputStream(archivo);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // encabezado
                String id = row.getCell(0).getStringCellValue();
                String f = row.getCell(2).getStringCellValue();
                String t = row.getCell(4).getStringCellValue();
                if (id.equals(idEmpleado) && f.equals(fecha) && t.equals(tipo)) {
                    return true;
                }
            }
        }
        return false;
    }

    private RegistroAsistencia agregarRegistro(RegistroAsistencia registro) throws Exception {
        String archivo = getArchivoDelMes();
        crearExcelSiNoExiste(archivo);

        try (FileInputStream fis = new FileInputStream(archivo);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int ultimaFila = sheet.getLastRowNum() + 1;

            Row row = sheet.createRow(ultimaFila);
            row.createCell(0).setCellValue(registro.getIdEmpleado());
            row.createCell(1).setCellValue(registro.getNombre());
            row.createCell(2).setCellValue(registro.getFecha());
            row.createCell(3).setCellValue(registro.getHora());
            row.createCell(4).setCellValue(registro.getTipo());

            try (FileOutputStream fos = new FileOutputStream(archivo)) {
                workbook.write(fos);
            }
        }

        return registro;
    }

    public RegistroResponse marcarEntrada(String idEmpleado, String nombre) {
        try {
            String fecha = LocalDate.now().toString();
            if (yaRegistrado(idEmpleado, fecha, "Entrada")) {
                return new RegistroResponse(false, "Ya registraste tu entrada hoy", null);
            }
            String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            RegistroAsistencia registro = new RegistroAsistencia(idEmpleado, nombre, fecha, hora, "Entrada");
            agregarRegistro(registro);
            return new RegistroResponse(true, "Entrada registrada correctamente", registro);
        } catch (Exception e) {
            return new RegistroResponse(false, "Error: " + e.getMessage(), null);
        }
    }

    public RegistroResponse marcarSalida(String idEmpleado, String nombre) {
        try {
            String fecha = LocalDate.now().toString();

            // Verificar si ya registró entrada
            if (!yaRegistrado(idEmpleado, fecha, "Entrada")) {
                return new RegistroResponse(false, "No puedes registrar la salida sin haber registrado la entrada", null);
            }

            // Verificar si ya registró salida
            if (yaRegistrado(idEmpleado, fecha, "Salida")) {
                return new RegistroResponse(false, "Ya registraste tu salida hoy", null);
            }

            String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            RegistroAsistencia registro = new RegistroAsistencia(idEmpleado, nombre, fecha, hora, "Salida");
            agregarRegistro(registro);

            return new RegistroResponse(true, "Salida registrada correctamente", registro);

        } catch (Exception e) {
            return new RegistroResponse(false, "Error: " + e.getMessage(), null);
        }
    }

}
