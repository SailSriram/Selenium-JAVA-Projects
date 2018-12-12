package AutomationTestScripts;



	import org.openqa.selenium.By;
	import org.openqa.selenium.JavascriptExecutor;
	import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.WebElement;
	import org.openqa.selenium.chrome.ChromeDriver;
	import org.openqa.selenium.remote.CapabilityType;
	import org.openqa.selenium.remote.DesiredCapabilities;

	public class ChangePassword 
	{
		public static String browser;
		static WebDriver driver;
		public static void main (String [] args) throws InterruptedException
		{
			
			try
			{
				
				setBrowser();
				setBrowserConfig();
				runTest();
					
				
			}
			catch (Exception e)
			{
				System.out.println(e.getMessage());
			}
			
		}
		public static void setBrowser()
		{
			 browser="Chrome";		
		}
		
		// method to set web driver configurations
		@SuppressWarnings("deprecation")
		public static void setBrowserConfig()
		{
			if (browser.equals("Chrome"))
			{
				//DesiredCapabilities handlSSLErr = DesiredCapabilities.chrome ();    
				//handlSSLErr.setCapability (CapabilityType.ACCEPT_SSL_CERTS, true);
				System.setProperty("webdriver.chrome.driver", "/Users/sass2/Documents/chromedriver");
				 driver= new ChromeDriver();
			}
		}
		public static void runTest() throws InterruptedException
		{
			
			//Go to the site 
			driver.get("https://supremegolf.com");
			//Log in
			WebElement signIn= driver.findElement(By.linkText("SIGN IN"));
			signIn.click();
			WebElement usernameBox= driver.findElement(By.id("user_email"));
			usernameBox.sendKeys("Saileshsriram@gmail.com");
			Thread.sleep(1000);
			WebElement passwordBox= driver.findElement(By.id("user_password"));
			passwordBox.sendKeys("saisac");
			WebElement submit=driver.findElement(By.name("button"));
			submit.submit();
			Thread.sleep(1500);
			driver.findElement(By.xpath("/html/body/div[1]/div[3]/div/header/div[2]/div/div/div/div/div/span")).click();
			Thread.sleep(50);
			driver.findElement(By.linkText("PROFILE")).click();
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollBy(0,100)", "");
			driver.findElement(By.xpath("//*[@id=\"user-information\"]/span/span[2]")).click();
			//For old password
			Thread.sleep(500);
			WebElement oldPassword= driver.findElement(By.id("user_current_password"));
			oldPassword.sendKeys("saisac");
			Thread.sleep(2000);
			//For new password
			WebElement newPassword= driver.findElement(By.id("user_password"));
			newPassword.sendKeys("abc");
			Thread.sleep(2000);
			WebElement confirmNewPassword= driver.findElement(By.id("user_password_confirmation"));
			confirmNewPassword.sendKeys("abcde");
			Thread.sleep(2000);
			
			driver.findElement(By.id("edit-password-submit")).click();
			//Move up
			Thread.sleep(500);
			js.executeScript("window.scrollBy(0,100)", "");
			Thread.sleep(200);
			driver.findElement(By.xpath("/html/body/div[1]/div[3]/div/header/div[2]/div/div/div/div/div/span")).click();
			Thread.sleep(50);
			driver.findElement(By.linkText("SIGN OUT")).click();
			
		}
	}

