package project_estate.dong;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Nonhyundong {
	
	private static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
	private static final String WEB_DRIVER_PATH = "C:\\chromedriver\\chromedriver.exe";
	
	public static void main(String[] args) throws IOException {
		
		File f = new File("C:\\KOPO\\git_tracking\\project\\estate\\oneData.csv");
		BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
	
		try {
			System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		ChromeOptions options = new ChromeOptions();
		WebDriver driver = new ChromeDriver(options);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
		
		driver.get("https://new.land.naver.com/complexes?ms=37.4996903,127.0381681,14&a=APT:ABYG:JGC&e=RETAIL");
		
		try {
			clickSelectionCity(wait);
			
			selectSeoul(wait);
			
			selectGu(wait, 1);	// 1: 강남구
			
			for (int i = 0; i < 14; i++) {
				selectDong(wait, i);
				
				for (int j = 0; j < 14)
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		bw.flush();
		bw.close();
	}
	
	
	
	private static void selectDong(WebDriverWait wait, int j) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div/div[1]/div/div/div/div[2]/ul/li[" + j + "]/label"))).click();
		wait500ms();
	}
	
	public static void selectGu(WebDriverWait wait, int guIndex) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div/div[1]/div/div/div/div[2]/ul/li[" + guIndex + "]/label"))).click();
		wait500ms();
	}
	
	private static void selectSeoul(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div/div[1]/div/div/div/div[2]/ul/li[1]/label"))).click();
		wait500ms();
	}
	
	private static void clickSelectionCity(WebDriverWait wait) {
		//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div/div[1]/div/div/a/span[1]"))).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"region_filter\"]/div/a/span[1]"))).click();
		wait500ms();
	}
	
	public static void wait500ms() {
		try {
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
	
	
