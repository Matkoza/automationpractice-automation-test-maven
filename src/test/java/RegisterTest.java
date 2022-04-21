
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterTest {
    private static WebDriver webDriver;
    private static WebDriverWait wait;
    private static Setup setup;

    @BeforeAll
    static void SetupBeforeExecution(){
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        webDriver = new ChromeDriver();
        webDriver.manage().window().maximize();
        wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        setup = new Setup(webDriver, wait);
    }

    @BeforeEach
    void setUp() throws Exception{
        setup.startApplication();
    }

    @AfterAll
    static void tearDown(){
        setup.exitApplication();
    }

    @Test
    void testRegister() throws InterruptedException{
        WebElement SignInButton = webDriver.findElement(By.className("login"));
        SignInButton.click();

        WebElement emailField = webDriver.findElement(By.id("email_create"));
        WebDriverWait waitOnSignInPageLoad = new WebDriverWait(webDriver, 20);
        waitOnSignInPageLoad.until(ExpectedConditions.elementToBeClickable(emailField));


        emailField.sendKeys("matej.mujezinovic@gmail.com");
        WebElement registerButton = webDriver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[3]/div/div/div[1]/form/div/div[3]/button/span"));
        registerButton.click();

        WebDriverWait waitOnRegisterLoad = new WebDriverWait(webDriver, 20);
        waitOnRegisterLoad.until(ExpectedConditions.urlToBe("http://automationpractice.com/index.php?controller=authentication&back=my-account#account-creation"));

        WebElement mrRadio = webDriver.findElement(By.xpath("//*[@id=\"id_gender1\"]"));
        mrRadio.click();

        WebElement firstName = webDriver.findElement(By.id("customer_firstname"));
        WebElement lastName = webDriver.findElement(By.id("customer_lastname"));
        WebElement password = webDriver.findElement(By.id("passwd"));
        Select DOBDays = new Select(webDriver.findElement(By.id("days")));
        Select DOBMonths = new Select(webDriver.findElement(By.id("months")));
        Select DOBYears = new Select(webDriver.findElement(By.id("years")));


        firstName.sendKeys("Matej");
        lastName.sendKeys("Mujezinovic");
        password.sendKeys("12345");
        DOBDays.selectByValue("30");
        DOBMonths.selectByValue("7");
        DOBYears.selectByValue("2000");

        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("window.scrollBy(0,600)", "");

        WebElement address = webDriver.findElement(By.id("address1"));
        WebDriverWait waitForScroll = new WebDriverWait(webDriver, 20);
        waitForScroll.until(ExpectedConditions.elementToBeClickable(address));

        WebElement city = webDriver.findElement(By.id("city"));

        address.sendKeys("Zmaja od Bosne 82");
        city.sendKeys("Sarajevo");

        js.executeScript("window.scrollBy(0,600)", "");

        WebElement postalCode = webDriver.findElement(By.id("postcode"));
        Select state = new Select(webDriver.findElement(By.id("id_state")));
        Select country = new Select(webDriver.findElement(By.id("id_country")));

        country.selectByValue("21");
        state.selectByValue("3");
        postalCode.sendKeys("85018");

        js.executeScript("window.scrollBy(0,600)", "");

        WebElement mobilePhone = webDriver.findElement(By.id("phone_mobile"));
        WebElement aliasAddress = webDriver.findElement(By.id("alias"));
        aliasAddress.clear();

        mobilePhone.sendKeys("+38761272470");
        aliasAddress.sendKeys("matej.mujezinovic@gmail.com");

        WebElement submitAccountButton = webDriver.findElement(By.id("submitAccount"));
        submitAccountButton.click();

        String accountUrl = "http://automationpractice.com/index.php?controller=my-account";
        assertEquals(webDriver.getCurrentUrl(), accountUrl);
    }

    @Test
    void testLogout() throws InterruptedException{
        WebElement logout = webDriver.findElement(By.className("logout"));
        logout.click();
        String homeUrl = "http://automationpractice.com/index.php";
        assertEquals(webDriver.getCurrentUrl(), homeUrl);
    }
}
