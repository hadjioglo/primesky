package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;

import java.util.Map;

/**
 * Page Object for the Contact Page
 * Encapsulates all contact form elements and actions
 */
public class ContactPage extends BasePage {
    
    // Contact form selectors
    private static final String CONTACT_FORM = "form, .contact-form, [data-testid='contact-form']";
    private static final String NAME_FIELD = "input[name*='name'], input[id*='name'], [data-testid='name']";
    private static final String EMAIL_FIELD = "input[type='email'], input[name*='email'], input[id*='email']";
    private static final String PHONE_FIELD = "input[name*='phone'], input[id*='phone'], input[type='tel']";
    private static final String MESSAGE_FIELD = "textarea, input[name*='message'], [data-testid='message']";
    private static final String SUBMIT_BUTTON = "button[type='submit'], input[type='submit'], .submit-btn";
    
    // Error message selectors
    private static final String ERROR_MESSAGES = ".error, .error-message, .invalid-feedback, [class*='error']";
    private static final String VALIDATION_ERROR = ".validation-error, .field-error";
    
    public ContactPage(Page page) {
        super(page);
    }
    
    /**
     * Verify that we are on the contact page
     */
    public boolean isOnContactPage() {
        String currentUrl = getCurrentUrl();
        boolean urlCheck = currentUrl.contains("contact");
        boolean formCheck = isElementVisible(CONTACT_FORM) || 
                           isElementVisible(NAME_FIELD) || 
                           isElementVisible(EMAIL_FIELD);
        
        System.out.println("Contact page verification - URL check: " + urlCheck + 
                          ", Form elements present: " + formCheck);
        
        return urlCheck || formCheck;
    }
    
    /**
     * Fill the contact form with provided data
     */
    public ContactPage fillContactForm(Map<String, String> formData) {
        System.out.println("Filling contact form with data: " + formData);
        
        if (formData.containsKey("Name") || formData.containsKey("name")) {
            String name = formData.getOrDefault("Name", formData.get("name"));
            fillNameField(name);
        }
        
        if (formData.containsKey("Email") || formData.containsKey("email")) {
            String email = formData.getOrDefault("Email", formData.get("email"));
            fillEmailField(email);
        }
        
        if (formData.containsKey("Phone") || formData.containsKey("phone")) {
            String phone = formData.getOrDefault("Phone", formData.get("phone"));
            fillPhoneField(phone);
        }
        
        if (formData.containsKey("Message") || formData.containsKey("message")) {
            String message = formData.getOrDefault("Message", formData.get("message"));
            fillMessageField(message);
        }
        
        return this;
    }
    
    /**
     * Fill the name field
     */
    public ContactPage fillNameField(String name) {
        System.out.println("Filling name field with: " + name);
        
        String[] nameSelectors = {
            "input[name*='name']",
            "input[id*='name']",
            "[data-testid='name']",
            "input[placeholder*='name']",
            "input[placeholder*='Name']"
        };
        
        boolean nameFilled = false;
        for (String selector : nameSelectors) {
            if (getElementCount(selector) > 0) {
                fillField(selector, name);
                nameFilled = true;
                break;
            }
        }
        
        if (!nameFilled) {
            System.out.println("Name field not found with standard selectors");
        }
        
        return this;
    }
    
    /**
     * Fill the email field
     */
    public ContactPage fillEmailField(String email) {
        System.out.println("Filling email field with: " + email);
        
        String[] emailSelectors = {
            "input[type='email']",
            "input[name*='email']",
            "input[id*='email']",
            "[data-testid='email']",
            "input[placeholder*='email']"
        };
        
        boolean emailFilled = false;
        for (String selector : emailSelectors) {
            if (getElementCount(selector) > 0) {
                fillField(selector, email);
                emailFilled = true;
                break;
            }
        }
        
        if (!emailFilled) {
            System.out.println("Email field not found with standard selectors");
        }
        
        return this;
    }
    
    /**
     * Fill the phone field
     */
    public ContactPage fillPhoneField(String phone) {
        System.out.println("Filling phone field with: " + phone);
        
        String[] phoneSelectors = {
            "input[name*='phone']",
            "input[id*='phone']",
            "input[type='tel']",
            "[data-testid='phone']",
            "input[placeholder*='phone']"
        };
        
        boolean phoneFilled = false;
        for (String selector : phoneSelectors) {
            if (getElementCount(selector) > 0) {
                fillField(selector, phone);
                phoneFilled = true;
                break;
            }
        }
        
        if (!phoneFilled) {
            System.out.println("Phone field not found with standard selectors");
        }
        
        return this;
    }
    
    /**
     * Fill the message field
     */
    public ContactPage fillMessageField(String message) {
        System.out.println("Filling message field with: " + message);
        
        String[] messageSelectors = {
            "textarea",
            "input[name*='message']",
            "[data-testid='message']",
            "textarea[placeholder*='message']",
            "textarea[name*='comment']"
        };
        
        boolean messageFilled = false;
        for (String selector : messageSelectors) {
            if (getElementCount(selector) > 0) {
                fillField(selector, message);
                messageFilled = true;
                break;
            }
        }
        
        if (!messageFilled) {
            System.out.println("Message field not found with standard selectors");
        }
        
        return this;
    }
    
    /**
     * Submit the contact form
     */
    public ContactPage submitForm() {
        System.out.println("Attempting to submit contact form...");
        
        String[] submitSelectors = {
            "button[type='submit']",
            "input[type='submit']",
            ".submit-btn",
            "button:has-text('Submit')",
            "button:has-text('Send')",
            "[data-testid='submit']"
        };
        
        boolean formSubmitted = false;
        for (String selector : submitSelectors) {
            if (getElementCount(selector) > 0) {
                clickElement(selector);
                formSubmitted = true;
                System.out.println("Clicked submit button with selector: " + selector);
                break;
            }
        }
        
        if (!formSubmitted) {
            // Fallback: try to submit form using Enter key
            System.out.println("Submit button not found, trying to submit form with Enter key");
            if (getElementCount(CONTACT_FORM) > 0) {
                page.locator(CONTACT_FORM).first().press("Enter");
            }
        }
        
        waitForPageLoad();
        return this;
    }
    
    /**
     * Check if validation errors are displayed
     */
    public boolean hasValidationErrors() {
        boolean hasErrors = isElementVisible(ERROR_MESSAGES) || 
                           isElementVisible(VALIDATION_ERROR) ||
                           page.content().toLowerCase().contains("error") ||
                           page.content().toLowerCase().contains("required");
        
        System.out.println("Validation errors present: " + hasErrors);
        return hasErrors;
    }
    
    /**
     * Get validation error messages
     */
    public String getValidationErrors() {
        String errors = "";
        
        if (getElementCount(ERROR_MESSAGES) > 0) {
            errors = getElementText(ERROR_MESSAGES);
        } else if (getElementCount(VALIDATION_ERROR) > 0) {
            errors = getElementText(VALIDATION_ERROR);
        }
        
        System.out.println("Validation error messages: " + errors);
        return errors;
    }
    
    /**
     * Check if form submission was successful
     */
    public boolean isFormSubmittedSuccessfully() {
        // Check for success indicators
        String content = page.content().toLowerCase();
        boolean hasSuccessMessage = content.contains("success") || 
                                   content.contains("thank you") ||
                                   content.contains("submitted") ||
                                   content.contains("sent");
        
        System.out.println("Form submission success indicators found: " + hasSuccessMessage);
        return hasSuccessMessage;
    }
}