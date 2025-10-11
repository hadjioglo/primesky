package exploration;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Website Explorer for https://fdev.primesky.com/
 * This class explores the website to identify key functionalities and UI elements for testing
 */
public class WebsiteExplorer {
    
    private static final String TARGET_URL = "https://fdev.primesky.com/";
    private static final String SCREENSHOTS_DIR = "target/screenshots/";
    
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;
    private List<String> explorationLog;
    
    public static void main(String[] args) {
        WebsiteExplorer explorer = new WebsiteExplorer();
        explorer.exploreWebsite();
    }
    
    public void exploreWebsite() {
        setupBrowser();
        try {
            navigateToWebsite();
            exploreMainPage();
            identifyKeyFeatures();
            documentFindings();
        } catch (Exception e) {
            System.err.println("Error during exploration: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }
    
    private void setupBrowser() {
        System.out.println("=== Setting up browser for exploration ===");
        explorationLog = new ArrayList<>();
        
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setSlowMo(1000)); // Slow down for better observation
        
        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1920, 1080));
        
        page = context.newPage();
        
        // Enable request/response logging
        page.onRequest(request -> {
            System.out.println("Request: " + request.method() + " " + request.url());
        });
        
        page.onResponse(response -> {
            if (response.status() >= 400) {
                System.out.println("Error Response: " + response.status() + " " + response.url());
            }
        });
        
        logExploration("Browser setup completed");
    }
    
    private void navigateToWebsite() {
        System.out.println("=== Navigating to website ===");
        
        try {
            page.navigate(TARGET_URL);
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            
            // Take initial screenshot
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get(SCREENSHOTS_DIR + "01_initial_page.png"))
                    .setFullPage(true));
            
            String title = page.title();
            String url = page.url();
            
            logExploration("Successfully navigated to: " + url);
            logExploration("Page title: " + title);
            
            // Check if page loaded successfully
            if (page.locator("body").count() > 0) {
                logExploration("Page loaded successfully - body element found");
            }
            
        } catch (Exception e) {
            logExploration("Error navigating to website: " + e.getMessage());
            throw e;
        }
    }
    
    private void exploreMainPage() {
        System.out.println("=== Exploring main page elements ===");
        
        try {
            // Wait for page to fully load
            page.waitForTimeout(3000);
            
            // Explore header elements
            exploreHeader();
            
            // Explore navigation
            exploreNavigation();
            
            // Explore main content
            exploreMainContent();
            
            // Explore footer
            exploreFooter();
            
            // Take full page screenshot after exploration
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get(SCREENSHOTS_DIR + "02_page_explored.png"))
                    .setFullPage(true));
            
        } catch (Exception e) {
            logExploration("Error exploring main page: " + e.getMessage());
        }
    }
    
    private void exploreHeader() {
        logExploration("--- Exploring Header ---");
        
        // Look for common header elements
        String[] headerSelectors = {
            "header", "nav", ".header", "#header", 
            ".navbar", ".navigation", ".top-nav"
        };
        
        for (String selector : headerSelectors) {
            if (page.locator(selector).count() > 0) {
                logExploration("Found header element: " + selector);
                
                // Look for logo
                Locator logos = page.locator(selector + " img, " + selector + " .logo");
                if (logos.count() > 0) {
                    logExploration("  - Logo found: " + logos.first().getAttribute("src"));
                }
                
                // Look for navigation links
                Locator navLinks = page.locator(selector + " a");
                if (navLinks.count() > 0) {
                    logExploration("  - Navigation links found: " + navLinks.count());
                    for (int i = 0; i < Math.min(navLinks.count(), 10); i++) {
                        String linkText = navLinks.nth(i).textContent();
                        String href = navLinks.nth(i).getAttribute("href");
                        if (linkText != null && !linkText.trim().isEmpty()) {
                            logExploration("    * " + linkText.trim() + " -> " + href);
                        }
                    }
                }
                break;
            }
        }
    }
    
    private void exploreNavigation() {
        logExploration("--- Exploring Navigation ---");
        
        // Look for main navigation elements
        Locator navElements = page.locator("nav, .nav, .menu, .navigation");
        
        if (navElements.count() > 0) {
            logExploration("Found " + navElements.count() + " navigation elements");
            
            // Get all links in navigation
            Locator allLinks = page.locator("a");
            if (allLinks.count() > 0) {
                logExploration("Total links on page: " + allLinks.count());
                
                // Document unique navigation paths
                List<String> uniquePaths = new ArrayList<>();
                for (int i = 0; i < Math.min(allLinks.count(), 20); i++) {
                    String href = allLinks.nth(i).getAttribute("href");
                    String text = allLinks.nth(i).textContent();
                    
                    if (href != null && text != null && !text.trim().isEmpty()) {
                        String path = text.trim() + " -> " + href;
                        if (!uniquePaths.contains(path)) {
                            uniquePaths.add(path);
                            logExploration("  Navigation item: " + path);
                        }
                    }
                }
            }
        }
    }
    
    private void exploreMainContent() {
        logExploration("--- Exploring Main Content ---");
        
        // Look for main content areas
        String[] contentSelectors = {
            "main", ".main", "#main", ".content", "#content",
            ".container", ".wrapper", "section"
        };
        
        for (String selector : contentSelectors) {
            Locator contentArea = page.locator(selector);
            if (contentArea.count() > 0) {
                logExploration("Found content area: " + selector + " (count: " + contentArea.count() + ")");
                
                // Look for headings
                Locator headings = contentArea.locator("h1, h2, h3, h4, h5, h6");
                if (headings.count() > 0) {
                    logExploration("  Headings found: " + headings.count());
                    for (int i = 0; i < Math.min(headings.count(), 5); i++) {
                        String headingText = headings.nth(i).textContent();
                        if (headingText != null && !headingText.trim().isEmpty()) {
                            logExploration("    - " + headingText.trim());
                        }
                    }
                }
                
                // Look for buttons
                Locator buttons = contentArea.locator("button, .btn, input[type='submit']");
                if (buttons.count() > 0) {
                    logExploration("  Buttons found: " + buttons.count());
                    for (int i = 0; i < Math.min(buttons.count(), 5); i++) {
                        String buttonText = buttons.nth(i).textContent();
                        if (buttonText != null && !buttonText.trim().isEmpty()) {
                            logExploration("    - Button: " + buttonText.trim());
                        }
                    }
                }
                
                // Look for forms
                Locator forms = contentArea.locator("form");
                if (forms.count() > 0) {
                    logExploration("  Forms found: " + forms.count());
                    exploreFormsInDetail(forms);
                }
                
                break; // Use first found content area
            }
        }
    }
    
    private void exploreFormsInDetail(Locator forms) {
        for (int i = 0; i < forms.count(); i++) {
            Locator form = forms.nth(i);
            logExploration("    Form " + (i + 1) + ":");
            
            // Look for form inputs
            Locator inputs = form.locator("input, textarea, select");
            if (inputs.count() > 0) {
                logExploration("      Inputs: " + inputs.count());
                for (int j = 0; j < inputs.count(); j++) {
                    String type = inputs.nth(j).getAttribute("type");
                    String name = inputs.nth(j).getAttribute("name");
                    String placeholder = inputs.nth(j).getAttribute("placeholder");
                    
                    logExploration("        - Type: " + type + ", Name: " + name + ", Placeholder: " + placeholder);
                }
            }
        }
    }
    
    private void exploreFooter() {
        logExploration("--- Exploring Footer ---");
        
        String[] footerSelectors = {"footer", ".footer", "#footer"};
        
        for (String selector : footerSelectors) {
            if (page.locator(selector).count() > 0) {
                logExploration("Found footer element: " + selector);
                
                // Look for footer links
                Locator footerLinks = page.locator(selector + " a");
                if (footerLinks.count() > 0) {
                    logExploration("  Footer links: " + footerLinks.count());
                }
                break;
            }
        }
    }
    
    private void identifyKeyFeatures() {
        System.out.println("=== Identifying Key Features ===");
        
        try {
            // Feature 1: Login/Authentication
            identifyAuthenticationFeatures();
            
            // Feature 2: Search functionality
            identifySearchFeatures();
            
            // Feature 3: User interaction elements
            identifyInteractiveElements();
            
            // Feature 4: Data display/tables
            identifyDataDisplayElements();
            
            // Feature 5: Navigation flows
            identifyNavigationFlows();
            
        } catch (Exception e) {
            logExploration("Error identifying key features: " + e.getMessage());
        }
    }
    
    private void identifyAuthenticationFeatures() {
        logExploration("--- Feature 1: Authentication ---");
        
        // Look for login elements
        String[] loginSelectors = {
            "input[type='email']", "input[type='password']", 
            ".login", "#login", "button:has-text('Login')",
            "a:has-text('Login')", "a:has-text('Sign In')"
        };
        
        boolean authFound = false;
        for (String selector : loginSelectors) {
            if (page.locator(selector).count() > 0) {
                logExploration("Authentication element found: " + selector);
                authFound = true;
            }
        }
        
        if (!authFound) {
            logExploration("No obvious authentication elements found");
        }
    }
    
    private void identifySearchFeatures() {
        logExploration("--- Feature 2: Search ---");
        
        String[] searchSelectors = {
            "input[type='search']", ".search", "#search",
            "input[placeholder*='search' i]", "button:has-text('Search')"
        };
        
        boolean searchFound = false;
        for (String selector : searchSelectors) {
            if (page.locator(selector).count() > 0) {
                logExploration("Search element found: " + selector);
                searchFound = true;
            }
        }
        
        if (!searchFound) {
            logExploration("No search functionality found");
        }
    }
    
    private void identifyInteractiveElements() {
        logExploration("--- Feature 3: Interactive Elements ---");
        
        // Count interactive elements
        int buttonCount = page.locator("button").count();
        int linkCount = page.locator("a").count();
        int inputCount = page.locator("input").count();
        int selectCount = page.locator("select").count();
        
        logExploration("Interactive elements summary:");
        logExploration("  Buttons: " + buttonCount);
        logExploration("  Links: " + linkCount);
        logExploration("  Inputs: " + inputCount);
        logExploration("  Selects: " + selectCount);
    }
    
    private void identifyDataDisplayElements() {
        logExploration("--- Feature 4: Data Display ---");
        
        int tableCount = page.locator("table").count();
        int listCount = page.locator("ul, ol").count();
        int cardCount = page.locator(".card, .panel").count();
        
        logExploration("Data display elements:");
        logExploration("  Tables: " + tableCount);
        logExploration("  Lists: " + listCount);
        logExploration("  Cards/Panels: " + cardCount);
    }
    
    private void identifyNavigationFlows() {
        logExploration("--- Feature 5: Navigation Flows ---");
        
        // Try to identify main navigation paths
        List<String> mainNavItems = new ArrayList<>();
        
        Locator navLinks = page.locator("nav a, .nav a, .menu a");
        for (int i = 0; i < Math.min(navLinks.count(), 10); i++) {
            String text = navLinks.nth(i).textContent();
            if (text != null && !text.trim().isEmpty()) {
                mainNavItems.add(text.trim());
            }
        }
        
        logExploration("Main navigation paths identified: " + mainNavItems.size());
        for (String navItem : mainNavItems) {
            logExploration("  - " + navItem);
        }
    }
    
    private void documentFindings() {
        System.out.println("\n=== EXPLORATION SUMMARY ===");
        
        for (String log : explorationLog) {
            System.out.println(log);
        }
        
        System.out.println("\n=== RECOMMENDATIONS FOR TESTING ===");
        System.out.println("Based on the exploration, consider creating tests for:");
        System.out.println("1. Page loading and initial state verification");
        System.out.println("2. Navigation functionality and menu interactions");
        System.out.println("3. Form validation and submission workflows");
        System.out.println("4. Authentication flows (if available)");
        System.out.println("5. Search functionality (if available)");
        System.out.println("6. Responsive design and cross-browser compatibility");
        System.out.println("7. Error handling and edge cases");
    }
    
    private void logExploration(String message) {
        explorationLog.add(message);
        System.out.println(message);
    }
    
    private void cleanup() {
        System.out.println("\n=== Cleaning up browser resources ===");
        
        if (context != null) {
            context.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
        
        System.out.println("Exploration completed successfully!");
    }
}
