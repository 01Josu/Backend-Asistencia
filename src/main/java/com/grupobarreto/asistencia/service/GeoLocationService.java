package com.grupobarreto.asistencia.service;


import com.grupobarreto.asistencia.model.Sede;
import com.grupobarreto.asistencia.repository.SedeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeoLocationService {

    private static final double RADIO_TIERRA = 6371000; //metros

    @Autowired
    private SedeRepository sedeRepository;


    public Sede validarUbicacion(
            double latUsuario,
            double lonUsuario
    ) {

        List<Sede> sedes = sedeRepository.findByActivoTrue();

        for (Sede sede : sedes) {
            double distancia = calcularDistancia(
                    latUsuario,
                    lonUsuario,
                    sede.getLatitud(),
                    sede.getLongitud()
            );

            if (distancia <= sede.getRadioMetros()) {
                return sede;
            }
        }

        return null;
    }

    private double calcularDistancia(
            double lat1,
            double lon1,
            double lat2,
            double lon2
    ) {

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(
                Math.sqrt(a),
                Math.sqrt(1 - a)
        );

        return RADIO_TIERRA * c;
    }
}
