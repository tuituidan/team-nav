package com.tuituidan.openhub.util;

import java.util.Random;
import lombok.experimental.UtilityClass;

/**
 * RandomColorUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2025/6/2
 */
@UtilityClass
public class RandomColorUtils {

    private static final Random RANDOM = new Random();

    /**
     * generate
     *
     * @return String
     */
    public static String generate() {
        double h = RANDOM.nextDouble() * 360;
        double s = RANDOM.nextDouble() * 0.5 + 0.5;
        double l = RANDOM.nextDouble() + 0.2;
        int rgb = hslToRgb(h / 360, s, l);
        return rgbToHex(rgb).toLowerCase();
    }

    private static int hslToRgb(double h, double s, double l) {
        double r;
        double g;
        double b;
        if (s == 0) {
            r = g = b = l;
        } else {
            double q = l < 0.5 ? l * (1 + s) : l + s - l * s;
            double p = 2 * l - q;
            r = hueToRgb(p, q, h + 1.0 / 3.0);
            g = hueToRgb(p, q, h);
            b = hueToRgb(p, q, h - 1.0 / 3.0);
        }
        return (int) (r * 255) << 16 | (int) (g * 255) << 8 | (int) (b * 255);
    }

    private static double hueToRgb(double p, double q, double t) {
        if (t < 0) {
            t += 1;
        }
        if (t > 1) {
            t -= 1;
        }
        if (t < 1.0 / 6.0) {
            return p + (q - p) * 6 * t;
        }
        if (t < 0.5) {
            return q;
        }
        if (t < 2.0 / 3.0) {
            return p + (q - p) * (2.0 / 3.0 - t) * 6;
        }
        return p;
    }

    private static String rgbToHex(int rgb) {
        return String.format("#%06X", (0xFFFFFF & rgb));
    }

}
