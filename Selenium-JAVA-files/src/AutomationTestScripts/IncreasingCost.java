package AutomationTestScripts;



import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class IncreasingCost 
{
	public static String browser;
	static WebDriver driver;
	static int numofPages;
	static int clicker;
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
	public static void setBrowserConfig()
	{
		if (browser.equals("Chrome"))
		{
			
			System.setProperty("webdriver.chrome.driver", "/Users/sass2/Documents/chromedriver");
			 driver= new ChromeDriver();
		}
	}
	
	//method to test search by city names
	public static void runTest() throws InterruptedException, FileNotFoundException
	{
		Scanner kbReader= new Scanner(System.in);
		//Go to SupremeGolf website
		driver.get("https://supremegolf.com");
		driver.manage().window().maximize();
		//Read from cityfile
		Scanner cityReader= new Scanner(new File("Markets.txt"));
		while (cityReader.hasNext())
		{
			//Read in city
			String city=cityReader.nextLine();
			//Check how many pages of golf courses the city has
			System.out.println("How many pages with golf courses when you search for " + city + "?");
			int pages=kbReader.nextInt();
			if (pages >= 8)
			{
				numofPages=8;
				clicker=9;
			}
			else
			{
				numofPages=(pages + 1);
				clicker=(pages + 2);
			}
			//Close popup
		//	WebElement popUp=driver.findElement(By.className("modal-header"));
		//	popUp.click();
			//Type in city
			WebElement typeCity= driver.findElement(By.id("q_home"));
			typeCity.sendKeys(city);
			typeCity.submit();
			Thread.sleep(100);
			//Click down on drop down menu
			WebElement dropDownMenu= driver.findElement(By.xpath("//*[@id=\"js-tee-times\"]/h4/span[4]"));
			dropDownMenu.click();
			WebElement increasingPrice= driver.findElement(By.xpath("//*[@id=\"js-tee-times\"]/h4/span[4]/div/ul/li[2]"));
			increasingPrice.click();
			Thread.sleep(5000);
			//Click on header
			WebElement header=driver.findElement(By.className("m-masthead__headline"));
			header.click();
			Thread.sleep(1000);
			//Look at the prices colored orange of each golf course and add them to ArrayList
			ArrayList <Double> prices= new ArrayList<>();
			JavascriptExecutor js= (JavascriptExecutor) driver;
			//Scroll down window to courses
			Thread.sleep(5000);
			js.executeScript("window.scrollBy(0,325)", "");
			//Thread.sleep(1500);
			//Set incremental variable
			int i=1; 
			//Create WebElements
			WebElement price=null;
			WebElement lastPage=null;
			String unit="";
			if (driver.findElement(By.xpath("(//span[@class='unit'])[1]")).getText().equals("$"))
			{
				unit="$";
			}
			else 
			{
				 unit=driver.findElement(By.xpath("(//span[@class='unit'])[1]")).getText();
			}
			int currentPage=1;
			while (currentPage <= pages)
			{
				//If current page is the final page, then break out of the loop
				if (currentPage==pages)
				{
					while (i < 3)
					{
						//If price is in dollars
						if (unit.equals("$"))
						{
							//Finds price and scrolls down
							String xyz="" + i;
							price= driver.findElement(By.xpath("(//div[@class='dollars'])[" + xyz + "]"));
							if (price.getCssValue("color").equals("rgba(255, 138, 0, 1)"))
							{
								prices.add(Double.parseDouble(price.getText()));
							}
							js.executeScript("window.scrollBy(0,150)", "");
							Thread.sleep(5);
							i+=2;
						}
						//If price is not in dollars, then convert and add it to arraylist
						else if (!unit.equals("$"))
						{
							String xyz="" + i;
							price= driver.findElement(By.xpath("(//div[@class='dollars'])[" + xyz + "]"));
							if (price.getCssValue("color").equals("rgba(255, 138, 0, 1)"))
							{
								double hello= CurrencyConverter.converttoDollars(unit, Double.parseDouble(price.getText()));
								prices.add(hello);
							}
							js.executeScript("window.scrollBy(0,150)", "");
							Thread.sleep(5);
							i+=2;
						}
						unit=driver.findElement(By.xpath("(//span[@class='unit'])[" + i + "]")).getText();
					}
					break;
				}
				//Else continue after getting all the prices from the page
				else
				{
					while (i < 19)
					{
						//Finds price and scrolls down 
						if (unit.equals("$"))
						{
							//Finds price and scrolls down
							String xyz="" + i;
							price= driver.findElement(By.xpath("(//div[@class='dollars'])[" + xyz + "]"));
							if (price.getCssValue("color").equals("rgba(255, 138, 0, 1)"))
							{
								prices.add(Double.parseDouble(price.getText()));
							}
							js.executeScript("window.scrollBy(0,150)", "");
							Thread.sleep(5);
							i+=2;
						}
						//Convert to dollars
						else if (!unit.equals("$"))
						{
							String xyz="" + i;
							price= driver.findElement(By.xpath("(//div[@class='dollars'])[" + xyz + "]"));
							if (price.getCssValue("color").equals("rgba(255, 138, 0, 1)"))
							{
								double hello= CurrencyConverter.converttoDollars(unit, Integer.parseInt(price.getText()));
								prices.add(hello);
							}
							js.executeScript("window.scrollBy(0,150)", "");
							Thread.sleep(5);
							i+=2;
							
						}
						unit=driver.findElement(By.xpath("(//span[@class='unit'])[" + i + "]")).getText();
					}
				}
				//Clicks on next page if current page is not final page and scrolls down
				i=1;
				currentPage++;
				Thread.sleep(500);
				driver.findElement(By.xpath("//*[@id=\"js-tee-times\"]/div/div/nav/span[" + "" + clicker + "]/a/span")).click();
				Thread.sleep(4000);
				js.executeScript("window.scrollBy(0,325)", "");
			}
			//Checks to see if prices are in increasing order
			if (checkIfArrayListIsIncreasing(prices))
			{
				System.out.println("Prices are in increasing order for golf courses");
			}
			else
			{
				System.out.println("error");
			}
			driver.get("https://supremegolf.com");
		}
		
		
		
	}
		//Pass in array to check if prices are in increasing order

			
	
	public static boolean checkIfArrayListIsIncreasing(ArrayList<Double> p)
	{
		//Check to see if all numbers are in order using size field
		int size=1;
		for (int i=0; i < p.size() - 1; i++)
		{
			if (p.get(i) <= p.get(i+1))
			{
				size++;
			}
		}
		
		if (size==p.size())
		{
			return true;
		}
		else
		{
			System.out.println(p);
			return false;
		}
		
	}
	public static boolean checkIfArrayListIsIncreasingwithDecimals(ArrayList<Double> p)
	{
		//Check to see if all numbers are in order using size field
		int size=1;
		for (int i=0; i < p.size() - 1; i++)
		{
			if (p.get(i) <= p.get(i+1))
			{
				size++;
			}
		}
		
		if (size==p.size())
		{
			return true;
		}
		else
		{
			System.out.println(p);
			return false;
		}
		
	}	
		
}