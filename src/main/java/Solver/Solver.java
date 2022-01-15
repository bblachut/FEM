package Solver;

import org.apache.commons.math3.linear.*;

import java.util.Arrays;

public class Solver {
    private final int n;

    public Solver(int n) {
        this.n = n;
    }

    public double[] solve() {
        RealMatrix bMatrix = new Array2DRowRealMatrix(n, n);
        RealVector lVector = new ArrayRealVector(n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                bMatrix.setEntry(i, j, B(i, j));
            }
            lVector.setEntry(i, L(i));
        }
        RealVector W = new LUDecomposition(bMatrix).getSolver().solve(lVector);
        return Arrays.copyOf(W.toArray(), n);
    }

    private double integrate(int i, int j) {
        double a_i = (2.0 / n) * (i - 1);
        double a_j = (2.0 / n) * (j - 1);
        double b_i = (2.0 / n) * (i + 1);
        double b_j = (2.0 / n) * (j + 1);
        if (a_j > b_i || a_i > b_j) {//it means that functions don't have common part
            return 0.0;
        }
        double a = Math.max(0, Math.max(a_i, a_j));
        double b = Math.min(2, Math.min(b_i, b_j));
        double[] w = {5.0 / 9, 8.0 / 9, 5.0 / 9};
        double[] x = {-((b - a) / 2) * Math.sqrt(3.0 / 5) + (b + a) / 2, (b + a) / 2, ((b - a) / 2) * Math.sqrt(3.0 / 5) + (b + a) / 2};
        double res = 0;
        for (int k = 0; k < w.length; k++) {
            res += w[k] * eDx(i, x[k]) * eDx(j, x[k]);
        }
        return ((b - a) / 2) * res;
    }

    public double e(int i, double x) {
        double h = 2.0 / n;
        double xi = i * h;
        double left = xi - h;
        double right = xi + h;
        if (x < left || x > right) {
            return 0.0;
        }
        if (x < xi) {
            return (x - left) / h;
        }
        return (right - x) / h;
    }

    private double eDx(int i, double x) {
        double h = 2.0 / n;
        double xi = i * h;
        double left = Math.max(xi - h, 0.0);
        double right = Math.min(xi + h, 2.0);
        if (x <= left || x >= right) {
            return 0.0;
        }
        if (x < xi) {
            return 1/h;
        }
        return -1/h;

    }

    private double B(int i, int j) {
        return -e(i, 0) * e(j, 0) + integrate(i, j);
    }

    public double L(int m) {
        return -20 * e(m, 0);
    }
}
