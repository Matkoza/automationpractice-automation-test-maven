
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomePageTest{
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
    void testHomePage() throws InterruptedException{
        String homeURL = webDriver.getCurrentUrl();
        assertEquals(homeURL, "http://automationpractice.com/index.php");
    }
}
