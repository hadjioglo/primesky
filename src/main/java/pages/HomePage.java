package pages;

import com.microsoft.playwright.Page;

public class HomePage {
	private final Page page;

	public HomePage(Page page) {
		this.page = page;
	}

	public void load() {
		String baseUrl = "https://fdev.primesky.com/";
		page.navigate(baseUrl);
		page.waitForSelector("body", new Page.WaitForSelectorOptions().setTimeout(30000));
	}

	public void assertLoaded() {
		String title = page.title();
		if (!title.toLowerCase().contains("prime")) {
			throw new AssertionError("PrimeSky homepage did not load as expected. Title: " + title);
		}
	}
}
