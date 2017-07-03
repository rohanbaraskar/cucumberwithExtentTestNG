package stepdefinition;
import org.testng.Assert;

import cucumber.api.java.en.Given;
import cucumberRunner.Runner;

public class Homepage extends Runner{
	@Given("^I am on google page$")
	public void googlePage() throws Throwable {

		String title = driver.getTitle();

		Assert.assertEquals(title, "Google");
	}

}
