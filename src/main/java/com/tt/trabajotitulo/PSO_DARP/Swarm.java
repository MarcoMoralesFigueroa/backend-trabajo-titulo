package com.tt.trabajotitulo.PSO_DARP;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Swarm {

    private int T; // 5000
    private int nParticules; // 20
    private float w = 0.3f, c1 = 0.6f, c2 = 0.6f; // w = 0.3f, c1 = 0.6f, c2 = 0.6f

    private ArrayList<Particle> swarm = null;
    private Particle g = null;

    boolean firstCall;

//	private int cont = 0; //borrar

    public Swarm () {
    }

    public int[][] execute(int T, int nParticules) {
        this.T = T;
        this.nParticules = nParticules;

        init();
        run();
        return g.getPBest();
    }


    private void init() {
        swarm = new ArrayList<>();
        do {
            g = new Particle();
        } while (!g.isFeasible());
        Particle p = null;
        for (int i = 0; i <= nParticules; i++) {
            do {
                p = new Particle();
            } while (!p.isFeasible());
            p.updatePBest();
            swarm.add(p);
        }
        g.copy(swarm.get(0).getX(), swarm.get(0).getPBest(), swarm.get(0).getV(), swarm.get(0).getT(),
                swarm.get(0).getL(), swarm.get(0).getW(), swarm.get(0).getInfactibles(),
                swarm.get(0).getTInfactibleBest(), swarm.get(0).getVBest(), swarm.get(0).getTBest(),
                swarm.get(0).getLBest(), swarm.get(0).getWBest(), swarm.get(0).getFitnessPBest(), 0,
                swarm.get(0).getTTotal(), swarm.get(0).getTTotalBest());
        for (int i = 0; i < nParticules; i++) {
            if (swarm.get(i).isBetterThan(g)) {
                g.copy(swarm.get(i).getX(), swarm.get(i).getPBest(), swarm.get(i).getV(), swarm.get(i).getT(),
                        swarm.get(i).getL(), swarm.get(i).getW(), swarm.get(i).getInfactibles(),
                        swarm.get(i).getTInfactibleBest(), swarm.get(i).getVBest(), swarm.get(i).getTBest(),
                        swarm.get(i).getLBest(), swarm.get(i).getWBest(), swarm.get(i).getFitnessPBest(), 0,
                        swarm.get(i).getTTotal(), swarm.get(i).getTTotalBest());
            }
        }
    }

    private void run() {
        int t = 1;
        firstCall = false;
        while (t <= T) {
            for (int i = 0; i < nParticules; i++) {
                if (swarm.get(i).isBetterThanPBest()) {
                    swarm.get(i).updatePBest();
                    swarm.get(i).updateFitness();
                }
                if (swarm.get(i).isBetterThan(g)) {
                    g.copy(swarm.get(i).getX(), swarm.get(i).getPBest(), swarm.get(i).getV(), swarm.get(i).getT(),
                            swarm.get(i).getL(), swarm.get(i).getW(), swarm.get(i).getInfactibles(),
                            swarm.get(i).getTInfactibleBest(), swarm.get(i).getVBest(), swarm.get(i).getTBest(),
                            swarm.get(i).getLBest(), swarm.get(i).getWBest(), swarm.get(i).getFitnessPBest(), t,
                            swarm.get(i).getTTotal(), swarm.get(i).getTTotalBest());
                }
            }
            for (int i = 0; i < nParticules; i++) {
                do {
                    swarm.get(i).move(g, w, c1, c2);
                } while (!swarm.get(i).isFeasible());
//					cont++;
            }
            t++;
        }
    }
}

