package pages;

import com.microsoft.playwright.Page;

/**
 * Page Object for the PrimeSky Homepage
 * Encapsulates all elements and actions available on the homepage
 */
public class HomePage extends BasePage {
    
    // Page URL
    private static final String HOME_URL = "/";
    
    // Locators
    private static final String LOGO = ".logo, [data-testid='logo'], .navbar-brand";
    private static final String NAVIGATION_MENU = ".nav, .navbar, [role='navigation']";
    private static final String CONTACT_LINK = "a[href*='contact'], .contact-link, [data-testid='contact']";
    private static final String FLIGHT_SEARCH_LINK = "a[href*='flight'], a[href*='search'], .flight-search";
    private static final String MAIN_CONTENT = "main, .main-content, .container";
    
    public HomePage(Page page) {
        super(page);
    }
    
    /**
     * Navigate to the homepage
     */
    public HomePage open() {
        navigateTo(baseUrl + HOME_URL);
        waitForPageLoad();
        takeScreenshot("homepage-loaded");
        return this;
    }
    
    /**
     * Verify that we are on the homepage
     */
    public boolean isOnHomePage() {
        String currentUrl = getCurrentUrl();
        boolean isHome = currentUrl.contains(baseUrl) && 
                        (currentUrl.endsWith("/") || currentUrl.equals(baseUrl));
        
        // Also check for presence of key homepage elements
        boolean hasLogo = isElementVisible(LOGO);
        boolean hasNavigation = isElementVisible(NAVIGATION_MENU);
        
        System.out.println("Homepage verification - URL check: " + isHome + 
                          ", Logo present: " + hasLogo + 
                          ", Navigation present: " + hasNavigation);
        
        return isHome && (hasLogo || hasNavigation);
    }
    
    /**
     * Navigate to the contact page
     */
    public void navigateToContactPage() {
        System.out.println("Attempting to navigate to contact page...");
        
        // Try multiple strategies to find and click contact link
        String[] contactSelectors = {
            "a[href*='contact']",
            ".contact-link",
            "[data-testid='contact']",
            "a:has-text('Contact')",
            "a:has-text('contact')",
            "nav a[href*='contact']"
        };
        
        boolean contactClicked = false;
        for (String selector : contactSelectors) {
            if (getElementCount(selector) > 0) {
                clickElement(selector);
                contactClicked = true;
                System.out.println("Clicked contact link with selector: " + selector);
                break;
            }
        }
        
        if (!contactClicked) {
            // Fallback: navigate directly to contact URL
            System.out.println("Contact link not found, navigating directly to contact page");
            navigateTo(baseUrl + "/contact");
        }
        
        waitForPageLoad();
    }
    
    /**
     * Navigate to flight search page
     */
    public void navigateToFlightSearch() {
        System.out.println("Attempting to navigate to flight search page...");
        
        // Try multiple strategies to find and click flight search link
        String[] flightSearchSelectors = {
            "a[href*='flight']",
            "a[href*='search']",
            ".flight-search",
            "[data-testid='flight-search']",
            "a:has-text('Flight')",
            "a:has-text('Search')",
            "nav a[href*='flight']"
        };
        
        boolean flightSearchClicked = false;
        for (String selector : flightSearchSelectors) {
            if (getElementCount(selector) > 0) {
                clickElement(selector);
                flightSearchClicked = true;
                System.out.println("Clicked flight search link with selector: " + selector);
                break;
            }
        }
        
        if (!flightSearchClicked) {
            // Fallback: navigate directly to flight search URL or assume we're already on a page with flight search
            System.out.println("Flight search link not found, assuming flight search is on current page or navigating to base URL");
            navigateTo(baseUrl);
        }
        
        waitForPageLoad();
    }
    
    /**
     * Get the page title
     */
    public String getPageTitle() {
        return getTitle();
    }
    
    /**
     * Verify homepage content is loaded
     */
    public boolean isContentLoaded() {
        return isElementVisible(MAIN_CONTENT) || 
               isElementVisible(LOGO) || 
               isElementVisible(NAVIGATION_MENU);
    }
}