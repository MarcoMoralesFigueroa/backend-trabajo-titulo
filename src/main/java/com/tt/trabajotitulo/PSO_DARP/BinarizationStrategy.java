package com.tt.trabajotitulo.PSO_DARP;

/**
 * The {@code BinarizationStrategy} class provides static methods
 * for transforming a number from continuous domain to binary domain.
 * For that, this class includes various instances of the S and V
 * shape as a transfer function. Then, to binarize a real value, a
 * discretization method is applied. Finally, the binary value is
 * returned.
 *
 * @author Rodrigo Olivares
 * @date april, 2014
 */

public class BinarizationStrategy {

    private static double sShape2(double x) {
        return (1 / (1 + Math.pow(Math.E, -x)));
    }

    protected static double toDiscreet(double x) {
        return sShape2(x);
    }
}