package stelitop.dudesbot.discord.listeners;

import discord4j.core.event.domain.interaction.ComponentInteractionEvent;
import reactor.core.publisher.Mono;

public interface IActionComponentListener<T extends ComponentInteractionEvent> {

    /**
     * Gets the unique component id of this component. Essentially a constant.
     *
     * @return The component id of this
     */
    String getComponentId();

    /**
     * Handles the event of the interaction with a given component.
     *
     * @param event The component event.
     * @return Reply.
     */
    Mono<Void> handle(T event);
}
