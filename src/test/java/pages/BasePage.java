package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

import java.nio.file.Paths;

/**
 * Base Page Object class that provides common functionality for all page objects.
 * This class encapsulates common Playwright operations and provides utilities
 * for element interaction, waiting, and error handling.
 */
public abstract class BasePage {
    protected final Page page;
    protected final String baseUrl = "https://fdev.primesky.com";
    
    public BasePage(Page page) {
        this.page = page;
    }
    
    /**
     * Navigate to a specific URL
     */
    public void navigateTo(String url) {
        page.navigate(url);
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }
    
    /**
     * Get the current page title
     */
    public String getTitle() {
        return page.title();
    }
    
    /**
     * Get the current page URL
     */
    public String getCurrentUrl() {
        return page.url();
    }
    
    /**
     * Wait for an element to be visible
     */
    protected void waitForElementVisible(String selector) {
        page.locator(selector).waitFor();
    }
    
    /**
     * Fill a field with enhanced hidden element support
     */
    protected void fillField(String selector, String value) {
        if (value == null || value.isEmpty()) return;
        
        Locator element = page.locator(selector);
        
        // Check if element has hidden attribute or is not visible
        String hiddenAttr = element.getAttribute("hidden");
        boolean isHidden = hiddenAttr != null || !element.isVisible();
        
        if (isHidden) {
            // Use JavaScript for hidden elements
            String jsScript = "arguments[0].value = '" + value + "'; arguments[0].dispatchEvent(new Event('change'));";
            element.evaluate(jsScript);
            System.out.println("Set hidden field using JavaScript: " + selector);
        } else {
            // Handle visible elements normally
            element.clear();
            element.fill(value);
            System.out.println("Filled visible field: " + selector);
        }
    }
    
    /**
     * Click an element with enhanced hidden element support
     */
    protected void clickElement(String selector) {
        Locator element = page.locator(selector);
        
        // Check if element has hidden attribute or is not visible
        String hiddenAttr = element.getAttribute("hidden");
        boolean isHidden = hiddenAttr != null || !element.isVisible();
        
        if (isHidden) {
            // Use JavaScript for hidden elements
            String jsScript = "arguments[0].click(); arguments[0].dispatchEvent(new Event('click'));";
            element.evaluate(jsScript);
            System.out.println("Clicked hidden element using JavaScript: " + selector);
        } else {
            // Handle visible elements normally
            element.click();
            System.out.println("Clicked visible element: " + selector);
        }
    }
    
    /**
     * Get text from an element
     */
    protected String getElementText(String selector) {
        return page.locator(selector).textContent();
    }
    
    /**
     * Check if an element is visible
     */
    protected boolean isElementVisible(String selector) {
        return page.locator(selector).isVisible();
    }
    
    /**
     * Get the count of elements matching a selector
     */
    protected int getElementCount(String selector) {
        return page.locator(selector).count();
    }
    
    /**
     * Take a screenshot for debugging
     */
    protected void takeScreenshot(String fileName) {
        try {
            page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get("target/screenshots/" + fileName + ".png")));
            System.out.println("Screenshot saved: target/screenshots/" + fileName + ".png");
        } catch (Exception e) {
            System.out.println("Failed to take screenshot: " + e.getMessage());
        }
    }
    
    /**
     * Wait for page to load completely
     */
    protected void waitForPageLoad() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(1000); // Additional wait for dynamic content
    }
    
    /**
     * Find element using multiple selector strategies
     */
    protected Locator findElementWithFallback(String... selectors) {
        for (String selector : selectors) {
            if (page.locator(selector).count() > 0) {
                System.out.println("Found element with selector: " + selector);
                return page.locator(selector).first();
            }
        }
        System.out.println("No element found with any of the provided selectors");
        return null;
    }
}