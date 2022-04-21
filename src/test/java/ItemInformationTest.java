
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemInformationTest {
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
    void testItemPriceAndName() throws InterruptedException{
        WebElement WomenButton = webDriver.findElement(By.xpath("//*[@id=\"block_top_menu\"]/ul/li[1]/a[text()='Women']"));
        WomenButton.click();
        WebDriverWait waitOnWomanPageLoad= new WebDriverWait(webDriver, 10);
        waitOnWomanPageLoad.until(ExpectedConditions.urlToBe("http://automationpractice.com/index.php?id_category=3&controller=category"));

        List<WebElement> itemList = webDriver.findElements(By.xpath("/html/body/div[1]/div[2]/div/div[3]/div[2]/ul/li"));

        for (int i = 1; i <= itemList.size(); i++){
            String priceXpath = String.format("/html/body/div/div[2]/div/div[3]/div[2]/ul/li[%d]/div/div[1]/div/div[2]/span", i);
            String itemNameXpath = String.format("/html/body/div/div[2]/div/div[3]/div[2]/ul/li[%d]/div/div[2]/h5/a", i);
            String itemLinkXpath = String.format("/html/body/div/div[2]/div/div[3]/div[2]/ul/li[%d]/div/div[2]/h5/a", i);

            String price = webDriver.findElement(By.xpath(priceXpath)).getAttribute("textContent").replaceAll("\\s+", "");
            String itemName = webDriver.findElement(By.xpath(itemNameXpath)).getAttribute("innerText");
            System.out.println(itemName + " " + price);

            WebElement itemLink = webDriver.findElement(By.xpath(itemLinkXpath));
            WebDriverWait waitOnItemLink = new WebDriverWait(webDriver, 10);
            waitOnItemLink.until(ExpectedConditions.elementToBeClickable(itemLink));
            itemLink.click();

            String pageItemNameXpath = String.format("/html/body/div/div[2]/div/div[3]/div/div/div/div[3]/h1");
            String pagePriceXpath = String.format("/html/body/div/div[2]/div/div[3]/div/div/div/div[4]/form/div/div[1]/div[1]/p[1]/span");

            String pageItemName = webDriver.findElement(By.xpath(pageItemNameXpath)).getAttribute("innerText");
            String pagePrice = webDriver.findElement(By.xpath(pagePriceXpath)).getAttribute("textContent");
            assertEquals(itemName, pageItemName);
            assertEquals(price, pagePrice);

            WebElement backButton = webDriver.findElement(By.xpath("/html/body/div/div[2]/div/div[1]/a[2]"));
            backButton.click();
            WebDriverWait waitOnPageBack= new WebDriverWait(webDriver, 10);
            waitOnPageBack.until(ExpectedConditions.urlToBe("http://automationpractice.com/index.php?id_category=3&controller=category"));
        }
    }
}
