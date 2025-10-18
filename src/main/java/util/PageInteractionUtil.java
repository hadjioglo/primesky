package util;

import com.microsoft.playwright.Page;
import org.apache.logging.log4j.Logger;

public class PageInteractionUtil {
    public enum ElementAction { FILL, CLICK }

    public static boolean interactWithElement(Page page, Logger logger, String selector, String value, ElementAction action, String screenshotName, String errorMessage) {
        return ErrorHandlingUtil.runWithErrorHandling(() -> {
            var element = page.querySelector(selector);
            if (element == null) return false;
            boolean isHidden = element.getAttribute("hidden") != null || !element.isVisible();
            switch (action) {
                case FILL:
                    if (isHidden) {
                        String jsScript = "arguments[0].value = '" + value + "'; arguments[0].dispatchEvent(new Event('change'));";
                        element.evaluate(jsScript);
                    } else {
                        element.fill(value);
                    }
                    break;
                case CLICK:
                    if (isHidden) {
                        String jsScript = "arguments[0].click(); arguments[0].dispatchEvent(new Event('change'));";
                        element.evaluate(jsScript);
                    } else {
                        element.click();
                    }
                    break;
            }
            logger.debug("Interacted with element using selector: {}", selector);
            return true;
        }, screenshotName, logger, page, errorMessage + ": " + selector);
    }
}
