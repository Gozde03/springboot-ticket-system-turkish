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
public
class UserFlowUiTest {

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
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        baseUrl = "http://localhost:" + port;
    }
    @AfterEach
    void stopBrowser() {
    }

    @Test
    void RegisterUser_Success_UI() throws InterruptedException {
        go("/register");
        Thread.sleep(2000); 

        driver.findElement(By.name("username")).sendKeys("brave_user");
        driver.findElement(By.name("email")).sendKeys("brave@test.com");
        driver.findElement(By.name("password")).sendKeys("1234");
        Thread.sleep(1000); 

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Thread.sleep(5000); 

        boolean isRedirected = driver.getCurrentUrl().endsWith("/login");
        
        if (isRedirected) {
            System.out.println(" BAŞARILI: Login sayfasına yönlendirildi!");
        } else {
            System.out.println(" HATA: Yönlendirme başarısız. Şu anki URL: " + driver.getCurrentUrl());
        }
        assertTrue(isRedirected, "Login sayfasına gidilemedi!");
    }
    private void go(String path) {
        driver.get(baseUrl + path);
    }
}