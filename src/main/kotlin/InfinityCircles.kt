import com.google.inject.Inject
import org.slf4j.Logger
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GamePreInitializationEvent
import org.spongepowered.api.event.game.state.GameStoppingEvent
import org.spongepowered.api.plugin.Plugin

/**
 * Created by karayuu on 2018/06/26
 */

@Plugin(id = InfinityCircles.ID, name = "InfinityCircles", version = "1.0.0", authors = ["karayuu"])
class InfinityCircles {
    @Inject lateinit var logger: Logger
        private set

    @Listener
    fun onPreInitialization(event: GamePreInitializationEvent) {
        logger.info("Start plugin.")
    }

    @Listener
    fun onGameStopped(event: GameStoppingEvent) {
        logger.info("Stop plugin.")
    }

    companion object {
        const val ID = "infinity-circles"
    }
}
