package stepdefinition;

import org.openqa.selenium.By;
import org.testng.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumberRunner.Runner;


public class SearchButton extends Runner{
	@Then("^I click search button$")
	public void searchButton() throws Throwable {
		driver.findElement(By.name("btnG")).click();

	}

}
