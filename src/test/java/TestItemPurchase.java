
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestItemPurchase {
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
    void testAddToCart(){

        WebElement WomenButton = webDriver.findElement(By.xpath("//*[@id=\"block_top_menu\"]/ul/li[1]/a[text()='Women']"));
        WomenButton.click();
        WebDriverWait waitOnWomanPageLoad= new WebDriverWait(webDriver, 20);
        waitOnWomanPageLoad.until(ExpectedConditions.urlToBe("http://automationpractice.com/index.php?id_category=3&controller=category"));

        Actions actions = new Actions(webDriver);
        WebElement firstItem = webDriver.findElement(By.xpath("/html/body/div/div[2]/div/div[3]/div[2]/ul/li[1]/div/div[1]/div/a[1]/img"));
        WebElement addFirstItemToCart = webDriver.findElement(By.cssSelector("#center_column > ul > li:nth-child(1) > div > div.right-block > div.button-container > a.button.ajax_add_to_cart_button.btn.btn-default"));
        actions.moveToElement(firstItem).moveToElement(addFirstItemToCart).click().build().perform();


        WebDriverWait waitOnAddToCart = new WebDriverWait(webDriver, 20);
        waitOnAddToCart.until(ExpectedConditions.visibilityOf(webDriver.findElement(By.className("icon-ok"))));
        WebElement proceedToCheckout = webDriver.findElement(By.xpath("/html/body/div/div[1]/header/div[3]/div/div/div[4]/div[1]/div[2]/div[4]/a"));
        proceedToCheckout.click();

        WebElement total = webDriver.findElement(By.id("total_product_price_1_1_0"));
        WebElement totalProducts = webDriver.findElement(By.id("total_product"));
        String totalText = total.getText();
        String totalProductsText = totalProducts.getText();
        assertEquals(totalText, totalProductsText);

        WebElement shippingCost = webDriver.findElement(By.xpath("//*[@id=\"total_shipping\"]"));
        WebElement totalCost = webDriver.findElement(By.xpath("//*[@id=\"total_price_without_tax\"]"));
        WebElement taxCost = webDriver.findElement(By.xpath("//*[@id=\"total_tax\"]"));

        String shippingCostStrippedString = shippingCost.getAttribute("innerHTML").replaceAll("\\$", "");
        String totalCostStrippedString = totalCost.getAttribute("innerHTML").replaceAll("\\$", "");
        String taxCostStrippedString = taxCost.getAttribute("innerHTML").replaceAll("\\$", "");
        String totalProductStrippedString = totalProducts.getAttribute("innerHTML").replaceAll("\\$", "");

        Double shippingCostDouble = Double.parseDouble(shippingCostStrippedString);
        Double totalProductsTextDouble = Double.parseDouble(totalProductStrippedString);
        Double taxCostDouble = Double.parseDouble(taxCostStrippedString);
        Double costWithShippingAndTax = totalProductsTextDouble + shippingCostDouble + taxCostDouble;
        Double totalCostDouble = Double.parseDouble(totalCostStrippedString);
        assertEquals(costWithShippingAndTax, totalCostDouble);

        WebElement proceedToCheckout2 = webDriver.findElement(By.xpath("/html/body/div/div[2]/div/div[3]/div/p[2]/a[1]"));
        proceedToCheckout2.click();

        WebElement email = webDriver.findElement(By.id("email"));
        WebElement password = webDriver.findElement(By.id("passwd"));
        WebElement submitButton = webDriver.findElement(By.id("SubmitLogin"));

        email.sendKeys("matej.mujezinovic@gmail.com");
        password.sendKeys("12345");
        submitButton.click();

        WebElement proceedToCheckout3 = webDriver.findElement(By.xpath("/html/body/div/div[2]/div/div[3]/div/form/p/button"));
        WebDriverWait waitForThirdCheckout = new WebDriverWait(webDriver, 20);
        waitForThirdCheckout.until(ExpectedConditions.elementToBeClickable(proceedToCheckout3));
        proceedToCheckout3.click();

        WebElement proceedToCheckout4 = webDriver.findElement(By.xpath("/html/body/div/div[2]/div/div[3]/div/div/form/p/button"));
        proceedToCheckout4.click();

        WebDriverWait waitForErrorMessage = new WebDriverWait(webDriver, 20);
        waitForErrorMessage.until(ExpectedConditions.visibilityOf(webDriver.findElement(By.xpath("//*[@id=\"order\"]/div[2]/div/div/div/div/p"))));
        String errorMessageStrippedString = webDriver.findElement(By.xpath("//*[@id=\"order\"]/div[2]/div/div/div/div/p")).getAttribute("innerHTML");
        assertEquals("You must agree to the terms of service before continuing.", errorMessageStrippedString);
    }
}
