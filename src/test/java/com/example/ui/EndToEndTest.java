package com.example.ui;

import com.example.bilet.BiletApplication;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(classes = BiletApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;NON_KEYWORDS=USER",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
public class EndToEndTest { 

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private String baseUrl;
    @BeforeAll
    static void setupDriver() {
        WebDriverManager.chromedriver().setup();
    }
    @BeforeEach
    void startBrowser() {
        ChromeOptions options = new ChromeOptions();
        File braveBinary = new File("/Applications/Brave Browser.app/Contents/MacOS/Brave Browser");
        if (braveBinary.exists()) {
            options.setBinary(braveBinary);
        }

        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        
        baseUrl = "http://localhost:" + port;
    }
    @AfterEach
    void stopBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
    @Test
    void Full_Scenario_Register_And_Login() throws InterruptedException {

        driver.get(baseUrl + "/register");
        Thread.sleep(1000); 
        driver.findElement(By.name("username")).sendKeys("son_kullanici");
        driver.findElement(By.name("email")).sendKeys("son@test.com");
        driver.findElement(By.name("password")).sendKeys("1234");
        Thread.sleep(1000); 

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertTrue(
            driver.getCurrentUrl().contains("/login"),
            "Kayıt sonrası /login sayfasına yönlenmedi!"
        );

        driver.findElement(By.name("username")).sendKeys("son_kullanici");
        driver.findElement(By.name("password")).sendKeys("1234");
        Thread.sleep(1000); 

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String currentUrl = driver.getCurrentUrl();
        System.out.println(" Test Başarılı! Sayfa: " + currentUrl);
        Thread.sleep(5000); 

        assertTrue(
            currentUrl.contains("/event"),
            "Giriş yapılamadı! Beklenen: /event, Mevcut: " + currentUrl
        );
    }}