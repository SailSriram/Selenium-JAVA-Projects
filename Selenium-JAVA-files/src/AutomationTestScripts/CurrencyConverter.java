package AutomationTestScripts;


public class CurrencyConverter 
{
	public static double converttoDollars(String u, double p)
	{
		if (u.equals("£"))
		{
			return (1.32 * p);
		}
		else if (u.equals("€"))
		{
			return (1.17 * p);
		}
		return 0.0;
	}
}
