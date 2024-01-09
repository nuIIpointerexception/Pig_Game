package dev.codenmore.tilegame.world.noise;

import java.util.Random;

public class PerlinNoise {
    double[][] noise;
    int noise_width;
    int noise_height;
    Random random;

    public PerlinNoise(int inHei, int inWid) {
        noise = new double[inHei][inWid];
        noise_width = inWid;
        noise_height = inHei;
        random = new Random();
    }

    public PerlinNoise(int inHei, int inWid, int seed) {
        noise = new double[inHei][inWid];
        noise_width = inWid;
        noise_height = inHei;
        random = new Random(seed);
    }

    public double[][] generate_noise(int width, int height, double frequency, int octaves) {

        // """Generates a 2d array of random noise."""

        // this.noise[];
        this.noise_width = width;
        this.noise_height = height;

        for (int y = 0; y < this.noise_height; y++) {
            for (int x = 0; x < this.noise_width; x++) {
                noise[y][x] = random.nextInt(1000) / 1000.0;
            }
        }

        // result = [];

        for (int y = 0; y < this.noise_height; y++) {
            for (int x = 0; x < this.noise_width; x++) {
                noise[y][x] = (this.turbulence(x * frequency, y * frequency, octaves));
            }
        }

        return noise;
    }

    public double smooth_noise(double x, double y) {

        // Returns the average value of the 4 neighbors of (x, y) from the
        // noise array.

        int fractX = (int) (x - x);
        int fractY = (int) (y - y);

        int x1 = ((int) x + this.noise_width) % this.noise_width;
        int y1 = ((int) y + this.noise_height) % this.noise_height;

        int x2 = (x1 + this.noise_width - 1) % this.noise_width;
        int y2 = (y1 + this.noise_height - 1) % this.noise_height;

        double value = 0.0;
        value += fractX * fractY * this.noise[y1][x1];
        value += fractX * (1 - fractY) * this.noise[y2][x1];
        value += (1 - fractX) * fractY * this.noise[y1][x2];
        value += (1 - fractX) * (1 - fractY) * this.noise[y2][x2];

        return value;
    }

    public double turbulence(double x, double y, int size) {

        // """This function controls how far we zoom in/out of the noise array.
        // The further zoomed in gives less detail and is more blurry."""

        double value = 0.0;
        size *= 1.0;
        double initial_size = size;

        while (size >= 1) {
            value += this.smooth_noise(x / size, y / size) * size;
            size /= 2.0;
        }
        double temp = 128.0 * value / initial_size;
        if (temp > 255) {
            int i = 0;
            if (i == 1) {

            }
        }
        return temp;
    }
}