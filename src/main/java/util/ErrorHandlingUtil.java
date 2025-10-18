package util;

import com.microsoft.playwright.Page;
import org.apache.logging.log4j.Logger;

import java.nio.file.Paths;
import java.util.function.Supplier;

public class ErrorHandlingUtil {
    public static <T> T runWithErrorHandling(Supplier<T> action, String screenshotPath, Logger logger, Page page, String errorMessage) {
        try {
            return action.get();
        } catch (Exception e) {
            logger.error(errorMessage + ": {}", e.getMessage(), e);
            try {
                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)));
                logger.info("Screenshot captured at: {}", screenshotPath);
            } catch (Exception ex) {
                logger.error("Failed to capture screenshot: {}", ex.getMessage(), ex);
            }
            throw new RuntimeException(errorMessage + ": " + e.getMessage(), e);
        }
    }

    public static void runWithErrorHandling(Runnable action, String screenshotPath, Logger logger, Page page, String errorMessage) {
        runWithErrorHandling(() -> {
            action.run();
            return null;
        }, screenshotPath, logger, page, errorMessage);
    }
}

