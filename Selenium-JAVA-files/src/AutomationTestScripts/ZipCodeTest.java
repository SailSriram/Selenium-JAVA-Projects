package AutomationTestScripts;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ZipCodeTest 
{
	public static void main (String [] args) 
	{
		try
		{
			//Create WebDriver instance
			System.setProperty("webdriver.chrome.driver", "/Users/sass2/Documents/chromedriver");
			WebDriver driver= new ChromeDriver();
			//Go to Supreme Golf Website
			//driver.get("https://staging.supremegolf.com/letmein");
			driver.get("https://supremegolf.com/");
			driver.manage().window().maximize();
			//Read in zip codes from text file and put them in ArrayList
			ArrayList<String>zipCodes= new ArrayList<>();
			Scanner readZipCodes= new Scanner(new File("ZipCodes.txt"));
			while (readZipCodes.hasNext())
			{
			String zipCode=readZipCodes.nextLine();
			zipCode=zipCode.substring(0, 5);
			zipCodes.add(zipCode);
			}
			//Create ArrayList that reads in the city names
			ArrayList<String> cityNames= new ArrayList<>();
			//Read in zipCodes from ArrayList
			for (int i=0; i < zipCodes.size(); i++)
			{
				WebElement textBox= driver.findElement(By.id("q_home"));
				textBox.sendKeys(zipCodes.get(i));
				textBox.submit();
				//Take out the comma in each header
				String cityName= driver.findElement(By.className("m-masthead__headline")).getText();
				int takeOutComma=cityName.indexOf(",");
				cityNames.add(cityName.substring(0, takeOutComma));
				Thread.sleep(500);
				driver.get("https://supremegolf.com/");
			}
			if (checkValidity(cityNames, zipCodes))
			{
				System.out.println("Zip Codes match with their respective city");
			}
			else
			{
				System.out.println("Error");
			}
			
			
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
	}
	public static boolean checkValidity(ArrayList<String> cityNames, ArrayList<String> zipNames) throws FileNotFoundException
	{
		//Iterate through the zipCodes
		Scanner zipCodes= new Scanner(new File("ZipCodes.txt"));
		int size=0;
		for (int i=0; i < cityNames.size(); i++)
		{
				String fullZipCode=zipCodes.nextLine();
				String cityName=cityNames.get(i).toLowerCase();
				if (fullZipCode.toLowerCase().indexOf(cityName) !=-1 && fullZipCode.indexOf(zipNames.get(i)) !=-1)
				{
					size++;
				}
				else
				{
					System.out.println(cityName + " " + zipNames.get(i));
				}
			
		}
		//Checks if all the zip codes match with their respective cities
		if (size==cityNames.size())
		{
			return true;
		}
		return false;
		
	
	}
	
}

