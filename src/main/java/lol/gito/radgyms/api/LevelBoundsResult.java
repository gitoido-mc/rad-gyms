package lol.gito.radgyms.api;

/**
 * Result for overriding level bounds.
 * - maxOverride: if non-null, use as the max level.
 * - enforceMin: if true AND minOverride is non-null, use that minimum.
 */
public final class LevelBoundsResult {
    private final Integer minOverride;
    private final Integer maxOverride;

    private LevelBoundsResult(Integer minOverride, Integer maxOverride) {
        this.maxOverride = maxOverride;
        this.minOverride = minOverride;
    }

    public static LevelBoundsResult pass() {
        return new LevelBoundsResult(null, null);
    }

    public static LevelBoundsResult overrideMax(int max) {
        return new LevelBoundsResult(null, max);
    }

    public static LevelBoundsResult overrideMin(int min) {
        return new LevelBoundsResult(min, null);
    }

    public static LevelBoundsResult override(int min, int max) {
        return new LevelBoundsResult(min, max);
    }

    public Integer maxOverride() { return maxOverride; }
    public Integer minOverride() { return minOverride; }

    /** Merge two results; rhs takes precedence when non-null / true. */
    public LevelBoundsResult merge(LevelBoundsResult rhs) {
        if (rhs == null) return this;
        Integer max = rhs.maxOverride != null ? rhs.maxOverride : this.maxOverride;
        Integer min = rhs.minOverride != null ? rhs.minOverride : this.minOverride;
        return new LevelBoundsResult(min, max);
    }
}
