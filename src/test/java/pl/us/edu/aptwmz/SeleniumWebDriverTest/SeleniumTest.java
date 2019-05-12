package pl.us.edu.aptwmz.SeleniumWebDriverTest;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumTest {

	enum Element {
		IMIE("inputEmail3"),
		NAZWISKO("inputPassword3"),
		DATAUR("dataU"),
		RODZICE("rodzice"),
		ZASWIADCZENIE("lekarz"),
		WYSLIJ("btn"),
		WYNIK("returnSt");
		
		Element(String id) {
			this.id = id;
		}
		
		public String toString() {
			return this.id;
		}
		private String id;
	};
	
	private static final String URL = "https://lamp.ii.us.edu.pl/~mtdyd/zawody/";
	private WebDriver driver;
	
	protected String executeTestCase(String imie, String nazwisko, String dataU, Boolean rodzice, Boolean zaswiadczenie) {
		driver.findElement(By.id(Element.IMIE.toString())).sendKeys(imie);
		driver.findElement(By.id(Element.NAZWISKO.toString())).sendKeys(nazwisko);
		driver.findElement(By.id(Element.DATAUR.toString())).sendKeys(dataU);
		if(rodzice)	{
			driver.findElement(By.id(Element.RODZICE.toString())).click();
		}
		
		if(zaswiadczenie) {
			driver.findElement(By.id(Element.ZASWIADCZENIE.toString())).click();
		}
		
		driver.findElement(By.className(Element.WYSLIJ.toString())).click();

		driver.switchTo().alert().accept();
		driver.switchTo().alert().accept();
		
		return driver.findElement(By.id(Element.WYNIK.toString())).getText();
	}
	
	protected Boolean findString(String text, String pattern) {
		return text.contains(pattern);
	}
	
	@BeforeEach
	public void runDriver() {
		System.setProperty("webdriver.chrome.driver","D:\\chromedriver\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.get(URL);
	}
	
	@AfterEach
	public void driverClose() {
		if(driver != null) {
			driver.close();
		}
	}
	
	@Test
	public void rejestracjaPonizej10RokuZycia() {
		String result = executeTestCase("Jan", "Kowalski", "12-05-2012", true, true);
		Assert.assertTrue("Test zakonczony negatywnie", findString(result, "Brak kwalifikacji"));
	}
	
	@Test
	public void rejestracjaMiedzy10a12RokiemZycia() {
		String result = executeTestCase("Jan", "Kowalski", "12-05-2008", true, true);
		Assert.assertTrue("Test zakonczony negatywnie", findString(result, "Skrzat"));
	}
	
	@Test
	public void rejestracjaMiedzy12a18RokiemZycia() {
		String result = executeTestCase("Jan", "Kowalski", "12-05-2004", true, true);
		Assert.assertTrue("Test zakonczony negatywnie", findString(result, "Junior"));
	}
	
	@Test
	public void rejestracjaMiedzy18a65RokiemZycia() {
		String result = executeTestCase("Jan", "Kowalski", "12-05-1996", false, true);
		Assert.assertTrue("Test zakonczony negatywnie", findString(result, "Dorosly"));
	}
	
	@Test
	public void rejestracjaPowyzej65RokuZycia() {
		String result = executeTestCase("Jan", "Kowalski", "12-05-1940", false, true);
		Assert.assertTrue("Test zakonczony negatywnie", findString(result, "Senior"));
	}
	
	@Test
	public void rejestracjaSeniorBezZaswiadczeniaLekarskiego() {
		String result = executeTestCase("Jan", "Kowalski", "12-05-1940", false, false);
		Assert.assertTrue("Test zakonczony negatywnie", findString(result, "Brak kwalifikacji"));
	}
	
	@Test
	public void rejestracjaSkrzatyBezZgodyRodzicow() {
		String result = executeTestCase("Jan", "Kowalski", "12-05-2012", false, true);
		Assert.assertTrue("Test zakonczony negatywnie", findString(result, "Brak kwalifikacji"));
	}
	
	@Test
	public void rejestracjaJuniorBezZgodyRodzicow() {
		String result = executeTestCase("Jan", "Kowalski", "12-05-2004", false, true);
		Assert.assertTrue("Test zakonczony negatywnie", findString(result, "Brak kwalifikacji"));
	}
	
	@Test
	public void rejestracjaSkrzatyBezZaswiadczeniaLekarskiego() {
		String result = executeTestCase("Jan", "Kowalski", "12-05-2008", true, false);
		Assert.assertTrue("Test zakonczony negatywnie", findString(result, "Brak kwalifikacji"));
	}
	
	@Test
	public void rejestracjaJuniorBezZaswiadczeniaLekarskiego() {
		String result = executeTestCase("Jan", "Kowalski", "12-05-2004", true, false);
		Assert.assertTrue("Test zakonczony negatywnie", findString(result, "Brak kwalifikacji"));
	}
	
}
