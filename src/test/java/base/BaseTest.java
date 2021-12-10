package base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.Status;

import extentlisteners.ExtentListeners;
//import io.github.bonigarcia.wdm.WebDriverManager;
import utilities.DbManager;
import utilities.ExcelReader;

public class BaseTest {
	
	/*
	 * WebDriver
	 * TestNG
	 * Screenshot
	 * Log4j
	 * Properties
	 * Keywords
	 * Database
	 * JavaMail
	 * Extent
	 * Listeners
	 * Excel
	 * Waits
	 * 
	 */
	
	public static WebDriver driver;
	public static Logger log = Logger.getLogger(BaseTest.class.getName());
	public static Properties OR = new Properties();
	public static Properties Config = new Properties();
	public static FileInputStream fis;
    public static ExcelReader excel = new ExcelReader(".\\src\\test\\resources\\excel\\testdata.xlsx");
	public static WebDriverWait wait;
	static WebElement dropdown;
	
	
	public static void click(String key) {

		if (key.endsWith("_XPATH")) {

			driver.findElement(By.xpath(OR.getProperty(key))).click();
		} else if (key.endsWith("_ID")) {

			driver.findElement(By.id(OR.getProperty(key))).click();
		} else if (key.endsWith("_CSS")) {

			driver.findElement(By.cssSelector(OR.getProperty(key))).click();
		}

		log.info("Clicking on an Element: " + key);
		ExtentListeners.test.log(Status.INFO, "Clicking on an Element: " + key);
	}

	
	public static void select(String key,String value) {

		
		if (key.endsWith("_XPATH")) {

			dropdown = driver.findElement(By.xpath(OR.getProperty(key)));
		} else if (key.endsWith("_ID")) {

			dropdown = driver.findElement(By.id(OR.getProperty(key)));
		} else if (key.endsWith("_CSS")) {

			dropdown =driver.findElement(By.cssSelector(OR.getProperty(key)));
		}
		
		Select select = new Select(dropdown);
		select.selectByVisibleText(value);

		log.info("Selecting the value from dropdown " + key+"  selected value as : "+value);
		ExtentListeners.test.log(Status.INFO, "Selecting the value from dropdown " + key+"  selected value as : "+value);
	}
	
	
	
	
	
	
	public static boolean isElementPresent(String key) {

		try {
		if (key.endsWith("_XPATH")) {

			driver.findElement(By.xpath(OR.getProperty(key)));
		} else if (key.endsWith("_ID")) {

			driver.findElement(By.id(OR.getProperty(key)));
		} else if (key.endsWith("_CSS")) {

			driver.findElement(By.cssSelector(OR.getProperty(key)));
		}
		log.info("Finding an Element: " + key);
		ExtentListeners.test.log(Status.INFO, "Finding an Element: " + key);

		return true;
		}catch(Throwable t) {
			log.error("Error while finding an Element: " + key+"--Error log is :"+t.getMessage());
			ExtentListeners.test.log(Status.ERROR, "Error while finding an Element: " + key+"--Error log is :"+t.getMessage());

			return false;
		}

		

	}
	
	
	public static void type(String key, String value) {

		if (key.endsWith("_XPATH")) {

			driver.findElement(By.xpath(OR.getProperty(key))).sendKeys(value);
		} else if (key.endsWith("_ID")) {

			driver.findElement(By.id(OR.getProperty(key))).sendKeys(value);;
		} else if (key.endsWith("_CSS")) {

			driver.findElement(By.cssSelector(OR.getProperty(key))).sendKeys(value);
		}

		log.info("Typing in an Element: " + key+" entered the value as : "+value);
		ExtentListeners.test.log(Status.INFO, "Typing in an Element: " + key+" entered the value as : "+value);

	}
	
	
	
	@BeforeSuite
	public void setUp() {
		
		if(driver==null) {
			
			PropertyConfigurator.configure(".\\src\\test\\resources\\properties\\log4j.properties");
			
			try {
				fis = new FileInputStream(".\\src\\test\\resources\\properties\\OR.properties");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				OR.load(fis);
				log.info("OR Properties loaded !!!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			try {
				fis = new FileInputStream(".\\src\\test\\resources\\properties\\Config.properties");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Config.load(fis);
				log.info("Config Properties loaded !!!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			if(Config.getProperty("browser").equals("chrome")) {
				
				
				//WebDriverManager.chromedriver().setup();
				System.setProperty("webdriver.chrome.driver", "D:\\Santosh\\selenium\\chromedriver_win32\\chromedriver.exe");
				driver = new ChromeDriver();
				log.info("Chrome Browser Launched !!!");
			}else if(Config.getProperty("browser").equals("firefox")) {
				
				
				//WebDriverManager.firefoxdriver().setup();
				System.setProperty("webdriver.gecko.driver", "D:\\Santosh\\selenium\\Gecko driver\\geckodriver-v0.26.0-win64\\geckodriver.exe");
				driver = new FirefoxDriver();
				log.info("Firefox Browser Launched !!!");
			}
			
			
			
			driver.get(Config.getProperty("testsiteurl"));
			log.info("Navigating to the URL : "+Config.getProperty("testsiteurl"));
			DbManager.setMysqlDbConnection();
			log.info("Database connection established");
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Integer.parseInt(Config.getProperty("implicit.wait")), TimeUnit.SECONDS);
		
			wait = new WebDriverWait(driver,Integer.parseInt(Config.getProperty("explicit.wait")));
		
		}
		
		
		
	}
	
	@AfterSuite
	public void tearDown() {
		
		driver.quit();
		log.info("Test Execution completed !!!");
		
	}
}

