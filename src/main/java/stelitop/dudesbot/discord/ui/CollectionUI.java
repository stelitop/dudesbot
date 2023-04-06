package stelitop.dudesbot.discord.ui;

import discord4j.core.object.entity.User;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
public class CollectionUI {

    /**
     * The id of the user who created the collection UI.
     */
    @Getter
    private long creatorId;

//    /**
//     * The id of the person whose collection is being shown. May be different
//     * from the person who created the UI.
//     */
//    @Getter
//    private String collectionOwnerUsername;

    /**
     * The entries to be displayed in the given order.
     */
    @Getter
    private List<String> entries;

    /**
     * The current page for the UI to display.
     */
    @Builder.Default
    @Setter
    private int currentPage = 1;

    /**
     * The amount of entries to be displayed per page.
     */
    @Getter
    @Builder.Default
    private int pageSize = 15;

//    /**
//     * The "subject" of the collection aka what's being collected.
//     * Dudes, items etc.
//     */
//    @Getter
//    private String collectionSubject;

    /**
     * The title of the ui.
     */
    @Getter
    private String uiTitle;

    private void fixPageInRange() {
        if (currentPage < 1) currentPage = 1;
        else if (currentPage > getTotalPages()) currentPage = getTotalPages();
    }

    /**
     * Gets the current page the UI is at.
     *
     * @return Current page.
     */
    public int getCurrentPage() {
        fixPageInRange();
        return currentPage;
    }

    /**
     * Increases the currnet page of the ui.
     */
    public void nextPage() {
        currentPage++;
    }

    /**
     * Decreases the currnet page of the ui.
     */
    public void previousPage() {
        currentPage--;
    }

    /**
     * Gets the total amount of pages in the collection.
     *
     * @return The amount of pages.
     */
    public int getTotalPages() {
        if (entries == null || entries.size() == 0) return 1;
        return (entries.size() - 1) / pageSize + 1;
    }

    /**
     * Gets a sublist of the entries that should be displayed at the current page.
     *
     * @return A sublist of all entries.
     */
    public List<String> getEntriesAtCurrentPage() {
        if (this.getEntries().size() == 0) return List.of();
        return this.getEntries().subList((this.getCurrentPage()-1)*this.getPageSize(),
                Math.min(this.getCurrentPage()*this.getPageSize(), this.getEntries().size()));
    }
}
