package cucumberRunner;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;



import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

import com.cucumber.listener.ExtentCucumberFormatter;
import com.cucumber.listener.ExtentProperties;
import com.cucumber.listener.Reporter;
import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.NetworkMode;



@CucumberOptions(
		strict = true,
		monochrome = true, 
		features = "src/test/resources/features",
		glue = "stepdefinition",
		//plugin = {"pretty", "html:target/cucumber-html-report" },
		plugin = {"com.cucumber.listener.ExtentCucumberFormatter:output/report.html"},
		tags={"@Regression,@JunitScenario,@TestngScenario"})

public class Runner extends AbstractTestNGCucumberTests{
	public static Properties config = null;
	public static WebDriver driver = null;
	
	ExtentReports extent;
	ExtentTest logger;

	public void LoadConfigProperty() throws IOException {
		config = new Properties();
		FileInputStream ip = new FileInputStream(
				System.getProperty("user.dir") + "//src//test//resources//config//config.properties");
		config.load(ip);
	}

	public void openBrowser() throws Exception {
		LoadConfigProperty();
		if (config.getProperty("browserType").equals("Firefox")) {
			System.setProperty("webdriver.firefox.marionette","C:\\geckodriver.exe");
	    	driver = new FirefoxDriver();
			//driver = new FirefoxDriver();
			 //driver = new HtmlUnitDriver();
		} else if (config.getProperty("browserType").equals("Chrome")) {
			//System.setProperty("webdriver.chrome.driver",
				//	System.getProperty("user.dir") + "//src//test//resources//driver/chromedriver");
			System.setProperty("webdriver.chrome.driver","C:\\chromedriver.exe");
			driver = new ChromeDriver();
		}
	}

	public void maximizeWindow() {
		driver.manage().window().maximize();
	}

	public void implicitWait(int time) {
		driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
	}

	public void pageLoad(int time) {
		driver.manage().timeouts().pageLoadTimeout(time, TimeUnit.SECONDS);
	}

	public void deleteAllCookies() {
		driver.manage().deleteAllCookies();
	}

	public void setEnv() throws Exception {
		LoadConfigProperty();
		String baseUrl = config.getProperty("siteUrl");
		driver.get(baseUrl);
	}

	public static String currentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String cal1 = dateFormat.format(cal.getTime());
		return cal1;
	}
	
	public static void takescreengrab(ITestResult arg0, String testname) throws IOException{
		File imageFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String failureImageFileName = arg0.getMethod().getMethodName()
				+ new SimpleDateFormat("MM-dd-yyyy_HH-ss").format(new GregorianCalendar().getTime()) + ".png";
		File failureImageFile = new File(System.getProperty("user.dir") + "//screenshots//" + failureImageFileName);
		FileUtils.copyFile(imageFile, failureImageFile);
		
	}

	@BeforeSuite(alwaysRun = true)
	public void setUp() throws Exception {
		openBrowser();
		maximizeWindow();
		implicitWait(30);
		deleteAllCookies();
		setEnv();
		
		//for cucumber
		ExtentProperties extentProperties = ExtentProperties.INSTANCE;
	    extentProperties.setReportPath("output/myreport.html");
		
		//normal extent report for testng
		//extent report data
		//False: existing data will remain, new tests will be appended to the existing report. If the the supplied path does not exist, a new file will be created.
				//extent = new ExtentReports (System.getProperty("user.dir") +"/test-output/STMExtentReport.html", true);
				//extent.addSystemInfo("Environment","Environment Name")
				//extent
		              //  .addSystemInfo("Host Name", "SoftwareTestingMaterial")
		             //   .addSystemInfo("Environment", "Automation Testing")
		            //    .addSystemInfo("User Name", "Rajkumar SM");
		                //loading the external xml file (i.e., extent-config.xml) which was placed under the base directory
		                //You could find the xml file below. Create xml file in your project and copy past the code mentioned below
		             //   extent.loadConfig(new File(System.getProperty("user.dir")+"\\extent-config.xml"));
		
	}

	@AfterClass(alwaysRun = true)
	public void takeScreenshot() throws IOException {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir") + "//screenshots/screenshot.png"));

	}

	@AfterMethod(alwaysRun = true)
	public void tearDownr(ITestResult result) throws IOException {
		if (result.isSuccess()) {
			File imageFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			String failureImageFileName = result.getMethod().getMethodName()
					+ new SimpleDateFormat("MM-dd-yyyy_HH-ss").format(new GregorianCalendar().getTime()) + ".png";
			File failureImageFile = new File(System.getProperty("user.dir") + "//screenshots//" + failureImageFileName);
			FileUtils.copyFile(imageFile, failureImageFile);
		}

	}

	@AfterSuite(alwaysRun = true)
	public void quit() throws IOException, InterruptedException {
		driver.quit();
		
		
		// extent report
		Reporter.loadXMLConfig(new File("src/test/resources/extent-config.xml"));
        Reporter.setSystemInfo("user", System.getProperty("user.name"));
        Reporter.setSystemInfo("os", "Mac OSX");
        Reporter.setTestRunnerOutput("Sample test runner output message");
		// writing everything to document
				//flush() - to write or update test information to your report. 
		               // extent.flush();
		                //Call close() at the very end of your session to clear all resources. 
		                //If any of your test ended abruptly causing any side-affects (not all logs sent to ExtentReports, information missing), this method will ensure that the test is still appended to the report with a warning message.
		                //You should call close() only once, at the very end (in @AfterSuite for example) as it closes the underlying stream. 
		                //Once this method is called, calling any Extent method will throw an error.
		                //close() - To close all the operation
		               // extent.close();
		
	}

	
}
