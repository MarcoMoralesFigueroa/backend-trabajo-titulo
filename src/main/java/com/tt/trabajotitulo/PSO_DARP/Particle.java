package com.tt.trabajotitulo.PSO_DARP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class Particle {

    private final int type = Problem.type;
    // private final int instance = Problem.instance[Problem.type];
    protected final int N = Problem.N[type]; // lugares de recogida y parada de clientes
    protected final int n = Problem.n[type]; // numero de clientes
    protected final int m = Problem.m[type]; // numero de vehiculos
    protected final int C = Problem.C[type]; // capacidad de vehiculos

    private int[][] x = new int[m][N];
    private double[][] v = new double[m][N];
    private double[][] T = new double[m][N];
    private double[][] W = new double[m][N];
    private int[][] L = new int[m][N];
    private double[] tTotal = new double[m];
    private int tInfactible = 0;
    protected double fitnessPBest = Double.MAX_VALUE;
    private int mejorIteracion;

    protected int[][] pBest = new int[m][N];
    private double[][] vBest = new double[m][N];
    private double[][] TBest = new double[m][N];
    private double[][] WBest = new double[m][N];
    private int[][] LBest = new int[m][N];
    private double[] tTotalBest = new double[m];
    private int tInfactibleBest = 0;

    private int[] revisados = new int[N];
    private int[] paradaAnterior = new int[m];
    private int[] capActual = new int[m];
    private double maxNorm = maxNorm();

    private double w1 = 0.5, w2 = 0.5, w3 = 0.5, w4 = 0.5, w5 = 0.5, w6 = 0.5, w7 = 0.5;

    public Particle() {
        for (int i = 0; i < m; i++) {
            paradaAnterior[i] = 0;
            capActual[i] = 0;
            tTotal[i] = 0;
            for (int k = 0; k < N; k++) {
                v[i][k] = Distribution.uniform();
                x[i][k] = -1;
                revisados[k] = -1;
                T[i][k] = -1;
                W[i][k] = -1;
                L[i][k] = -1;
            }
        }

        double tEspera = 0;
        int j = 0;
        int sigParada;
        int porRecoger = n;
        double probRecoger;
        for (int i = 0; i < N - 2; i++) {
            j = Distribution.uniform(m);
            probRecoger = Distribution.uniform();
            if (capActual[j] < C && porRecoger > 0 && probRecoger > 0.5) {
                sigParada = recoger(j);

                tTotal[j] += tiempoEntreNodos(sigParada, paradaAnterior[j]);
                T[j][sigParada] = tTotal[j];

                if (tTotal[j] < Problem.iVentanaTiempo[type][sigParada]) {
                    tTotal[j] = Problem.iVentanaTiempo[type][sigParada] - tTotal[j];
                    W[j][sigParada] = tEspera;
                    tTotal[j] += tEspera;
                } else {
                    W[j][sigParada] = 0;
                }

                x[j][paradaAnterior[j]] = sigParada;
                paradaAnterior[j] = sigParada;

                tTotal[j] += 3;
                capActual[j]++;
                porRecoger--;
                L[j][paradaAnterior[j]] = capActual[j];
            } else {
                if (capActual[j] != 0) {
                    sigParada = dejar(j);

                    tTotal[j] += tiempoEntreNodos(sigParada, paradaAnterior[j]);
                    T[j][sigParada] = tTotal[j];

                    if (tTotal[j] < Problem.iVentanaTiempo[type][sigParada]) {
                        tEspera = Problem.iVentanaTiempo[type][sigParada] - tTotal[j];
                        W[j][sigParada] = tEspera;
                        tTotal[j] += tEspera;
                    } else {
                        W[j][sigParada] = 0;
                    }

                    x[j][paradaAnterior[j]] = sigParada;
                    paradaAnterior[j] = sigParada;

                    tTotal[j] += 3;
                    capActual[j]--;
                    L[j][paradaAnterior[j]] = capActual[j];
                } else {
                    i--;
                }
            }
        }

        for (int i = 0; i < m; i++) {
            x[i][paradaAnterior[i]] = 0;
        }
    }

    protected int recoger(int auto) {
        int sigParada;
        do {
            sigParada = Distribution.discrete(n + 1); // n + 1 o
        } while (revisados[sigParada] != -1);

        revisados[sigParada] = auto;

        return sigParada;
    }

    protected int dejar(int auto) {
        int sigParada;

        do {
            sigParada = Distribution.uniform(n + 1) + (n);
        } while (revisados[sigParada] != -1 || revisados[sigParada - (n)] != auto);

        revisados[sigParada] = auto;

        return sigParada;
    }

    private double tiempoEntreNodos(int sigParada, int paradaAnterior) {

        return Math.sqrt(Math.pow(Problem.x[type][sigParada] - Problem.x[type][paradaAnterior], 2)
                + Math.pow(Problem.y[type][sigParada] - Problem.y[type][paradaAnterior], 2));

    }

    public boolean isBetterThanPBest() {
        return fitness() < fitnessPBest;
    }

    public boolean isBetterThan(Particle g) {
        if (fitnessPBest() < g.fitnessPBest()) {
            return true;
        } else {
            return false;
        }
    }

    protected void updatePBest() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < N; j++) {
                pBest[i][j] = x[i][j];
                WBest[i][j] = W[i][j];
                TBest[i][j] = T[i][j];
                LBest[i][j] = L[i][j];
            }
            tTotalBest[i] = tTotal[i];
        }

        tInfactibleBest = tInfactible;
    }

    protected void updateFitness() {
        fitnessPBest = fitnessPBest();
    }

    private double fitness() {
        double[] sumas = new double[7];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < N; j++) {
                if (x[i][j] != -1) {
//    				w1
                    sumas[0] += Math.sqrt(Math.pow(Problem.x[type][x[i][j]] - Problem.x[type][j], 2)
                            + Math.pow(Problem.y[type][x[i][j]] - Problem.y[type][j], 2));

//    				w3
                    sumas[2] += W[i][j] * (L[i][j] - Problem.l[type][j]);

//    				w5
                    sumas[4] += Math.max(0, Math.max(Problem.iVentanaTiempo[type][j] - T[i][j],
                            T[i][j] - Problem.tVentanaTiempo[type][j]));
                }
            }

            for (int j = 1; j < n; j++) {
                if (x[i][j] != -1) {
//					w2
                    sumas[1] += T[i][j + n] - Problem.s[type] - T[i][j] - tiempoEntreNodos(j + n, j);

//    				w6
                    sumas[5] += Math.max(0, (T[i][j + n] - T[i][j]) - Problem.MRT[type]);
                }
            }
//    		w4
            sumas[3] += tTotal[i] - T[i][x[i][0]];

//    		w7
            sumas[6] += Math.max(0, (tTotal[i] - T[i][x[i][0]]) - Problem.MRD[type]);

        }

        return (sumas[0] * w1 + sumas[1] * w2 + sumas[2] * w3 + sumas[3] * w4 + sumas[4] * w5 + sumas[5] * w6
                + sumas[6] * w7) * penalizacion();
    }

    private double fitnessPBest() {
        double[] sumas = new double[7];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < N; j++) {
                if (pBest[i][j] != -1) {
//    				w1
                    sumas[0] += Math.sqrt(Math.pow(Problem.x[type][pBest[i][j]] - Problem.x[type][j], 2)
                            + Math.pow(Problem.y[type][pBest[i][j]] - Problem.y[type][j], 2));

//    				w3
                    sumas[2] += WBest[i][j] * (LBest[i][j] - Problem.l[type][j]);

//    				w5
                    sumas[4] += Math.max(0, Math.max(Problem.iVentanaTiempo[type][j] - TBest[i][j],
                            TBest[i][j] - Problem.tVentanaTiempo[type][j]));
                }
            }

            for (int j = 1; j < n; j++) {
                if (pBest[i][j] != -1) {
//					w2
                    sumas[1] += TBest[i][j + n] - Problem.s[type] - TBest[i][j] - tiempoEntreNodos(j + n, j);

//    				w6
                    sumas[5] += Math.max(0, (TBest[i][j + n] - TBest[i][j]) - Problem.MRT[type]);
                }
            }
//    		w4
            sumas[3] += tTotalBest[i] - TBest[i][pBest[i][0]];
            // tTotal
//    		w7
            sumas[6] += Math.max(0, (tTotalBest[i] - TBest[i][pBest[i][0]]) - Problem.MRD[type]);

        }

        return (sumas[0] * w1 + sumas[1] * w2 + sumas[2] * w3 + sumas[3] * w4 + sumas[4] * w5 + sumas[5] * w6
                + sumas[6] * w7) * penalizacionBest();
    }

    private double penalizacionBest() {
        if (tInfactibleBest == 0) {
            return 0.75;
        } else if (tInfactibleBest >= (int) (n) / 4 - 1) {
            return 1 + (0.25 * tInfactibleBest);
        } else if (tInfactibleBest >= (int) ((n) / 4 * 2) - 2) {
            return 1 + (0.5 * tInfactibleBest);
        } else if (tInfactibleBest >= (int) ((n) / 4 * 3) - 3) {
            return 1 + (0.75 * tInfactibleBest);
        }
        return 1 + (0.1 * tInfactibleBest);
    }

    protected double penalizacion() {
        if (tInfactible == 0) {
            return 0.75;
        } else if (tInfactible >= (int) (n) / 4 - 1) {
            return 1 + (0.25 * tInfactible);
        } else if (tInfactible >= (int) ((n) / 4 * 2) - 2) {
            return 1 + (0.5 * tInfactible);
        } else if (tInfactible >= (int) ((n) / 4 * 3) - 3) {
            return 1 + (0.75 * tInfactible);
        }
        return 1 + (0.1 * tInfactible);
    }

    protected void move(Particle g, float w, float c1, float c2) {
        int[][] xTemp = new int[m][N];

        tInfactible = 0;
        for (int i = 0; i < m; i++) {
            paradaAnterior[i] = 0;
            capActual[i] = 0;
            tTotal[i] = 0;
            for (int j = 0; j < N; j++) {
                xTemp[i][j] = -1;
                revisados[j] = -1;
                T[i][j] = -1;
                W[i][j] = -1;
                L[i][j] = -1;
            }
        }

        double tEspera = 0;
        int j = 0;
        int sigParada = 0;
        int porRecoger = n;
        double probRecoger;
        for (int i = 0; i < N - 2; i++) {
            j = Distribution.uniform(m);
            probRecoger = Distribution.uniform();
            if (capActual[j] < C && porRecoger > 0 && probRecoger > 0.5) {

                sigParada = moveRecoger(j, g, w, c1, c2);

                tTotal[j] += tiempoEntreNodos(sigParada, paradaAnterior[j]);
                T[j][sigParada] = tTotal[j];

                if (tTotal[j] < Problem.iVentanaTiempo[type][sigParada]) {
                    tTotal[j] = Problem.iVentanaTiempo[type][sigParada] - tTotal[j];
                    W[j][sigParada] = tEspera;
                    tTotal[j] += tEspera;
                } else {
                    W[j][sigParada] = 0;
                }

                xTemp[j][paradaAnterior[j]] = sigParada;
                paradaAnterior[j] = sigParada;

                tTotal[j] += 3;
                capActual[j]++;
                porRecoger--;
                L[j][paradaAnterior[j]] = capActual[j];
            } else {
                if (capActual[j] != 0) {
                    sigParada = moveDejar(j, g, w, c1, c2);

                    tTotal[j] += tiempoEntreNodos(sigParada, paradaAnterior[j]);
                    T[j][sigParada] = tTotal[j];

                    if (tTotal[j] < Problem.iVentanaTiempo[type][sigParada]) {
                        tTotal[j] = Problem.iVentanaTiempo[type][sigParada] - tTotal[j];
                        W[j][sigParada] = tEspera;
                        tTotal[j] += tEspera;
                    } else {
                        W[j][sigParada] = 0;
                    }

                    xTemp[j][paradaAnterior[j]] = sigParada;
                    paradaAnterior[j] = sigParada;

                    tTotal[j] += 3;
                    capActual[j]--;
                    L[j][paradaAnterior[j]] = capActual[j];
                } else {
                    i--;
                }
            }

        }

        for (int i = 0; i < m; i++) {
            xTemp[i][paradaAnterior[i]] = 0;
        }

        for (int k = 0; k < m; k++) {
            for (int i = 0; i < N; i++) {
                x[k][i] = xTemp[k][i];
            }
        }
    }

    private int moveRecoger(int i, Particle g, double w, double c1, double c2) {
        int sigParada;
//		rango de la normalizacion de "a" a "b"
        int a = -3;
        int b = 3;
        double norm, temp;

        if (v[i][paradaAnterior[i]] < 0.0001) {
            v[i][paradaAnterior[i]] = Distribution.uniform();
        }

        v[i][paradaAnterior[i]] = v[i][paradaAnterior[i]] * w
                + c1 * Distribution.uniform() * (pBest[i][paradaAnterior[i]] - x[i][paradaAnterior[i]])
                + c2 * Distribution.uniform() * (g.pBest[i][paradaAnterior[i]] - x[i][paradaAnterior[i]]);
        temp = x[i][paradaAnterior[i]] + v[i][paradaAnterior[i]];

//		0 y 38.7 valores minimos y maximos respectivamente de x + v
        norm = a + (((temp - 0) * (b - a)) / (38.7 - 0));

        sigParada = discretizationStrategy(norm);

        if (revisados[sigParada] != -1) {
            sigParada = recoger(i);
        } else {
            revisados[sigParada] = i;
        }

        return sigParada;
    }

    private int moveDejar(int i, Particle g, double w, double c1, double c2) {
        int sigParada;

        int a = -3, b = 3;
        double norm, temp;

        if (v[i][paradaAnterior[i]] < 0.0001) {
            v[i][paradaAnterior[i]] = Distribution.uniform();
        }

        v[i][paradaAnterior[i]] = v[i][paradaAnterior[i]] * w
                + c1 * Distribution.uniform() * (pBest[i][paradaAnterior[i]] - x[i][paradaAnterior[i]])
                + c2 * Distribution.uniform() * (g.pBest[i][paradaAnterior[i]] - x[i][paradaAnterior[i]]);

        temp = x[i][paradaAnterior[i]] + v[i][paradaAnterior[i]];

        norm = a + (((temp - 0) * (b - a)) / (maxNorm - 0));

        sigParada = discretizationStrategy(norm) + n;

//		"reparar"
        if (revisados[sigParada] != -1 || revisados[sigParada - (n)] != i) {
            sigParada = dejar(i);
        } else {
            revisados[sigParada] = i;
        }

        return sigParada;
    }

    private int discretizationStrategy(double k) {
        double sigmoide = BinarizationStrategy.toDiscreet(k);
        double distanciaMenor = Integer.MAX_VALUE;
        double distancia;
        double param;
        int discretizado = 0;

        for (int i = 1; i < n + 1; i++) {

            param = (double) i / (n + 1);
            distancia = Math.abs(param - Math.abs(sigmoide));

            if (distancia < distanciaMenor) {

                discretizado = i;
                distanciaMenor = distancia;
            }
        }
        return discretizado;
    }

    public boolean isFeasible() {
        for (int i = 0; i < m; i++) {
            if (tTotal[i] > Problem.MRD[type]) {
                return false;
            }

            for (int j = 0; j < n; j++) {
                if (T[i][j] != -1) {
                    if (T[i][j + (n)] - T[i][j] > Problem.MRT[type]) {
                        tInfactible++;
                    }
                }
            }

        }

        return true;
    }

    private double maxNorm() {
        double maximo = 0;
        for (int i = 0; i <= n * 2; i++) {
            for (int j = 0; j <= n * 2; j++) {
                for (int k = 0; k <= n * 2; k++) {
                    double temp = (0.999 * 0.3 + 0.6 * 0.999 * (j - i) + 0.6 * 0.999 * (k - i)) + i;

                    maximo = Math.max(maximo, temp);
                }
            }
        }
        return maximo;
    }

    protected void copy(int[][] paramX, int[][] paramPBest, double[][] paramV, double[][] paramT, int[][] paramL,
                        double[][] paramW, int paramTInfactible, int paramTInfactibleBest, double[][] paramVBest,
                        double[][] paramTBest, int[][] paramLBest, double[][] paramWBest, double paramFitnessPBest, int iteracion,
                        double[] paramTTotal, double[] paramTTotalBest) {
        mejorIteracion = iteracion;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < N; j++) {
                this.x[i][j] = paramX[i][j];
                this.v[i][j] = paramV[i][j];
                this.T[i][j] = paramT[i][j];
                this.L[i][j] = paramL[i][j];
                this.W[i][j] = paramW[i][j];
                this.pBest[i][j] = paramPBest[i][j];
                this.vBest[i][j] = paramVBest[i][j];
                this.TBest[i][j] = paramTBest[i][j];
                this.LBest[i][j] = paramLBest[i][j];
                this.WBest[i][j] = paramWBest[i][j];
            }
            this.tTotal[i] = paramTTotal[i];
            this.tTotalBest[i] = paramTTotalBest[i];
        }
        this.tInfactibleBest = paramTInfactibleBest;
        this.tInfactible = paramTInfactible;
        this.fitnessPBest = paramFitnessPBest;
    }

    protected int[][] getX() {
        return x;
    }

    protected int[][] getPBest() {
        return pBest;
    }

    protected double[][] getV() {
        return v;
    }

    protected double[][] getT() {
        return T;
    }

    protected int[][] getL() {
        return L;
    }

    protected double[][] getW() {
        return W;
    }

    protected int getTInfactible() {
        return tInfactible;
    }

    protected double[][] getVBest() {
        return vBest;
    }

    protected double[][] getTBest() {
        return TBest;
    }

    protected int[][] getLBest() {
        return LBest;
    }

    protected double[][] getWBest() {
        return WBest;
    }

    protected double getFitness() {
        return fitness();
    }

    protected int getInfactibles() {
        return tInfactible;
    }

    protected int getMejorIteracion() {
        return mejorIteracion;
    }

    protected double getFitnessPBest() {
        return fitnessPBest();
    }

    protected int getTInfactibleBest() {
        return tInfactibleBest;
    }

    protected double[] getTTotal() {
        return tTotal;
    }

    protected double[] getTTotalBest() {
        return tTotalBest;
    }

}

