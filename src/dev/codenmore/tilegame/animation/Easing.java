package dev.codenmore.tilegame.animation;

public class Easing {
    public static float lerp(float start, float end, float t) {
        return start + t * (end - start);
    }
}

