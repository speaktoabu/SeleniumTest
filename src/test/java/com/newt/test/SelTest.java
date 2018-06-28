package com.newt.test;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

public class SelTest {
	
	@Test
	public void test() throws MalformedURLException
	{
		Capabilities capabilities = DesiredCapabilities.chrome();
		WebDriver driver = new RemoteWebDriver(new URL("http://10.0.16.9:4444/wd/hub"), capabilities);
		
		driver.get("http://google.com");
		driver.quit();
		Capabilities capabilitiesff = DesiredCapabilities.firefox();
		WebDriver driverff = new RemoteWebDriver(new URL("http://10.0.16.9:4444/wd/hub"), capabilitiesff);
		
		driverff.get("http://google.com");
		
		driverff.quit();
	}

}
