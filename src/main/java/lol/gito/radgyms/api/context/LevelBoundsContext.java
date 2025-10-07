package lol.gito.radgyms.api.context;

import net.minecraft.client.gui.widget.SliderWidget;

/**
 * Context for deciding level bounds for the level slider.
 * Kept minimal and client-safe.
 */
public record LevelBoundsContext(int defaultMin, int defaultMax) {}
