package com.tt.trabajotitulo.PSO_DARP;

public class PSO_DARP {

    public static int[][] run(int instance, int T, int nParticules) {
        int i = instance;
        Problem.type = i;
        Swarm s = new Swarm();
        int[][] solucion = s.execute(T, nParticules);

        for (int j = 0; j < Problem.m[i]; j++) {
            for (int l = 0; l < Problem.N[i] - 1; l++) {
                if (l == Problem.n[i]) {
                }
            }
        }
        return solucion;
    }
}
