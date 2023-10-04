package com.tt.trabajotitulo.PSO_DARP;


import java.util.Random;

/**
 * The {@code Distribution} class provides static methods for generating random
 * number from various discrete and continuous distributions, including
 * Bernoulli, uniform, Gaussian, exponential, pareto, Poisson, and Cauchy. It
 * also provides method for shuffling an array or subarray.
 * <p>
 * For additional documentation, see
 * <a href="http://introcs.cs.princeton.edu/22library">Section 2.2</a> of
 * <i>Introduction to Programming in Java: An Interdisciplinary Approach</i> by
 * Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public final class Distribution {

    private static Random random; // pseudo-random number generator
    private static long seed = 4320; // pseudo-random number generator seed
    protected long initseed = 4320;

    // static initializer
    static {
        // this is how the seed was set in Java 1.4
        random = new Random(seed);
    }

    // don't instantiate
    private Distribution() {
    }

    /**
     * Returns a random real number uniformly in [0, 1).
     *
     * @return a random real number uniformly in [0, 1)
     */
    public static double uniform() {
        return random.nextDouble();
    }

    /**
     * Returns a random integer uniformly in [0, n).
     *
     * @param n number of possible integers
     * @return a random integer uniformly between 0 (inclusive) and <tt>N</tt>
     * (exclusive)
     * @throws IllegalArgumentException if <tt>n <= 0</tt>
     */
    public static int uniform(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Parameter N must be positive");
        }
        return random.nextInt(n);
    }

    ///////////////////////////////////////////////////////////////////////////
    // STATIC METHODS BELOW RELY ON JAVA.UTIL.RANDOM ONLY INDIRECTLY VIA
    // THE STATIC METHODS ABOVE.
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Returns a random integer uniformly in [a, b).
     *
     * @param a the left endpoint
     * @param b the right endpoint
     * @return a random integer uniformly in [a, b)
     * @throws IllegalArgumentException if <tt>b <= a</tt> @
     *                                  throws IllegalArgumentException if <tt>b - a >= Integer.MAX_VALUE</tt>
     */
    public static int uniform(int a, int b) {
        if (b <= a) {
            throw new IllegalArgumentException("Invalid range");
        }
        if ((long) b - a >= Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid range");
        }
        return a + uniform(b - a);
    }

    public static int discrete(int n) {
        if (n == 0) {
            throw new NullPointerException("argument is 0");
        }
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += i;
        }
        if (n < 0) {
            throw new IllegalArgumentException("the argument must not be nonnegative");
        }

        if (sum >= Integer.MAX_VALUE) {
            throw new IllegalArgumentException("sum of frequencies overflows an int");
        }

        // pick index i with probabilitity proportional to frequency
        double r = uniform((int) sum);
        sum = 0;
        for (int i = 0; i < n; i++) {
            sum += i;
            if (sum > r) {
                return i;
            }
        }

        // can't reach here
        assert false;
        return -1;
    }
}
