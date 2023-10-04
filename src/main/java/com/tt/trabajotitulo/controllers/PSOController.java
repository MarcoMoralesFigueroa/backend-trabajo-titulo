package com.tt.trabajotitulo.controllers;

import com.tt.trabajotitulo.PSO_DARP.PSO_DARP;
import com.tt.trabajotitulo.format.FormatSolucion;
import com.tt.trabajotitulo.format.Vehiculo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class PSOController {

    @RequestMapping(value = "api/PSO-DARP/{instance}/{T}/{nParticules}")
    public ArrayList<Vehiculo> getDARP(@PathVariable int instance, @PathVariable int T, @PathVariable int nParticules) {
        PSO_DARP pso;
        pso = new PSO_DARP();

        return FormatSolucion.format(ordenarSolucion(PSO_DARP.run(instance, T , nParticules)));
    }

    public ArrayList<ArrayList<Integer>> ordenarSolucion(int [][] solucion){
        ArrayList<ArrayList<Integer>> solucionOrdenada = new ArrayList<>();

        for (int[] s : solucion) {
            ArrayList<Integer> temp = new ArrayList<>();
            int k = 0;
            temp.add(0);
            do {
                temp.add(s[k]);
                k = temp.get(temp.size() - 1); // Acceder al último elemento añadido en temp
            } while (k != 0);
            solucionOrdenada.add(temp);
        }

        return solucionOrdenada ;
    }
}

