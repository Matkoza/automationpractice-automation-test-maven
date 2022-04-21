import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Setup {
    private static WebDriver webDriver;
    private static WebDriverWait wait;

    public Setup(WebDriver webDriver, WebDriverWait wait) {
        Setup.webDriver = webDriver;
        Setup.wait = wait;

    }

    public void startApplication() {
        webDriver.get("http://automationpractice.com/index.php");
    }
    public void exitApplication(){
        webDriver.quit();
    }
}
