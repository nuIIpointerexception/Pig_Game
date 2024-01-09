package dev.codenmore.tilegame.world.noise;

import java.util.Random;

public class PerlinNoiseGenerator {

    private int repeat;
    private int[] permutation;

    private double scale = 1.0;
    private double frequency = 1.0;

    public PerlinNoiseGenerator(long seed) {
        Random random = new Random(seed);
        permutation = new int[512];
        int[] p = new int[256];

        for (int i = 0; i < 256; i++) {
            p[i] = i;
        }

        for (int i = 255; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = p[index];
            p[index] = p[i];
            p[i] = temp;
        }

        for (int i = 0; i < 512; i++) {
            permutation[i] = p[i & 255];
        }
    }

    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    private double grad(int hash, double x, double y, double z) {
        int h = hash & 15;
        double u = h < 8 ? x : y;
        double v = h < 4 ? y : h == 12 || h == 14 ? x : z;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public double getNoise(double x, double y, double z) {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        return (generateNoise(x, y, z) + 1) / 2 * scale;
    }

    public double generateNoise(double x, double y, double z) {
        int xi = (int) x & 255;
        int yi = (int) y & 255;
        int zi = (int) z & 255;

        double xf = x - (int) x;
        double yf = y - (int) y;
        double zf = z - (int) z;

        double u = fade(xf);
        double v = fade(yf);
        double w = fade(zf);

        int aaa, aba, aab, abb, baa, bba, bab, bbb;
        aaa = permutation[permutation[permutation[xi] + yi] + zi];
        aba = permutation[permutation[permutation[xi] + inc(yi)] + zi];
        aab = permutation[permutation[permutation[xi] + yi] + inc(zi)];
        abb = permutation[permutation[permutation[xi] + inc(yi)] + inc(zi)];
        baa = permutation[permutation[permutation[inc(xi)] + yi] + zi];
        bba = permutation[permutation[permutation[inc(xi)] + inc(yi)] + zi];
        bab = permutation[permutation[permutation[inc(xi)] + yi] + inc(zi)];
        bbb = permutation[permutation[permutation[inc(xi)] + inc(yi)] + inc(zi)];

        double x1, x2, y1, y2;
        x1 = lerp(grad(aaa, xf, yf, zf), grad(baa, xf - 1, yf, zf), u);
        x2 = lerp(grad(aba, xf, yf - 1, zf), grad(bba, xf - 1, yf - 1, zf), u);
        y1 = lerp(x1, x2, v);

        x1 = lerp(grad(aab, xf, yf, zf - 1), grad(bab, xf - 1, yf, zf - 1), u);
        x2 = lerp(grad(abb, xf, yf - 1, zf - 1), grad(bbb, xf - 1, yf - 1, zf - 1), u);
        y2 = lerp(x1, x2, v);

        return (lerp(y1, y2, w) + 1) / 2;
    }

    private int inc(int num) {
        num++;
        if (repeat > 0) num %= repeat;
        return num;
    }

    public double getNoise(double x, double y) {
        return getNoise(x, y, 0);
    }
}
