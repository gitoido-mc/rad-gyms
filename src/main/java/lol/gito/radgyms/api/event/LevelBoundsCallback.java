package lol.gito.radgyms.api.event;

import lol.gito.radgyms.api.LevelBoundsResult;
import lol.gito.radgyms.api.context.LevelBoundsContext;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Fired when the level slider computes its min/max bounds.
 * Listeners can override the max, and optionally enforce a minimum.
 *
 * Multiple listeners are merged; later listeners take precedence.
 */
@FunctionalInterface
public interface LevelBoundsCallback {
    Event<LevelBoundsCallback> EVENT = EventFactory.createArrayBacked(
            LevelBoundsCallback.class,
            listeners -> ctx -> {
                LevelBoundsResult acc = LevelBoundsResult.pass();
                for (LevelBoundsCallback l : listeners) {
                    LevelBoundsResult r = l.onDecideBounds(ctx);
                    if (r != null) acc = acc.merge(r);
                }
                return acc;
            }
    );

    /**
     * @return a LevelBoundsResult (or null to pass) describing overrides.
     */
    LevelBoundsResult onDecideBounds(LevelBoundsContext ctx);
}
