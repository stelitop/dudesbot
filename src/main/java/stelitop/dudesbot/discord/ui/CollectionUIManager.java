package stelitop.dudesbot.discord.ui;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CollectionUIManager {

    private Map<Long, CollectionUI> messageIdToUI = new HashMap<>();

    public void saveUI(long messageId, CollectionUI ui) {
        messageIdToUI.put(messageId, ui);
    }

    /**
     * Gets the collection UI of a given message if it exists.
     *
     * @param messageId The ID of the message.
     * @return The UI of the message if there is one.
     */
    public Optional<CollectionUI> getUI(long messageId) {
        if (!messageIdToUI.containsKey(messageId)) return Optional.empty();
        else return Optional.of(messageIdToUI.get(messageId));
    }
}
