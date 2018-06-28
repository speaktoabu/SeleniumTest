package com.newt.test;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Main {

	public static void main(String[] args) throws MalformedURLException, InterruptedException {
		
//		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		
		DesiredCapabilities capabilities = new DesiredCapabilities(BrowserType.CHROME, "67.0.3396.99", Platform.ANY);
		DesiredCapabilities firefoxCapabilities = DesiredCapabilities.firefox();
		firefoxCapabilities.setVersion("61.0");
		
//		capabilities.setCapability(CapabilityType.BROWSER_VERSION, 66);
		WebDriver driver = new RemoteWebDriver(new URL("http://10.0.16.9:4444/wd/hub"), capabilities);
		WebDriver driver1 = new RemoteWebDriver(new URL("http://10.0.16.9:4444/wd/hub"), firefoxCapabilities);
		driver.get("http://google.com");
		driver1.get("http://google.com");
		Thread.sleep(4000);
		driver.quit();
		System.out.println("I have finished my work");
	}

}
