import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.lsch0037.cps3230.ScreenScraper;
import com.lsch0037.cps3230.Pages.MarketAlertHome;
import com.lsch0037.cps3230.Pages.MarketAlertList;
import com.lsch0037.cps3230.Pages.MarketAlertLogin;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ScreenScraperSteps{

    private WebDriver driver;
    private ScreenScraper screenScraper = new ScreenScraper(driver);
    private MarketAlertHome marketAlertHome = new MarketAlertHome(driver);
    private MarketAlertLogin marketAlertLogin = new MarketAlertLogin(driver);
    private MarketAlertList marketAlertList = new MarketAlertList(driver);


    @Given("I am a user of marketalertum")
    public void iAmAUserOfMarketalertum(){
        screenScraper.run();
    }

    @When("I login using valid credentials")
    public void I_login_using_valid_credentials(String userId) {

        screenScraper.visitMarketAlert(driver);
        screenScraper.goToLogIn(driver, marketAlertHome);
        screenScraper.logIn(driver, marketAlertLogin, userId);
    }

    @Then("I should see my alertsFor registered users to gain access to MarketAlertUm")
    public void I_should_see_my_alertsFor_registered_users_to_gain_access_to_MarketAlertUm() {
        // Write code here that turns the phrase above into concrete actions
        List<WebElement> alerts = screenScraper.getAlerts(driver);
        assertFalse(alerts.isEmpty());
    }

}