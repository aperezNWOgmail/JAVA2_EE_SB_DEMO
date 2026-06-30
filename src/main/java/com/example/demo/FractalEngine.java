package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class FractalEngine {

    // ─────────────────────────────────────────────────────────────────────────
    // FRACTAL KIND ENUM
    // ─────────────────────────────────────────────────────────────────────────

    public enum FractalKind {
        MANDELBROT(1),
        JULIA     (2),
        LEAF      (3);

        private final int value;

        FractalKind(int value) { this.value = value; }

        @JsonValue
        public int getValue() { return value; }

        @JsonCreator
        public static FractalKind fromValue(int value) {
            for (FractalKind kind : FractalKind.values()) {
                if (kind.value == value) return kind;
            }
            throw new IllegalArgumentException("Tipo de fractal inválido: " + value);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SHARED TYPES
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Wire format returned to Angular.
     * x, y       — pixel coordinates (integers, typed as double for
     *              backwards-compatibility with existing consumers)
     * intensity  — [0…255] encoding of the escape-time iteration count:
     *                0          → point is inside the set (maxIterations reached)
     *                1…255      → iter * 255 / maxIterations
     *              Angular's _adaptRemotePoints back-calculates the iteration
     *              from this value, so the formula must stay consistent.
     */
    public record FractalPoint(double x, double y, int intensity) {}

    /**
     * Zoom / pan parameters shared by all escape-time fractals.
     * Mirrors the ZoomParams interface on the Angular side so both ends
     * speak the same language.
     *
     * zoomInOut  — true  = zoom IN  (narrow the view window)
     *              false = zoom OUT (widen  the view window)
     * zoomStep   — multiplicative factor per zoom action (e.g. 1.5 or 2.0).
     *              Passing 1.0 leaves the view unchanged.
     * centerX    — complex-plane Re coordinate to zoom towards.
     *              Matches the reticle position sent by the Angular UI.
     * centerY    — complex-plane Im coordinate to zoom towards.
     */
    public record ZoomParams(
        boolean zoomInOut,
        double  zoomStep,
        double  centerX,
        double  centerY
    ) {}

    // ─────────────────────────────────────────────────────────────────────────
    // CANVAS DIMENSIONS  — single source of truth, must match Angular constants
    // ─────────────────────────────────────────────────────────────────────────
    private static final int    CANVAS_WIDTH   = 800;
    private static final int    CANVAS_HEIGHT  = 600;
    private static final int    MAX_ITERATIONS = 500;

    // ─────────────────────────────────────────────────────────────────────────
    // SHARED ZOOM MATH
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Applies zoom / pan to a complex-plane view window.
     *
     * Mirrors applyZoomToBounds() in the Angular service so both ends
     * produce identical coordinate transforms.
     *
     * Algorithm
     * ─────────
     * 1. Compute the half-widths of the current window.
     * 2. Divide by zoomStep (IN) or multiply by zoomStep (OUT) to get
     *    new half-widths.
     * 3. Re-centre the window around (centerX, centerY).
     *
     * @param minX     current left  bound of the complex plane view
     * @param maxX     current right bound
     * @param minY     current bottom bound
     * @param maxY     current top    bound
     * @param params   zoom direction, factor, and reticle center
     * @return         double[4] = { newMinX, newMaxX, newMinY, newMaxY }
     */
    public static double[] applyZoom(
        double minX, double maxX,
        double minY, double maxY,
        ZoomParams params
    ) {
        // factor > 1 narrows the window (zoom IN);
        // factor < 1 widens  the window (zoom OUT).
        // zoomInOut=true  → divide half-widths by zoomStep → narrower  → IN
        // zoomInOut=false → multiply half-widths by zoomStep → wider   → OUT
        double factor = params.zoomInOut() ? params.zoomStep() : (1.0 / params.zoomStep());

        double halfW = (maxX - minX) / 2.0;
        double halfH = (maxY - minY) / 2.0;

        double newHalfW = halfW / factor;
        double newHalfH = halfH / factor;

        double cx = params.centerX();
        double cy = params.centerY();

        return new double[] {
            cx - newHalfW,   // newMinX
            cx + newHalfW,   // newMaxX
            cy - newHalfH,   // newMinY
            cy + newHalfH    // newMaxY
        };
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SHARED INTENSITY ENCODING
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Encodes the escape-time iteration count as an [0…255] intensity value.
     *
     * Must stay in sync with _adaptRemotePoints() in the Angular service:
     *   Angular: value = round(intensity * maxIterations / 255)
     *   Java:    intensity = (iter == maxIterations) ? 0 : (iter * 255 / maxIterations)
     *
     * Special case: iter == maxIterations means the point is INSIDE the set
     * → intensity 0 → Angular maps this back to maxIterations → black pixel.
     */
    private static int encodeIntensity(int iter, int maxIterations) {
        return (iter == maxIterations) ? 0 : (iter * 255 / maxIterations);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ROUTER
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Orchestrates fractal generation, delegating to the correct engine
     * by fractal kind.
     */
    public List<FractalPoint> getFractal(
        FractalKind fractalKind,
        ZoomParams  zoomParams
    ) {
        return switch (fractalKind) {
            case MANDELBROT -> generateMandelbrot(zoomParams);
            case JULIA      -> generateJulia(zoomParams);
            case LEAF       -> generateLeaf();
        };
    }

    // ─────────────────────────────────────────────────────────────────────────
    // MANDELBROT  (new)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Generates the Mandelbrot set on a CANVAS_WIDTH × CANVAS_HEIGHT grid.
     *
     * Formula: z(n+1) = z(n)² + c,  z(0) = 0,  c = pixel coordinate
     *
     * Default view window: Re ∈ [-2.0, 1.0]  Im ∈ [-1.2, 1.2]
     * These match DEFAULT_BOUNDS_MANDELBROT in the Angular service so both
     * ends show the same view at zoom level 1.
     *
     * @param zoomParams  zoom direction, factor, and reticle center.
     *                    Pass zoomStep=1.0 for the default (unzoomed) view.
     */
    public static List<FractalPoint> generateMandelbrot(ZoomParams zoomParams) {
        List<FractalPoint> points = new ArrayList<>();

        // Default Mandelbrot bounds — must match Angular's DEFAULT_BOUNDS_MANDELBROT
        double minX = -2.0, maxX = 1.0;
        double minY = -1.2, maxY = 1.2;

        // Apply zoom / pan if the user has moved the reticle or zoomed
        if (zoomParams.zoomStep() != 1.0) {
            double[] zoomed = applyZoom(minX, maxX, minY, maxY, zoomParams);
            minX = zoomed[0]; maxX = zoomed[1];
            minY = zoomed[2]; maxY = zoomed[3];
        }

        double xRange = maxX - minX;
        double yRange = maxY - minY;

        for (int screenY = 0; screenY < CANVAS_HEIGHT; screenY++) {
            for (int screenX = 0; screenX < CANVAS_WIDTH; screenX++) {

                // Map pixel → complex plane coordinate
                double cRe = minX + (screenX * xRange / CANVAS_WIDTH);
                double cIm = minY + (screenY * yRange / CANVAS_HEIGHT);

                // Mandelbrot iteration: z starts at 0, c = pixel coordinate
                double zRe = 0.0, zIm = 0.0;
                int iter = 0;

                while (zRe * zRe + zIm * zIm <= 4.0 && iter < MAX_ITERATIONS) {
                    double nextRe = zRe * zRe - zIm * zIm + cRe;
                    double nextIm = 2.0 * zRe * zIm + cIm;
                    zRe = nextRe;
                    zIm = nextIm;
                    iter++;
                }

                points.add(new FractalPoint(screenX, screenY, encodeIntensity(iter, MAX_ITERATIONS)));
            }
        }

        return points;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // JULIA  (updated — zoom now uses centerX / centerY)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Generates the Julia set on a CANVAS_WIDTH × CANVAS_HEIGHT grid.
     *
     * Formula: z(n+1) = z(n)² + c,  z(0) = pixel coordinate,  c = fixed constant
     *
     * Default view window: Re ∈ [-1.5, 1.5]  Im ∈ [-1.5, 1.5]
     * These match DEFAULT_BOUNDS_JULIA in the Angular service.
     *
     * Changes from the original
     * ──────────────────────────
     * - Zoom now accepts a ZoomParams record instead of two loose parameters.
     * - centerX / centerY from ZoomParams replace the hardcoded (0,0) center,
     *   so the Angular reticle position is honoured.
     * - Zoom direction logic fixed: zoomInOut=true now reliably zooms IN
     *   (factor > 1 → narrower window) regardless of zoomStep value.
     * - encodeIntensity() extracted so the formula stays in one place.
     *
     * @param zoomParams  zoom direction, factor, and reticle center.
     *                    Pass zoomStep=1.0 for the default (unzoomed) view.
     */
    public static List<FractalPoint> generateJulia(ZoomParams zoomParams) {
        List<FractalPoint> points = new ArrayList<>();

        // Default Julia bounds — must match Angular's DEFAULT_BOUNDS_JULIA
        double minX = -1.5, maxX = 1.5;
        double minY = -1.5, maxY = 1.5;

        // Apply zoom / pan if the user has moved the reticle or zoomed
        if (zoomParams.zoomStep() != 1.0) {
            double[] zoomed = applyZoom(minX, maxX, minY, maxY, zoomParams);
            minX = zoomed[0]; maxX = zoomed[1];
            minY = zoomed[2]; maxY = zoomed[3];
        }

        double xRange = maxX - minX;
        double yRange = maxY - minY;

        // Fixed complex constant c — unchanged from original
        double cRe = -0.400;
        double cIm =  0.600;

        for (int screenY = 0; screenY < CANVAS_HEIGHT; screenY++) {
            for (int screenX = 0; screenX < CANVAS_WIDTH; screenX++) {

                // Map pixel → complex plane coordinate
                // Julia: z starts at the pixel coordinate, c is fixed
                double zRe = minX + (screenX * xRange / CANVAS_WIDTH);
                double zIm = minY + (screenY * yRange / CANVAS_HEIGHT);

                int iter = 0;
                while (zRe * zRe + zIm * zIm <= 4.0 && iter < MAX_ITERATIONS) {
                    double nextRe = zRe * zRe - zIm * zIm + cRe;
                    double nextIm = 2.0 * zRe * zIm + cIm;
                    zRe = nextRe;
                    zIm = nextIm;
                    iter++;
                }

                points.add(new FractalPoint(screenX, screenY, encodeIntensity(iter, MAX_ITERATIONS)));
            }
        }

        return points;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // BARNSLEY FERN  (unchanged)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * IFS Barnsley Fern — zoom does not apply to IFS attractors.
     * Unchanged from the original implementation.
     */
    public static List<FractalPoint> generateLeaf() {
        List<FractalPoint> points = new ArrayList<>();
        int[][] pixelGrid = new int[CANVAS_WIDTH][CANVAS_HEIGHT];

        double x = 0.0, y = 0.0;
        Random rand    = new Random();
        int totalPoints = 150_000;

        for (int i = 0; i < totalPoints; i++) {
            double nextX, nextY;
            int r = rand.nextInt(100);

            if (r < 1) {
                nextX = 0.0;
                nextY = 0.16 * y;
            } else if (r < 86) {
                nextX =  0.85 * x + 0.04 * y;
                nextY = -0.04 * x + 0.85 * y + 1.6;
            } else if (r < 93) {
                nextX =  0.20 * x - 0.26 * y;
                nextY =  0.23 * x + 0.22 * y + 1.6;
            } else {
                nextX = -0.15 * x + 0.28 * y;
                nextY =  0.26 * x + 0.24 * y + 0.44;
            }

            x = nextX;
            y = nextY;

            int screenX = (int) Math.round((x + 2.182) * (CANVAS_WIDTH  - 1) / (2.655 + 2.182));
            int screenY = (int) Math.round((9.96 - y)  * (CANVAS_HEIGHT - 1) / 9.96);

            if (screenX >= 0 && screenX < CANVAS_WIDTH && screenY >= 0 && screenY < CANVAS_HEIGHT) {
                pixelGrid[screenX][screenY] = 200;
            }
        }

        for (int px = 0; px < CANVAS_WIDTH; px++) {
            for (int py = 0; py < CANVAS_HEIGHT; py++) {
                if (pixelGrid[px][py] > 0) {
                    points.add(new FractalPoint(px, py, pixelGrid[px][py]));
                }
            }
        }

        return points;
    }
}
