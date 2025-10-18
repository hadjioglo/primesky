
package service;

import pages.HomePage;
import com.microsoft.playwright.*;
// ...existing code...

/**
 * Website Explorer for https://fdev.primesky.com/
 * This class explores the website to identify key functionalities and UI
 * elements for testing
 */
public class PlaywrightService {
    private static final BrowserManager browserManager = BrowserManager.getInstance();
    private static final Page page = browserManager.getPage();
    private static final HomePage homePage = new HomePage(page);

    /**
     * Loads the PrimeSky homepage using HomePage page object.
     * Handles navigation, waits, and error handling.
     */
    public static void loadPrimeSkyHomepage() {
        try {
            homePage.load();
            homePage.assertLoaded();
        } catch (Exception e) {
            // Optionally, capture screenshot on failure if implemented
            throw new RuntimeException("Failed to load PrimeSky homepage", e);
        }
    }

    // ...existing code...

    // ...existing code...
}
