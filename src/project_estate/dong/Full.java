package project_estate.dong;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Full  {
	final static int startGuIndex = 13;
	
	//private static final String filePath = "c:\\KOPO\\git_tarcking\\기본프로그래밍_java\\Pro\\Data.csv";
	private static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
	//private static final String WEB_DRIVER_PATH = "D:\\KOPO\\utility\\chromedriver_win32\\chromedriver.exe";
	private static final String WEB_DRIVER_PATH = "C:\\chromedriver\\chromedriver.exe";

	public static void main(String[] args) throws IOException {
		File f = new File("C:\\KOPO\\git_tracking\\기본프로그래밍_java\\Pro\\schooldistance\\data.csv");
		BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
	
		try {
			System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		ChromeOptions options = new ChromeOptions();
		WebDriver driver = new ChromeDriver(options);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
		
		driver.get("https://new.land.naver.com/complexes?ms=37.566427,126.977872,13&a=APT:ABYG:JGC&e=RETAIL");
		
		int[] target = {23};
		
		try {
			// 광역시 배너 클릭
			clickSelectionCity(wait);
			
			// 광역시 중 서울시 선택 -> 자동으로 구 선택을 넘어감
			selectSeoul(wait);
			
			// 총 구의 개수 확인
			int guSize = checkRegionSize(driver);
			
			// 구 개수 만큼 반복
			//for (int i = 9 ; i <= 9 ; i++) {
			for (int ii = 0 ; ii < target.length ; ii++) {
				int i = target[ii];
				// 서울인지 확인 다르면 처리
				// 추가 예정
				
				if (driver.findElement(By.xpath("//*[@id=\"region_filter\"]/div/div")).getAttribute("aria-hidden").equals("true")) {
					// 동 목록 열기
					openGuSelection(wait);
				}
//				if (i != 1) {
//					// 동 목록 열기
//					openGuSelection(wait);
//				}
				// 구 선택 -> 자동으로 동 선택으로 넘아감
				selectGu(wait, i);
				String guName = getGuName(driver);
				
				// 총 동의 개수 확인, 26
				int dongSize = checkRegionSize(driver);
				
				// 동 개수 만큼 반복
				//for (int j = 1 ; j <= dongSize ; j++) {
				for (int j = 1 ; j <= dongSize ; j++) {
					if(i==23 && j==1) {
						j = 86;
					}
					// 두번째 동부터
//					if(j != 1) {
//						// 지역 확인 후 바꼈으면 재설정
//						compareAndRearrangeGu(driver, wait, guName, i);
//					}
//					
//					// 동 선택 -> 자동으로 단지 선택으로 넘어감
//					// 단지 목록이 닫혀잇으면
					if (driver.findElement(By.xpath("//*[@id=\"region_filter\"]/div/div")).getAttribute("aria-hidden").equals("true")) {
						// 동 목록 열기
						openDongSelection(wait);
					}
//					if(j != 3) {
//						openDongSelection(wait);
//					}
					selectDong(wait, j);
					
					
					// 동이름 확인
					String dongName = getDongName(driver);
						
					// 총 단지의 개수 확인
					int complexSize = checkComplexSize(driver);
					
					if (complexSize == 0) {
						closeSelection(wait);
					}
					
					// 단지 개수만큼 반복
					//for(int k = 1 ; k <= complexSize ; k++) {
					for (int k = 1 ; k <= complexSize ; k++) {
						if(i==23 && j==86 && k==1) {
							k = 1;
						}
						// 단지 목록이 닫혀잇으면
						if (driver.findElement(By.xpath("//*[@id=\"region_filter\"]/div/div")).getAttribute("aria-hidden").equals("true")) {
							// 단지 목록 열기
							openCitySelection(wait);
						}
						System.out.println(complexSize + " / " + k);
						if(!(k == 1 || i==23 && j==86 && k==1)) {
							selectSeoul(wait);
							
							selectGu(wait, i);
						
							selectDong(wait, j);
						}
						
						// 단지 선택 - 자동으로 단지 정보로 펼쳐짐
						selectComplex(wait, k);
						
						// 아파트 단지명 수집
						String complexName = "";
						try {
							complexName = collectComplexName(wait);
						} catch(Exception e) {
							complexName = "정보 없음";
						}
						
						/* 면적 선택 확인 */
						
						// 전체 면적 선택하여 선택지 열기
						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[2]/button/span"))).click();
						
						// 전체 면적 선택 (75 ~85)
						int sizeIndex = 1;
						while (true) {
							try {
								int sizeOption = Integer.parseInt(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[2]/div/div/ul/li[" + sizeIndex + "]/label"))).getText());
								if ( 75 <= sizeOption && sizeOption <= 85) {
									wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[2]/div/div/ul/li[" + sizeIndex + "]/label"))).click();
								}
							} catch (Exception e) {
								break;
							}
						}
						// 전체 면적 선택지 닫기
						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[2]/div/button/i"))).click();
		
						
						/* 매매가 죄저가 확인 */
						
						// 전체거래방식 클릭하여 선택지 열기
						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='complexOverviewList']/div[2]/div[1]/div[2]/div/div[1]/button)"))).click();
						
						// 전체거래방식 매매로 선택 - 선택지목록은 계속 열려 있다.
						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[1]/div/div/ul/li[2]/label"))).click();
						
						// 가격순 정렬 버튼이 안 눌려있으면 클릭하여 낮은가격순으로 변경
						WebElement priceSort = driver.findElement(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[3]/a[3]"));
						if (priceSort.getAttribute("aria-pressed").equals("false")) {
							wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[3]/a[3]"))).click();
						}
						// 가격순이 높은 가격 순이면 클릭하여 낮은 가격순으로 변경
						if (priceSort.getAttribute("class").equals("sorting_type is-descending")) {
							wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[3]/a[3]"))).click();
						}
						
						int complexIndex = 1;
						Double minSaleSize = (double) 1000000000;
						int minOnePrice = 1000000000;
						while (true) {
							try {
								String oneSaleInfo = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexTitle\"]"))).getText();
								Double oneSaleSize = Double.parseDouble(oneSaleInfo.split(",")[0].split("/")[1].substring(0, -1));
								
								String oneSalePriceKor = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"articleListArea\"]/div[1]/div/a/div[2]/span[2]"))).getText();
								int oneSalePriceNum = Integer.parseInt(oneSalePriceKor.replace(",", "").replace("억", "").replace(" ", ""));
								
								if (75 <= oneSaleSize && oneSaleSize <= 85) {
									minSaleSize = oneSaleSize;
									minOnePrice = oneSalePriceNum;
									break;
								} else if (64 < oneSaleSize && oneSaleSize < minSaleSize) {
									minSaleSize = oneSaleSize;
									minOnePrice = oneSalePriceNum;
								}
								
								complexIndex++;
							} catch (Exception e) {
								break;
							}
						}
						
						//*[@id="articleListArea"]/div[1]/div/a/div[3]/p[1]/span
						//*[@id="articleListArea"]/div[2]/div/a/div[3]/p[1]/span
						
						
						
						
						
						
						
						
						
						bw.write(guName + "," + dongName + "," + complexName.replaceAll(",", "") + "," + priceRange.replaceAll(",", "") + ",");
						
						bw.write(distance.trim());
						bw.newLine();
						
						// 현재 단지 닫기
						closeComplexInformation(wait);
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		bw.flush();
		bw.close();
	}
	
//	private static int checkRegionSize(WebDriver driver) {
//		return driver.findElements(By.xpath("//*[@class=\"area_item\"]")).size();
//	}
	
	private static void closeSelection(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"region_filter\"]/div/a/span[3]"))).click();
		wait500ms();
		//wait500ms();
		//wait500ms();
	}

	private static String collectComplexScale(WebDriverWait wait) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"summaryInfo\"]/dl"))).getText();
	}
	
	private static String collectComplexPriceRange(WebDriverWait wait) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"summaryInfo\"]/div[2]/div[1]/div/dl[1]/dd"))).getText();
	}
	
	private static String collectComplexName(WebDriverWait wait) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexTitle\"]"))).getText();
	}
	
//	private static void openComplexSelection(WebDriverWait wait) {
//		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div[1]/div/div/a/span[4]"))).click();
//		wait500ms();
//	}
//
//	private static void compareAndRearrangeDong(WebDriver driver, WebDriverWait wait, String dongName, int j) {
//		String CurrentDongName = driver.findElement(By.xpath("//*[@id=\"region_filter\"]/div/a/span[3]")).getText();
//		if (!dongName.equals(CurrentDongName)) {
//			openDongSelection(wait);
//			selectDong(wait, j);
//		}
//	}

	private static void openDongSelection(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"region_filter\"]/div/a/span[3]"))).click();
		wait500ms();
	}

//	private static void compareAndRearrangeGu(WebDriver driver, WebDriverWait wait, String guName, int i) {
//		String CurrentGuName = driver.findElement(By.xpath("//*[@id=\"region_filter\"]/div/a/span[2]")).getText();
//		if (!guName.equals(CurrentGuName)) {
//			openGuSelection(wait);
//			selectGu(wait, i);
//		}
//	}

	private static void openGuSelection(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"region_filter\"]/div/a/span[2]"))).click();
		wait500ms();
	}
	
	private static void openCitySelection(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"region_filter\"]/div/a/span[1]"))).click();
		wait500ms();
	}

	private static String getDongName(WebDriver driver) {
		String dongName = driver.findElement(By.xpath("//*[@id=\"region_filter\"]/div/div/div[1]/div/a[3]")).getText();
		wait500ms();
		return dongName;
	}

	private static String getGuName(WebDriver driver) {
		String guName = driver.findElement(By.xpath("//*[@id=\"region_filter\"]/div/div/div[1]/div/a[2]")).getText();
		wait500ms();
		return guName;
	}

	private static void closeComplexInformation(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/button"))).click();
		wait500ms();
	}

	private static String collectDistance(WebDriverWait wait) {
		String distance = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"detailContents5\"]/div/div[1]"))).getText();
		wait500ms();
		return distance;
	}

	private static void clickSchoolDistrict(WebDriver driver, WebDriverWait wait) {
		int size = driver.findElements(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div/div/a")).size();
		for(int i = 1 ; i <= size ; i++) {
			String content = driver.findElement(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div/div/a[" + i + "]/span")).getText();
			if(content.equals("학군정보")) {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div/div/a[" + i + "]"))).click();
				wait500ms();
				break;
			}
		}
	}

	private static void selectComplex(WebDriverWait wait, int k) {
		//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div/div[1]/div/div/div/div[3]/ul/li[" + k + "]/a"))).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"region_filter\"]/div/div/div[3]/ul/li[" + k + "]/a"))).click();
		wait500ms();
	}
	
	private static int checkComplexSize(WebDriver driver) {
		
		return driver.findElements(By.xpath("//*[@id=\"region_filter\"]/div/div/div[3]/ul/li")).size();
	}
	
	private static void selectDong(WebDriverWait wait, int j) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div/div[1]/div/div/div/div[2]/ul/li[" + j + "]/label"))).click();
		wait500ms();
	}

	private static int checkRegionSize(WebDriver driver) {
		return driver.findElements(By.xpath("//*[@class=\"area_item\"]")).size();
	}

	private static void clickSelectionCity(WebDriverWait wait) {
		//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div/div[1]/div/div/a/span[1]"))).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"region_filter\"]/div/a/span[1]"))).click();
		wait500ms();
	}

	private static void selectSeoul(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div/div[1]/div/div/div/div[2]/ul/li[1]/label"))).click();
		wait500ms();
	}

	public static void selectGu(WebDriverWait wait, int guIndex) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div/div[1]/div/div/div/div[2]/ul/li[" + guIndex + "]/label"))).click();
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