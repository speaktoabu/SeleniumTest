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
		String gridUrl = System.getProperty("GridUrl");

		Capabilities capabilities = DesiredCapabilities.chrome();
		WebDriver driver = new RemoteWebDriver(new URL(gridUrl+"/wd/hub"), capabilities);
		
		driver.get("http://google.com");
		driver.quit();
		Capabilities capabilitiesff = DesiredCapabilities.firefox();
		WebDriver driverff = new RemoteWebDriver(new URL(gridUrl+"/wd/hub"), capabilitiesff);
		
		driverff.get("http://google.com");
		
		driverff.quit();
	}

}
