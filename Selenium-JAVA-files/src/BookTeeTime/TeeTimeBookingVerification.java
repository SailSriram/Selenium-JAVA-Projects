package BookTeeTime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
public class TeeTimeBookingVerification 
{
	private static HashMap<String, String> getUIIdentifiers;
	private static ConfigData config;
	private static WebDriver driver;
	private static PrintStream myConsole;
	private static int teeTimes;
	private static int blueTeeTimes;
	private static int orangeTeeTimes;
	static boolean isLessThanFiveTeeTimes;
	static Scanner cityReader;
	static int increment;
	public static final String bracket="]";
	public static void main (String [] args) throws InterruptedException, FileNotFoundException
	{
			 //Initalizes config and UI Properties
			initializeConfigAndUI();
			//Initalizes file reader
			initalizeScanner();
			//Sets config properties
			setConfigProperties();
			//Sets UI Identifiers for webdriver
			setUIIdentifiers();
			//Sets up webdriver
			setDriverConfig();
			//Sets up file writer
			setConsoleConfig();
			//Goes to supremegolf website
			goToWebsite();
			//Removes popup
			removePopup();
			//Tests login
			testLogin();
			//Removes popup again
			removePopup();
			//Tests booking process
			while (cityReader.hasNext())
			{
				String city=cityReader.nextLine();
				getUIIdentifiers.put("cityName", city);
				gotoCityResultsPage(getUIIdentifiers.get("cityName"));
				selectCourse();
				findTeeTimes();
				if (isLessThanFiveTeeTimes==true)
				{
					goToWebsite();
				removePopup();
				}
				else
				{
					runTest();
					goToWebsite();
					removePopup();
				}
				
				
				
				
			}
			cityReader.close();		
    }
	public static void initializeConfigAndUI()
	{
		config= new ConfigData();
		getUIIdentifiers= new HashMap<>();
	}
	public static void initalizeScanner() throws FileNotFoundException
	{
		cityReader= new Scanner(new File("Markets.txt"));
	}
	public static void setConfigProperties() throws FileNotFoundException, InterruptedException
	{
		//Set config properties
		config.setBrowser("Chrome");
		config.setBrowserDriverName("webdriver.chrome.driver");
		config.setBrowserDriverValue("/Users/sass2/Documents/chromedriver");
		config.setWebsiteURL("https://supremegolf.com");
		config.setUser_email("Saileshsriram@gmail.com");
		config.setUser_pwd("saisac");
		config.setUser_email_UI_Key("user_email");
		config.setUser_email_pwd_UI_Key("user_password");
		config.setOutputFileLocation("/Users/sass2/eclipse-workspace/Selenium/report.txt");
			
	}
	public static void setUIIdentifiers()
	{
				//Set UIIdentifiers
				getUIIdentifiers.put("city/course/zip search", "q_home");
				getUIIdentifiers.put("selectCourse", "//h3[@class='m-infobox__headline hidden-sm hidden-xs'][1]");
				//getUIIdentifiers.put("selectCourse", "//*[@id=\"course_8269\"]/div[3]/span[2]/h3[1]/a");
				///html/body/div[15]/div[2]/form/div[1]
				getUIIdentifiers.put("golfCourse", "course-name");
				getUIIdentifiers.put("bookInstantlyButton", "BOOK INSTANTLY");
				getUIIdentifiers.put("dropDownMenu", "qty");
				//getUIIdentifiers.put("cityName", "m-masthead__headline");
				getUIIdentifiers.put("date", "//h2[@class='m-bookit--details prepare']");
				getUIIdentifiers.put("tax", "//div[@class='detail-output']");
				getUIIdentifiers.put("teeTime", "//tr[@class='js-teetime-row'][");
				getUIIdentifiers.put("lowestTeeTime", "//tr[@class='js-teetime-row lowest-price']");
				getUIIdentifiers.put("totalTeeTimes", "js-num-courses");
				getUIIdentifiers.put("popUpDeals", "//html/body/div[13]/div[2]/div/div/div/span/div/div[3]/div/div/p");
				getUIIdentifiers.put("errorMessageClose", "/html/body/div[14]/div[2]/form/div[3]/input");
				getUIIdentifiers.put("errorMessageCloseSecondOption", "/html/body/div[15]/div[2]/form/div[3]/input");
				getUIIdentifiers.put("suddenDealPopup", "/html/body/div[15]/div[2]/div/div/div/span/div/div[7]");
				getUIIdentifiers.put("suddenDealPopupTwo", "/html/body/div[13]/div[2]/div/div/div/span/div/div[7]");
				getUIIdentifiers.put("scrollToElement", "arguments[0].scrollIntoView();");
				getUIIdentifiers.put("bookItButton", "//*[@id=\"tee_time_");
				getUIIdentifiers.put("price", "//div[@class='detail-output']");
				
	}
	public static void setDriverConfig() throws FileNotFoundException
	{
		System.setProperty(config.getBrowserDriverName(), config.getBrowserDriverValue());
		driver= new ChromeDriver();
		
	}
	public static void setConsoleConfig() throws FileNotFoundException
	{
		myConsole = new PrintStream(config.getOutputFileLocation());
		System.setOut(myConsole);
	}
	public static void goToWebsite() throws InterruptedException
	{
		driver.get(config.getWebsiteURL());
		driver.manage().window().maximize();
		Thread.sleep(2000);
	}
	private static void removePopup() throws InterruptedException
	{
		try
		{
			WebElement popUp=driver.findElement(By.className("modal-header"));
			popUp.click();
			Thread.sleep(1000);
		}
		catch (Exception e)
		{
			
			WebElement passwordBox= driver.findElement(By.id(config.getUser_email_pwd_UI_Key()));
			passwordBox.clear();
			config.setUser_pwd("saisac1");
			passwordBox.sendKeys(config.getUser_pwd());
			passwordBox.submit();
			Thread.sleep(2000);
			WebElement popUp=driver.findElement(By.className("modal-header"));
			popUp.click();
			Thread.sleep(1000);
		}
		
	}

	private static void testLogin() throws InterruptedException 
	{
		
			driver.findElement(By.linkText("SIGN IN")).click();
			WebElement usernameBox = driver.findElement(By.id(config.getUser_email_UI_Key()));
			usernameBox.sendKeys(config.getUser_email());
			Thread.sleep(200);
			WebElement passwordBox = driver.findElement(By.id(config.getUser_email_pwd_UI_Key()));
			passwordBox.sendKeys(config.getUser_pwd());
			Thread.sleep(200);
			WebElement submit = driver.findElement(By.name("button"));
			submit.submit();
			Thread.sleep(2000);
		
		
		

	}
	public static void gotoCityResultsPage(String city) throws InterruptedException, FileNotFoundException
	{
		
         //removePopUp();
			try
			{
				  Thread.sleep(500);
		           WebElement citySearch= driver.findElement(By.id(getUIIdentifiers.get("city/course/zip search")));
		          // WebElement dateSearch=driver.findElement(By.id("on_date_home"));
		          // dateSearch.sendKeys("8/29/18");
		           citySearch.sendKeys(city);
		           citySearch.submit();
		           Thread.sleep(2000);
			}
			catch (Exception e)
			{
				throw e;
			}
        
        
	}
	public static void selectCourse()
	{
		driver.findElement(By.xpath(getUIIdentifiers.get("selectCourse"))).click();;
	}
	public static void findTeeTimes()
	{
		blueTeeTimes=0;
		orangeTeeTimes=0;
        int j=1;
        while (j <= Integer.parseInt(driver.findElement(By.className(getUIIdentifiers.get("totalTeeTimes"))).getText().replace(" TEE TIMES", "")))
        {
            try
            {
                if (driver.findElement(By.xpath(getUIIdentifiers.get("teeTime") + j + bracket))!=null)
                {
                    blueTeeTimes++;
                }
            }
            catch (Exception e)
            {
            		
            		orangeTeeTimes++;	
             }
            j++;
        }
        
        System.out.println("Here are the total number of tee times for " + driver.findElement(By.className(getUIIdentifiers.get("golfCourse"))).getText() +  "<<< " + (blueTeeTimes +=orangeTeeTimes));
        if (blueTeeTimes >=5 && blueTeeTimes > orangeTeeTimes)
        {
        	isLessThanFiveTeeTimes=false;
        	teeTimes=blueTeeTimes;
        }
        else if (orangeTeeTimes >=5 && orangeTeeTimes > blueTeeTimes)
        {
        	isLessThanFiveTeeTimes=false;
        	teeTimes=orangeTeeTimes;
        }
        else if (blueTeeTimes >=5 && orangeTeeTimes >= 5 && blueTeeTimes > orangeTeeTimes)
        {
        	isLessThanFiveTeeTimes=false;
        	teeTimes=blueTeeTimes;
        }
        else if (orangeTeeTimes >= 5 && blueTeeTimes >= 5 && orangeTeeTimes > blueTeeTimes)
        {
        	isLessThanFiveTeeTimes=false;
        	teeTimes=orangeTeeTimes;
        }
        else
        {
        	isLessThanFiveTeeTimes=true;
        	myConsole.println("This course in " + getUIIdentifiers.get("cityName") + " has less than five tee times");
        	myConsole.println("_____________________________________________________________________");
        	return;
        }
        
	}
	
	public static void runTest() throws InterruptedException
	{
		
			
			JavascriptExecutor js = (JavascriptExecutor) driver;
			String golfCourse = driver.findElement(By.className(getUIIdentifiers.get("golfCourse"))).getText();
			Random r = new Random();
			int increment = 0;
			int randomTeeTime = 0;
			while (increment <= 5)
			{
				
				if (teeTimes==blueTeeTimes)
				{
					
					randomTeeTime=r.nextInt(teeTimes) + 1;
					Thread.sleep(1000);
					String otherInformation=driver.findElement(By.xpath(getUIIdentifiers.get("teeTime") + randomTeeTime + bracket)).getText();
					int percent=otherInformation.indexOf("%");
					otherInformation=otherInformation.substring(0, percent + 1);
					String time=otherInformation.substring(0, 8);
					String priceWithoutTax=otherInformation.substring(8, 14);
					String discount=otherInformation.substring(15, percent + 1);
					Thread.sleep(500);
					js.executeScript(getUIIdentifiers.get("scrollToElement"), driver.findElement(By.xpath(getUIIdentifiers.get("teeTime") + randomTeeTime + bracket)));
					Thread.sleep(2000);
					//Find information about that tee time
					//Gets id to identify tee time
					String id=driver.findElement(By.xpath(getUIIdentifiers.get("teeTime") + randomTeeTime + bracket)).getAttribute("id");
					int hash=id.indexOf("_");
					id=id.substring(hash + 1, id.length());
					hash=id.indexOf("_");
					id=id.substring(hash+1, id.length());
					Thread.sleep(1000);
					//Checks to see if booking confirmation modal is displayed
					try
					{
						try
						{
							
							WebElement bookItButton=driver.findElement(By.xpath(getUIIdentifiers.get("bookItButton") + id + "\"]/td[9]/a/span"));
							bookItButton.click();
							Thread.sleep(8000);
							WebElement confirmBooking=driver.findElement(By.linkText(getUIIdentifiers.get("bookInstantlyButton")));
							confirmBooking.click();
							Thread.sleep(8000);
							if (driver.findElement(By.id(getUIIdentifiers.get("dropDownMenu"))).isDisplayed())
							{
								
								myConsole.println("City: " + getUIIdentifiers.get("cityName"));
								myConsole.println("Course Name: " + golfCourse);
								Thread.sleep(200);
								myConsole.println("Date: " + driver.findElement(By.xpath(getUIIdentifiers.get("date"))).getText());
								myConsole.println("Time: " + time);
								myConsole.println("Price without tax:" + priceWithoutTax);
								myConsole.println("Discount:" + discount);
								Thread.sleep(200);
								myConsole.println("Tax: " + driver.findElement(By.xpath(getUIIdentifiers.get("tax"))).getText());
								myConsole.println("Total Price: $" + getTotalPrice(priceWithoutTax.replace(" ", ""), driver.findElement(By.xpath(getUIIdentifiers.get("price"))).getText().replace(" ", "")));
								String data_Provider=driver.findElement(By.xpath(getUIIdentifiers.get("bookItButton") + id + "\"]/td[8]")).getAttribute("data-provider").replace("_", "").toUpperCase();
								if (data_Provider.indexOf("18")>=0)
								{
									data_Provider.replace("_", "");
									data_Provider.toUpperCase();
									data_Provider=data_Provider.substring(0, 6);
								}
								else
								{
									data_Provider.replace("_", "");
									data_Provider.toUpperCase();
								}
								myConsole.println("Provider: " + data_Provider);
								myConsole.println("Result: success");
								myConsole.println("_____________________________________________________________________");
								//findTeeTimes();
								increment++;
							//Click exit button
							driver.findElement(By.className("close")).click();
							
							}
						}
						catch (Exception e)
						{
							
								driver.findElement(By.xpath(getUIIdentifiers.get("suddenDealPopup"))).click();
								break;
							
							
							
						}
					}
					catch(Exception e) 
					{ 
						try
						{
							myConsole.println("City: " + getUIIdentifiers.get("cityName"));
							myConsole.println("Course Name: " + golfCourse);
							myConsole.println("Date: " + new Date().toString().replace(" ","").substring(0, 8));
							myConsole.println("Time: " + time);
							myConsole.println("Price without tax:" + priceWithoutTax);
							myConsole.println("Discount:" + discount);
							//"//*[@id=\"tee_time_"
							String data_Provider=driver.findElement(By.xpath(getUIIdentifiers.get("bookItButton") + id + "\"]/td[8]")).getAttribute("data-provider").replace("_", "").toUpperCase();
							if (data_Provider.indexOf("18")>=0)
							{
								data_Provider.replace("_", "");
								data_Provider.toUpperCase();
								data_Provider=data_Provider.substring(0, 6);
							}
							else
							{
								data_Provider.replace("_", "");
								data_Provider.toUpperCase();
							}
							myConsole.println("Provider: " + data_Provider);
							String error="";
							try
							{
								error=driver.findElement(By.className("vex-dialog-message")).getText();
								myConsole.println("Result: failure <<<" + error);
							}
							catch (Exception bcd)
							{
								myConsole.println("Result: failure <<<" + driver.findElement(By.xpath("//div[@class='vex-dialog-message'][1]")).getText());
							}
							myConsole.println("_____________________________________________________________________");
							try
							{
								
								driver.findElement(By.xpath(getUIIdentifiers.get("errorMessageCloseSecondOption"))).click();
								Thread.sleep(2000);
								driver.findElement(By.xpath(getUIIdentifiers.get("errorMessageCloseSecondOption"))).click();
								Thread.sleep(5000);
									
								
								
							}
							catch (Exception abc)
							{
								
								driver.findElement(By.xpath(getUIIdentifiers.get("errorMessageClose"))).click();
							}

							Thread.sleep(1000);
							findTeeTimes();
							increment++;
						}
						catch (Exception ab)
						{
							
							
								driver.findElement(By.xpath(getUIIdentifiers.get("suddenDealPopup"))).click();
								break;
							
							
						}
						
					}
					
				
				}
				else if (teeTimes==orangeTeeTimes)
				{
					randomTeeTime=r.nextInt(teeTimes) + 1;
					Thread.sleep(500);
					String otherInformation=driver.findElement(By.xpath(getUIIdentifiers.get("lowestTeeTime") + randomTeeTime + bracket)).getText();
					int percent=otherInformation.indexOf("%");
					otherInformation=otherInformation.substring(0, percent + 1);
					String time=otherInformation.substring(0, 8);
					String priceWithoutTax=otherInformation.substring(8, 14);
					String discount=otherInformation.substring(15, percent + 1);
					Thread.sleep(500);
					js.executeScript(getUIIdentifiers.get("scrollToElement"), driver.findElement(By.xpath(getUIIdentifiers.get("lowestTeeTime") + randomTeeTime + "]")));
					Thread.sleep(2000);
					//Find information about that tee time
					//Gets id to identify tee time
					String id=driver.findElement(By.xpath(getUIIdentifiers.get("lowestTeeTime") + randomTeeTime + bracket)).getAttribute("id");
					int hash=id.indexOf("_");
					id=id.substring(hash + 1, id.length());
					hash=id.indexOf("_");
					id=id.substring(hash+1, id.length());
					Thread.sleep(1000);
					//Checks to see if booking confirmation modal is displayed
					try
					{
						try
						{
							WebElement bookItButton=driver.findElement(By.xpath(getUIIdentifiers.get("bookItButton") + id + "\"]/td[10]/a/span[2]"));
							bookItButton.click();
							Thread.sleep(8000);
							WebElement confirmBooking=driver.findElement(By.linkText(getUIIdentifiers.get("bookInstantlyButton")));
							confirmBooking.click();
							Thread.sleep(8000);
							if (driver.findElement(By.id(getUIIdentifiers.get("dropDownMenu"))).isDisplayed())
							{
								//js.executeScript("window.scrollBy(0, 325)", "");
								myConsole.println("City: " + getUIIdentifiers.get("cityName"));
								myConsole.println("Course Name: " + golfCourse);
								Thread.sleep(200);
								myConsole.println("Date: " + driver.findElement(By.xpath(getUIIdentifiers.get("date"))).getText());
								myConsole.println("Time: " + time);
								myConsole.println("Price without tax:" + priceWithoutTax);
								myConsole.println("Discount:" + discount);
								Thread.sleep(200);
								myConsole.println("Tax: " + driver.findElement(By.xpath(getUIIdentifiers.get("tax"))).getText());
								myConsole.println("Total Price: $" + getTotalPrice(priceWithoutTax.replace(" ", ""), driver.findElement(By.xpath(getUIIdentifiers.get("price"))).getText().replace(" ", "")));
								String data_Provider=driver.findElement(By.xpath(getUIIdentifiers.get("bookItButton") + id + "\"]/td[8]")).getAttribute("data-provider").replace("_", "").toUpperCase();
								if (data_Provider.indexOf("18")>=0)
								{
									data_Provider.replace("_", "");
									data_Provider.toUpperCase();
									data_Provider=data_Provider.substring(0, 6);
								}
								else
								{
									data_Provider.replace("_", "");
									data_Provider.toUpperCase();
								}
								myConsole.println("Provider: " + data_Provider);
								myConsole.println("Result: success");
								myConsole.println("_____________________________________________________________________");
								increment++;
								//Click exit button
								driver.findElement(By.className("close")).click();
							}
						}
						catch (Exception e)
						{
							
								driver.findElement(By.xpath(getUIIdentifiers.get("suddenDealPopup"))).click();
								break;
							
							
						}
					}
					catch(Exception e) 
					{ 
						try
						{
							myConsole.println("City: " + getUIIdentifiers.get("cityName"));
							myConsole.println("Course Name: " + golfCourse);
							myConsole.println("Date: " + new Date().toString().replace(" ","").substring(0, 8));
							myConsole.println("Time: " + time);
							myConsole.println("Price without tax:" + priceWithoutTax);
							myConsole.println("Discount:" + discount);
							String data_Provider=driver.findElement(By.xpath("//*[@id=\"tee_time_" + id + "\"]/td[8]")).getAttribute("data-provider").replace("_", "").toUpperCase();
							if (data_Provider.indexOf("18")>=0)
							{
								data_Provider.replace("_", "");
								data_Provider.toUpperCase();
								data_Provider=data_Provider.substring(0, 6);
							}
							else
							{
								data_Provider.replace("_", "");
								data_Provider.toUpperCase();
							}
							myConsole.println("Provider: " + data_Provider);
							try
							{
								myConsole.println("Result: failure <<<" + driver.findElement(By.className("vex-dialog-message")).getText());
							}
							catch (Exception bcd)
							{
								myConsole.println("Result: failure <<<" + driver.findElement(By.xpath("//div[@class='vex-dialog-message'][1]")).getText());
							}
							myConsole.println("_____________________________________________________________________");
							try
							{
								driver.findElement(By.xpath(getUIIdentifiers.get("errorMessageCloseSecondOption"))).click();
								Thread.sleep(2000);
								driver.findElement(By.xpath(getUIIdentifiers.get("errorMessageCloseSecondOption"))).click();
								Thread.sleep(5000);
								
							}
							catch (Exception abc)
							{
								driver.findElement(By.xpath(getUIIdentifiers.get("errorMessageClose"))).click();
							}

							Thread.sleep(1000);
							findTeeTimes();
							increment++;
						}
						catch (Exception ab)
						{
							
								driver.findElement(By.xpath(getUIIdentifiers.get("suddenDealPopup"))).click();
								break;
							
							
						}
						
					}
				}
				
			}
		
		
		
		
	}
	public static double getTotalPrice(String priceWithOutTax, String tax)
	{
	        priceWithOutTax=priceWithOutTax.replace("$", "");
	        tax=tax.replace("$", "");
	        double totalPrice=(Double.parseDouble(priceWithOutTax) + Double.parseDouble(tax));
	        return totalPrice;
	}


	
}


