package stepdefinition;

import org.openqa.selenium.By;
import org.testng.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumberRunner.Runner;

public class SearchText extends Runner{
	@When("^I type \"(.*?)\"$")
	public void searchText(String text) throws Throwable {

		driver.findElement(By.cssSelector("input[name='q']")).sendKeys(text);

	}
}
