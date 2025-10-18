package service;

import com.microsoft.playwright.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Singleton BrowserManager for Playwright browser lifecycle management.
 * Ensures only one instance of Playwright, Browser, and BrowserContext is created and reused.
 */
public class BrowserManager {
    private static final Logger logger = LogManager.getLogger(BrowserManager.class);
    private static BrowserManager instance;
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    private BrowserManager() {
        setupBrowser();
    }

    public static BrowserManager getInstance() {
        if (instance == null) {
            synchronized (BrowserManager.class) {
                if (instance == null) {
                    instance = new BrowserManager();
                }
            }
        }
        return instance;
    }

    private void setupBrowser() {
        ErrorHandlingUtil.runWithErrorHandling(() -> {
            logger.info("=== Setting up Playwright browser (singleton) ===");
            if (playwright == null) {
                playwright = Playwright.create();
            }
            if (browser == null) {
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setTimeout(60000));
            }
            if (context == null) {
                context = browser.newContext(new Browser.NewContextOptions()
                        .setViewportSize(1920, 1080));
            }
            if (page == null) {
                page = context.newPage();
            }
        }, "logs/browser_setup_failure.png", logger, page, "Error during browser setup");
    }

    public Playwright getPlaywright() {
        return playwright;
    }

    public Browser getBrowser() {
        return browser;
    }

    public BrowserContext getContext() {
        return context;
    }

    public Page getPage() {
        return page;
    }

    public void cleanup() {
        ErrorHandlingUtil.runWithErrorHandling(() -> {
            logger.info("=== Cleaning up browser resources ===");
            if (context != null) {
                context.close();
            }
            if (browser != null) {
                browser.close();
            }
            if (playwright != null) {
                playwright.close();
            }
            logger.info("Browser resources cleaned up!");
                }, "logs/browser_cleanup_failure.png", logger, (page != null && !page.isClosed() ? page : null), "Error during browser cleanup");
    }
}
