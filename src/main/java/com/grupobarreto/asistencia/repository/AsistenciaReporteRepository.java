package com.grupobarreto.asistencia.repository;

import com.grupobarreto.asistencia.model.Asistencia;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AsistenciaReporteRepository extends JpaRepository<Asistencia, Long> {

    @Query(value = """
    SELECT 
        a.fecha,
        a.hora_entrada_real,

        -- Hora ingreso esperada
        CASE 
            WHEN EXTRACT(DOW FROM a.fecha) = 6 
                THEN TIME '09:00'
            ELSE h.hora_entrada
        END AS hora_ingreso,

        -- Tardanza
        CASE 
            WHEN a.hora_entrada_real IS NOT NULL AND
                 a.hora_entrada_real >
                 (CASE 
                     WHEN EXTRACT(DOW FROM a.fecha) = 6 
                         THEN TIME '09:00'
                     ELSE h.hora_entrada
                  END)
            THEN a.hora_entrada_real -
                 (CASE 
                     WHEN EXTRACT(DOW FROM a.fecha) = 6 
                         THEN TIME '09:00'
                     ELSE h.hora_entrada
                  END)
            ELSE INTERVAL '0'
        END AS tardanza,

        a.hora_salida_real,

        -- Horas laboradas reales
        CASE 
            WHEN a.hora_entrada_real IS NOT NULL 
                 AND a.hora_salida_real IS NOT NULL
            THEN a.hora_salida_real - a.hora_entrada_real
            ELSE INTERVAL '0'
        END AS horas_laboradas,

        -- Horas esperadas del día
        CASE 
            WHEN EXTRACT(DOW FROM a.fecha) = 6 
                THEN INTERVAL '4 hours'
            ELSE h.hora_salida - h.hora_entrada
        END AS horas_diarias,

        -- Sobretiempo
        CASE 
            WHEN a.hora_entrada_real IS NOT NULL 
                 AND a.hora_salida_real IS NOT NULL
                 AND (a.hora_salida_real - a.hora_entrada_real) >
                     (CASE 
                         WHEN EXTRACT(DOW FROM a.fecha) = 6 
                             THEN INTERVAL '4 hours'
                         ELSE h.hora_salida - h.hora_entrada
                      END)
            THEN (a.hora_salida_real - a.hora_entrada_real) -
                 (CASE 
                     WHEN EXTRACT(DOW FROM a.fecha) = 6 
                         THEN INTERVAL '4 hours'
                     ELSE h.hora_salida - h.hora_entrada
                  END)
            ELSE INTERVAL '0'
        END AS sobretiempo,

        -- Pendiente
        CASE 
        
            WHEN a.estado_asistencia = 'FALTA'
            THEN 
                CASE 
                    WHEN EXTRACT(DOW FROM a.fecha) = 6 
                        THEN INTERVAL '4 hours'
                    ELSE h.hora_salida - h.hora_entrada
                END
        
            WHEN a.hora_entrada_real IS NOT NULL 
                 AND a.hora_salida_real IS NOT NULL
                 AND (a.hora_salida_real - a.hora_entrada_real) <
                     (CASE 
                         WHEN EXTRACT(DOW FROM a.fecha) = 6 
                             THEN INTERVAL '4 hours'
                         ELSE h.hora_salida - h.hora_entrada
                      END)
        
            THEN 
                (CASE 
                    WHEN EXTRACT(DOW FROM a.fecha) = 6 
                        THEN INTERVAL '4 hours'
                    ELSE h.hora_salida - h.hora_entrada
                 END)
                -
                (a.hora_salida_real - a.hora_entrada_real)
        
            ELSE INTERVAL '0'
        
        END AS pendiente,

        h.hora_entrada,
        h.hora_salida,
        
        a.estado_asistencia,
                   
        e.nombres || ' ' || e.apellidos AS empleado,

        CASE 
            WHEN EXTRACT(DOW FROM a.fecha) = 6 
                THEN 'SÁBADO 09:00 - 13:00'
            ELSE h.descripcion
        END AS descripcion

    FROM asistencia a
    JOIN empleado e ON a.id_empleado = e.id_empleado
    LEFT JOIN horario_empleado he 
            ON he.id_empleado = e.id_empleado
            AND a.fecha >= he.fecha_inicio
            AND (he.fecha_fin IS NULL OR a.fecha <= he.fecha_fin)
    LEFT JOIN horario h ON he.id_horario = h.id_horario

    WHERE a.fecha BETWEEN :inicio AND :fin
    ORDER BY e.id_empleado, a.fecha
    """, nativeQuery = true)
    List<Object[]> reporteExcel(
        @Param("inicio") LocalDate inicio,
        @Param("fin") LocalDate fin
    );
    
    @Query(value = """
        SELECT 
            a.id_empleado,
            e.nombres || ' ' || e.apellidos AS empleado,
            a.fecha,

            -- cálculo de sobretiempo (igual que en el principal)
            (
                (a.hora_salida_real - a.hora_entrada_real) -
                (CASE 
                    WHEN EXTRACT(DOW FROM a.fecha) = 6 
                        THEN INTERVAL '4 hours'
                    ELSE h.hora_salida - h.hora_entrada
                 END)
            ) AS sobretiempo,

            j.motivo

        FROM asistencia a
        JOIN empleado e ON a.id_empleado = e.id_empleado
        LEFT JOIN horario_empleado he 
               ON he.id_empleado = e.id_empleado
               AND a.fecha >= he.fecha_inicio
               AND (he.fecha_fin IS NULL OR a.fecha <= he.fecha_fin)
        LEFT JOIN horario h ON he.id_horario = h.id_horario
        LEFT JOIN justificacion j 
               ON j.id_asistencia = a.id_asistencia
               AND j.tipo_justificacion = 'SOBRETIEMPO'

        WHERE a.fecha BETWEEN :inicio AND :fin

        -- asegurar que haya horas reales
        AND a.hora_entrada_real IS NOT NULL
        AND a.hora_salida_real IS NOT NULL

        -- SOLO mayores a 30 minutos
        AND (
            (a.hora_salida_real - a.hora_entrada_real) -
            (CASE 
                WHEN EXTRACT(DOW FROM a.fecha) = 6 
                    THEN INTERVAL '4 hours'
                ELSE h.hora_salida - h.hora_entrada
             END)
        ) > INTERVAL '30 minutes'

        ORDER BY a.id_empleado, a.fecha
        """, nativeQuery = true)
    List<Object[]> reporteHorasExtras(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );
}