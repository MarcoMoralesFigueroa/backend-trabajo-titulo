package com.tt.trabajotitulo.format;

import java.util.ArrayList;

public class FormatSolucion {
    private static ArrayList<Vehiculo> vehiculos = null;
    private static ArrayList<Ruta> rutas = null;
    private static Vehiculo vehiculo = null;
    private static Ruta ruta = null;

    public static ArrayList<Vehiculo> format(ArrayList<ArrayList<Integer>> solucion) {
        vehiculos = new ArrayList<Vehiculo>();
        int i = 0;

        for (ArrayList<Integer> lista : solucion) {
            int k = 0;
            rutas = new ArrayList<Ruta>();
            int listaSize = lista.size();

            ruta = new Ruta(0, "Origen");
            rutas.add(ruta);
            k++;

            for (int j = 1; j < listaSize - 1; j++ ) {
                Integer valor = lista.get(j);
                ruta = new Ruta(valor);
                rutas.add(ruta);
                k++;
            }

            ruta = new Ruta(0, "Fin");
            rutas.add(ruta);
            k++;

            vehiculo = new Vehiculo(i, rutas);
            vehiculos.add(vehiculo);
            i++;
        }

        return vehiculos;
    }
}
