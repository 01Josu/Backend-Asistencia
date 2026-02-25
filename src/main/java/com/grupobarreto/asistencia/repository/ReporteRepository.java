package com.grupobarreto.asistencia.repository;

import com.grupobarreto.asistencia.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Asistencia, Long> {

    @Query(value = """
        SELECT 
            estado_asistencia AS estado,
            COUNT(*) AS total
        FROM asistencia
        WHERE fecha BETWEEN :inicio AND :fin
        GROUP BY estado_asistencia
    """, nativeQuery = true)
    List<Object[]> resumenPorEstado(
        @Param("inicio") LocalDate inicio,
        @Param("fin") LocalDate fin
    );

    @Query(value = """
        SELECT COUNT(*) 
        FROM asistencia
        WHERE fecha BETWEEN :inicio AND :fin
    """, nativeQuery = true)
    long totalAsistencias(
        @Param("inicio") LocalDate inicio,
        @Param("fin") LocalDate fin
    );

    @Query(value = """
        SELECT COUNT(*) 
        FROM asistencia
        WHERE estado_asistencia = 'TARDANZA'
          AND fecha BETWEEN :inicio AND :fin
    """, nativeQuery = true)
    long totalTardanzas(
        @Param("inicio") LocalDate inicio,
        @Param("fin") LocalDate fin
    );

    @Query(value = """
        SELECT COUNT(*) 
        FROM asistencia
        WHERE estado_asistencia = 'FALTA'
          AND fecha BETWEEN :inicio AND :fin
    """, nativeQuery = true)
    long totalFaltas(
        @Param("inicio") LocalDate inicio,
        @Param("fin") LocalDate fin
    );

    @Query(value = """
        SELECT COUNT(*) 
        FROM asistencia a
        JOIN justificacion j ON a.id_asistencia = j.id_asistencia
        WHERE a.fecha BETWEEN :inicio AND :fin
    """, nativeQuery = true)
    long totalJustificadas(
        @Param("inicio") LocalDate inicio,
        @Param("fin") LocalDate fin
    );

    @Query(value = """
        SELECT 
            TO_CHAR(DATE_TRUNC('month', fecha), 'Month') AS mes,
            COUNT(*) AS total
        FROM asistencia
        WHERE estado_asistencia = 'TARDANZA'
        GROUP BY DATE_TRUNC('month', fecha)
        ORDER BY mes
    """, nativeQuery = true)
    List<Object[]> tardanzasPorMes();

    @Query(value = """
        SELECT 
            e.id_empleado,
            e.nombres,
            e.apellidos,
            COUNT(*) AS total,
            SUM(CASE WHEN a.estado_asistencia = 'PRESENTE' THEN 1 ELSE 0 END) AS puntuales,
            SUM(CASE WHEN a.estado_asistencia = 'TARDANZA' THEN 1 ELSE 0 END) AS tardanzas,
            SUM(CASE WHEN a.estado_asistencia = 'FALTA' THEN 1 ELSE 0 END) AS faltas
        FROM asistencia a
        JOIN empleado e ON a.id_empleado = e.id_empleado
        WHERE a.fecha BETWEEN :inicio AND :fin
        GROUP BY e.id_empleado, e.nombres, e.apellidos
    """, nativeQuery = true)
    List<Object[]> asistenciaPorEmpleado(
        @Param("inicio") LocalDate inicio,
        @Param("fin") LocalDate fin
    );
    
    @Query(value = """
        SELECT 
            COUNT(*) AS total_asistencias,
            COALESCE(SUM(CASE WHEN a.estado_asistencia = 'TARDANZA' THEN 1 ELSE 0 END),0),
            COALESCE(SUM(CASE WHEN a.estado_asistencia = 'FALTA' THEN 1 ELSE 0 END),0),
            COALESCE(SUM(CASE WHEN j.id_asistencia IS NOT NULL THEN 1 ELSE 0 END),0)
        FROM asistencia a
        LEFT JOIN justificacion j 
            ON a.id_asistencia = j.id_asistencia
        WHERE a.fecha BETWEEN :inicio AND :fin
    """, nativeQuery = true)
    List<Object[]> resumenDashboard(
        @Param("inicio") LocalDate inicio,
        @Param("fin") LocalDate fin
    );
}