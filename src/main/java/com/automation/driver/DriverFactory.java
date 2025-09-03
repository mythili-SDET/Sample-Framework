package com.automation.driver;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.safari.SafariDriver;

import java.io.File;

public class DriverFactory {

    public static WebDriver createInstance(BrowserType browser, ExecutionMode mode, OSType os, boolean headless) {
        switch (mode) {
            case LOCAL:
                return createLocalDriver(browser, os, headless);
            case CI:
                return createCiDriver(browser, headless);
            default:
                throw new IllegalArgumentException("Unsupported execution mode: " + mode);
        }
    }

    private static WebDriver createLocalDriver(BrowserType browser, OSType os, boolean headless) {
        String driverFolder = "src/test/resources/drivers/";
        switch (browser) {
            case CHROME:
                setDriverPath("webdriver.chrome.driver", driverFolder, os, "chromedriver");
                return new ChromeDriver(getChromeOptions(headless));
            case FIREFOX:
                setDriverPath("webdriver.gecko.driver", driverFolder, os, "geckodriver");
                return new FirefoxDriver(getFirefoxOptions(headless));
            case EDGE:
                setDriverPath("webdriver.edge.driver", driverFolder, os, "msedgedriver");
                return new EdgeDriver(getEdgeOptions(headless));
            case SAFARI:
                if (os != OSType.MAC) throw new RuntimeException("Safari only supported on MacOS");
                return new SafariDriver();
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    private static WebDriver createCiDriver(BrowserType browser, boolean headless) {
        switch (browser) {
            case CHROME:
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver(getChromeOptions(headless));
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver(getFirefoxOptions(headless));
            case EDGE:
                WebDriverManager.edgedriver().setup();
                return new EdgeDriver(getEdgeOptions(headless));
            case SAFARI:
                return new SafariDriver(); // CI Mac agent must have Safari
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    private static void setDriverPath(String property, String baseFolder, OSType os, String driverName) {
        String extension = (os == OSType.WINDOWS) ? ".exe" : "";
        String path = baseFolder + driverName + extension;
        File file = new File(path);
        if (!file.exists()) throw new RuntimeException("Driver not found at: " + file.getAbsolutePath());
        System.setProperty(property, file.getAbsolutePath());
    }

    // ------------------- OPTIONS -------------------

    private static ChromeOptions getChromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) options.addArguments("--headless=new");
        options.addArguments("--disable-notifications", "--disable-popup-blocking",
                "--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu", "--incognito", "--remote-allow-origins=*");
        return options;
    }

    private static FirefoxOptions getFirefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) options.addArguments("--headless");
        options.addPreference("dom.webnotifications.enabled", false);
        options.addPreference("media.volume_scale", "0.0");
        return options;
    }

    private static EdgeOptions getEdgeOptions(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        if (headless) options.addArguments("--headless=new");
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu", "--inprivate");
        return options;
    }
}

