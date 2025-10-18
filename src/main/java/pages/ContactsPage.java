package pages;

import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.ErrorHandlingUtil;

public class ContactsPage {
    private static final Logger logger = LogManager.getLogger(ContactsPage.class);
    private final Page page;
    private static final String CONTACTS_URL = "https://fdev.primesky.com/contacts";
    private static final String CONTACTS_TITLE = "Contacts";
    private static final String CONTACTS_SELECTOR = "[data-testid='contacts-page'], h1, .contacts-title";

    public ContactsPage(Page page) {
        this.page = page;
    }

    public void load() {
        ErrorHandlingUtil.runWithErrorHandling(() -> {
            page.navigate(CONTACTS_URL);
            page.waitForSelector(CONTACTS_SELECTOR);
            logger.info("Contacts page loaded");
        }, "logs/contacts_page_load_failure.png", logger, page, "Failed to load Contacts page");
    }

    public void assertLoaded() {
        ErrorHandlingUtil.runWithErrorHandling(() -> {
            String title = page.title();
            if (!title.equals(CONTACTS_TITLE)) {
                throw new AssertionError("Contacts page did not load as expected. Title: " + title);
            }
            logger.info("Contacts page title verified: " + title);
        }, "logs/contacts_page_assert_failure.png", logger, page, "Contacts page title assertion failed");
    }
}
