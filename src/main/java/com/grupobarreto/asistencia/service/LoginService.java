package com.grupobarreto.asistencia.service;

import com.grupobarreto.asistencia.model.Empleado;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoginService {

    private final String EXCEL_PATH = "C:/Asistencias/Empleados.xlsx"; // ruta a tu archivo

    public Empleado login(String usuario, String contraseña) throws IOException {
        try (FileInputStream file = new FileInputStream(EXCEL_PATH);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // saltar encabezado

                String id = String.valueOf((int) row.getCell(0).getNumericCellValue());
                String apellido = row.getCell(1).getStringCellValue();
                String nombre = row.getCell(2).getStringCellValue();
                String user = row.getCell(3).getStringCellValue();
                String pass = row.getCell(4).getStringCellValue();

                if (user.equals(usuario) && pass.equals(contraseña)) { // sensible a mayúsculas/minúsculas
                    return new Empleado(id, apellido, nombre, user, pass);
                }
            }
        }

        return null; // no se encontró
    }

}
