package project_estate.dong;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Full3  {
	final static int startGuIndex = 13;
	
	//private static final String filePath = "c:\\KOPO\\git_tarcking\\기본프로그래밍_java\\Pro\\Data.csv";
	private static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
	//private static final String WEB_DRIVER_PATH = "D:\\KOPO\\utility\\chromedriver_win32\\chromedriver.exe";
	private static final String WEB_DRIVER_PATH = "C:\\KOPO\\유틸\\chromedriver_win32 (2)\\chromedriver.exe";

	public static void main(String[] args) throws IOException {
		File f = new File("C:\\KOPO\\git_tracking\\project\\estate\\data.csv");
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
		
		//int[] target = {23};
		
		try {
			// 광역시 배너 클릭
			clickSelectionCity(wait);
			
			// 광역시 중 서울시 선택 -> 자동으로 구 선택을 넘어감
			selectSeoul(wait);
			
			// 총 구의 개수 확인
			int guSize = checkRegionSize(driver);
			
			// 구 개수 만큼 반복
			for (int i = 1; i <= guSize; i++) {
			//for (int ii = 0 ; ii < target.length ; ii++) {
				//int i = target[ii];
				// 서울인지 확인 다르면 처리
				// 추가 예정
				
				if (driver.findElement(By.xpath("//*[@id=\"region_filter\"]/div/div")).getAttribute("aria-hidden").equals("true")) {
					// 동 목록 열기
					openGuSelection(wait);
				}
				if (i != 1) {
					// 동 목록 열기
					openGuSelection(wait);
				}
				// 구 선택 -> 자동으로 동 선택으로 넘아감
				selectGu(wait, i);
				String guName = getGuName(driver);
				
				// 총 동의 개수 확인, 26
				int dongSize = checkRegionSize(driver);
				
				// 동 개수 만큼 반복
				for (int j = 1 ; j <= dongSize ; j++) {
//				for (int j = 1 ; j <= dongSize ; j++) {
//					if(i==23 && j==1) {
//						j = 86;
//					}
					// 두번째 동부터
//					if(j != 1) {
//						// 지역 확인 후 바꼈으면 재설정
//						compareAndRearrangeGu(driver, wait, guName, i);
//					}
					
					// 동 선택 -> 자동으로 단지 선택으로 넘어감
					// 단지 목록이 닫혀잇으면
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
					for (int k = 1 ; k <= complexSize ; k++) {
//						if(i==23 && j==86 && k==1) {
//							k = 1;
//						}
						// 단지 목록이 닫혀잇으면
						if (driver.findElement(By.xpath("//*[@id=\"region_filter\"]/div/div")).getAttribute("aria-hidden").equals("true")) {
							// 단지 목록 열기
							openCitySelection(wait);
						}
						System.out.println(complexSize + " / " + k);
						if(!(i==1 && j==1 && k==1)) {
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
						
						// 아파트 주소 수집
						String complexAddress = "";
						try {
							complexAddress = getComplexAddress(wait);
						} catch (Exception e) {
							complexAddress = "정보 없음";
						}
						
						// 가격순 정렬 버튼이 안 눌려있으면 클릭하여 낮은가격순으로 변경
						clickPriceSort(driver, wait);
						
						// 가격순이 높은 가격 순이면 클릭하여 낮은 가격순으로 변경
						setPriceSortAsc(driver, wait);
						
						
					/* 매매 조사 */
						int mintradeSize = 0;			// 현재 등록된 최저가 매물의 가격
						int mintradePrice = 0;			// 현재 등록된 최저가 매물의 면적 
						int recentRealtradePrice = 0;	// 해당 최저가 매물과 같은 면적의 최근 실거래 가격
						int recentRealtradeDate = 0;		// 해당 최저가 매물과 같은 면적의 최근 실거래 날짜
						
						/* 거래방식 매매로 설정 */
						// 전체거래방식 클릭하여 선택지 열기
						openDealMethod(wait);

						// 전체거래방식 매매로 선택
						selectDealMethod_trade(wait);
						
						// 선택지 목록 닫기
						closeDealMethod(wait);
						
						/* 면적 범위설정 전 임시 최저가 임시 저장 */
						int temptradeSize = 0;
						int temptradePriceNum = 0;
						int tempRecentRealtradePrice = 0;
						int tempRecentRealtradeDate = 0;	
						
						try {
							// 아파트 면적 데이터 수집
							String temptradeInfo = getTradeInfo(wait);
							temptradeSize = Integer.parseInt(temptradeInfo.split(",")[0].split("/")[0].replaceAll("[^0-9]", ""));
							
							// 아파트 가격 데이터 수집
							String temptradePriceKor = gettradePrice(wait);
							temptradePriceNum = Integer.parseInt(temptradePriceKor.replace(",", "").replace("억", "").replace(" ", ""));
							
							// 최근 실거래가 / 실거래 날짜 확인					
							try {	// 실거래가가 있는 경우
								// 클릭하여 상세 정보 열기
								clickFirstOne(driver, wait);
								
								// "시세/실거래가" 배너 클릭
								clickRecentReal(wait);
								
								tempRecentRealtradePrice = Integer.parseInt(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[1]/td/div/div/span"))).getText().split("\\(")[0].replace(",", "").replace("억", "").replace(" ", ""));
								wait500ms();
								
								
								tempRecentRealtradeDate = Integer.parseInt(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[1]/th"))).getText().replaceAll("\\.", ""));
								wait500ms();
								
							} catch (Exception e) { }		// 실거래가가 없는 경우
						} catch (Exception e) { } // 매물이 하나도 없음
						
						
						
						/* 면적 선택하여 최저가 확인 */
						// 전체 면적 선택하여 선택지 열기
						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[2]/button/span"))).click();
						wait500ms();
						// 전체 면적 선택 (75 ~85) - 범위가 없을 시 전체 선택이 유지된다.
						int sizeIndex = 1;
						while (true) {
							try {
								int sizeOption = Integer.parseInt(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[2]/div/div/ul/li[" + sizeIndex + "]/label"))).getText().replaceAll("[^0-9]", ""));
								wait500ms();
								if ( 75 <= sizeOption && sizeOption <= 85) {
									wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[2]/div/div/ul/li[" + sizeIndex + "]/label"))).click();
									wait500ms();
								}
							} catch (Exception e) {	// 모든 면적 옵션을 확인하면 종료
								break;
							}
						}
						// 선택지 닫기
						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[2]/div/button/i"))).click();
						wait500ms();

						// 목표한 범위 내에 존재하는 최저가 가격과 면적 가져오기
						try {		// 범위 내에 매물이 있는 경우
							String mintradeInfo = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"articleListArea\"]/div[1]/div/a/div[3]/p[1]/span"))).getText();
							wait500ms();
							mintradeSize = Integer.parseInt(mintradeInfo.split(",")[0].split("/")[0].replaceAll("[^0-9]", ""));
							
							String mintradePriceKor = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"articleListArea\"]/div[1]/div/a/div[2]/span[2]"))).getText();
							wait500ms();
							mintradePrice = Integer.parseInt(mintradePriceKor.replace(",", "").replace("억", "").replace(" ", ""));
							
							// 최근 실거래가 확인
							try {	// 실거래가가 있는 경우
								
								// 클릭하여 상세정보 보기
								clickFirstOne(driver, wait);
								//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"articleListArea\"]/div[1]/div/a"))).click();
								//wait500ms();
								// "시세/실거래가" 배너 클릭
								//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div[2]/div/a[2]/span"))).click();
								//wait500ms();
								clickRecentReal(wait);
								recentRealtradePrice = Integer.parseInt(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[1]/td/div/div/span"))).getText().split("\\(")[0].replace(",", "").replace("억", "").replace(" ", ""));
								wait500ms();
								recentRealtradeDate = Integer.parseInt(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[1]/th"))).getText().replaceAll("\\.", ""));
							} catch (Exception e) {		// 실거래가가 없는 경우
								//System.out.println("  목표한 면적의 매매 최근 실거래가 / 날짜 없음. 0으로 저장.");
							}
						} catch(Exception e) {	// 범위 내에 매물이 없는 경우
							mintradeSize = temptradeSize;
							mintradePrice = temptradePriceNum;
							recentRealtradePrice = tempRecentRealtradePrice;
							recentRealtradeDate = tempRecentRealtradeDate;
						}
						
					/* 전세 조사 */
						int minRentSize = 0;			// 현재 등록된 최저가 전세의 가격
						int minRentPrice = 0;			// 현재 등록된 최저가 전세의 면적 
						int recentRealRentPrice = 0;	// 해당 최저가 전세와 같은 면적의 최근 실거래 가격
						int recentRealRentDate = 0;		// 해당 최저가 전세와 같은 면적의 최근 실거래 날짜
						
						/* 전체거래방식 전세로 변경 */
						// 전체거래방식 목록지 열기
						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[1]/button/span"))).click();
						wait500ms();
						// 체크되있는 매매 체크 풀기
						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[1]/div/div/ul/li[2]/label"))).click();
						wait500ms();
						// 전세 체크 하기
						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[1]/div/div/ul/li[3]/label"))).click();
						wait500ms();
						
						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[1]/div/button"))).click();
						wait500ms();
						
						
						try {	// 목표한 면적의 매물이 존재하는 경우
							// 목표한 범위 내에 존재하는 최저가 가격과 면적 가져오기
							try {		// 범위 내에 매물이 있는 경우
								String minRentInfo = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"articleListArea\"]/div[1]/div/a/div[3]/p[1]/span"))).getText();
								wait500ms();
								minRentSize = Integer.parseInt(minRentInfo.split(",")[0].split("/")[0].replaceAll("[^0-9]", ""));
								
								String minRentPriceKor = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"articleListArea\"]/div[1]/div/a/div[2]/span[2]"))).getText();
								wait500ms();
								minRentPrice = Integer.parseInt(minRentPriceKor.replace(",", "").replace("억", "").replace(" ", ""));
								
								// 최근 실거래가 확인
								try {	// 실거래가가 있는 경우
									// 클릭하여 상세정보 보기
									clickFirstOne(driver, wait);
									//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"articleListArea\"]/div[1]/div/a"))).click();
									//wait500ms();
									
									// "시세/실거래가" 배너 클릭
									//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div[2]/div/a[2]/span"))).click();
									clickRecentReal(wait);
									wait5000ms();
									//wait500ms();
									System.out.println("전세 임시 : " + wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[1]/td/div/div/span"))).getText() + "/" + wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[1]/th"))).getText());
									recentRealRentPrice = Integer.parseInt(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[1]/td/div/div/span"))).getText().split("\\(")[0].replace(",", "").replace("억", "").replace(" ", ""));
									wait500ms();
									recentRealRentDate = Integer.parseInt(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[1]/th"))).getText().replaceAll("\\.", ""));
									wait500ms();
								} catch (Exception e) {		// 실거래가가 없는 경우
									//System.out.println("  목표한 면적의 전세 최근 실거래가 / 날짜 없음. 0으로 저장.");
								}
							} catch(Exception e) {			// 범위 내에 매물이 없는 경우
								throw e;
							}
							
						} catch (Exception e) { // 목표한 면적의 매물이 존재하지 않는 경우 - 전체 범위에서 데이터 수집
							// 전체면적 선택지 열기
							wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[2]/button/span"))).click();
							wait500ms();
							// 전체면적 전체로 선택하기
							wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[2]/div/div/ul/li[1]/label"))).click();
							wait500ms();
							
							try { // 매물이 존재하는 경우
								String mintradeInfo = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"articleListArea\"]/div[1]/div/a/div[3]/p[1]/span"))).getText();	//  "79A/59m², 중/27층, 남동향"
								wait500ms();
								minRentSize = Integer.parseInt(mintradeInfo.split(",")[0].split("/")[0].replaceAll("[^0-9]", ""));																	//  79
								
								String minRentPriceKor = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"articleListArea\"]/div[1]/div/a/div[2]/span[2]"))).getText();	// "21억 5,000"
								wait500ms();
								minRentPrice = Integer.parseInt(minRentPriceKor.replace(",", "").replace("억", "").replace(" ", ""));															// 215000
								// 최근 실거래가 확인
								try {	// 실거래가가 있는 경우
									// 클릭하여 상세정보 보기
									clickFirstOne(driver, wait);
									//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"articleListArea\"]/div[1]/div/a"))).click();
									//wait500ms();
									
									// "시세/실거래가" 배너 클릭
									//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div[2]/div/a[2]/span"))).click();
									clickRecentReal(wait);
									wait5000ms();
									//wait500ms();
									System.out.println("전세 임시 : " + wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[1]/td/div/div/span"))).getText() + "/" + wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[1]/th"))).getText());
									recentRealRentPrice = Integer.parseInt(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[1]/td/div/div/span"))).getText().split("\\(")[0].replace(",", "").replace("억", "").replace(" ", ""));
									wait500ms();
									recentRealRentDate = Integer.parseInt(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[1]/th"))).getText().replaceAll("\\.", ""));
									wait500ms();
								} catch (Exception ex) {		// 실거래가가 없는 경우
									//System.out.println("  목표한 면적의 전세 최근 실거래가 / 날짜 없음. 0으로 저장.");
								}
							} catch (Exception ex) { // 전세 하나도 없음
								//System.out.println("  전세 하나도 없음. 모두 0으로 저장.");
							}
							
							/* 면적 범위설정 전 임시 최저가의 최근 실거래가 확인 */					
							try {	// 실거래가가 있는 경우
								// 클릭하여 상세정보 보기
								clickFirstOne(driver, wait);
								//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"articleListArea\"]/div[1]/div/a"))).click();
								//wait500ms();
								// "시세/실거래가" 배너 클릭
								//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div[2]/div/a[2]/span"))).click();
								//wait500ms();
								clickRecentReal(wait);
								System.out.println("전세찐 : " + wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[1]/td/div/div/span"))).getText() + "/" + wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[1]/th"))).getText());
								recentRealRentPrice = Integer.parseInt(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[1]/td/div/div/span"))).getText().split("\\(")[0].replace(",", "").replace("억", "").replace(" ", ""));
								wait500ms();
								recentRealRentDate = Integer.parseInt(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[1]/th"))).getText().replaceAll("\\.", ""));
								wait500ms();
								
								// 최근 실거래가 확인
								try {	// 실거래가가 있는 경우
									// 클릭하여 상세정보 보기
									clickFirstOne(driver, wait);
									//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"articleListArea\"]/div[1]/div/a"))).click();
									//wait500ms();
									
									// "시세/실거래가" 배너 클릭
									//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div[2]/div/a[2]/span"))).click();
									clickRecentReal(wait);
									wait5000ms();
									//wait500ms();
									System.out.println("전세 임시 : " + wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[1]/td/div/div/span"))).getText() + "/" + wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[1]/th"))).getText());
									recentRealRentPrice = Integer.parseInt(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[1]/td/div/div/span"))).getText().split("\\(")[0].replace(",", "").replace("억", "").replace(" ", ""));
									wait500ms();
									recentRealRentDate = Integer.parseInt(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[1]/th"))).getText().replaceAll("\\.", ""));
									wait500ms();
								} catch (Exception ex) {		// 실거래가가 없는 경우
									//System.out.println("  목표한 면적의 전세 최근 실거래가 / 날짜 없음. 0으로 저장.");
								}
							} catch (Exception ex) {		// 실거래가가 없는 경우
								//System.out.println("  임시 최저가 전세 최근 실거래가 / 실거래 날짜 없음. 0으로 저장.");
							}
						}
						

						// 저장할 필드 : 지역구 / 지역동 / 아파트명 / 아파트주소 // 최저가 매매 / 최저가 매매의 면적 / 최저가 매매의 최근 실거래가 / 최저가 매매의 최근 거래날짜 // 최저가 전세 / 최저가 전세의 면적 / 최저가 전세의 최근 실거래가 / 최저가 전세의 최근 거래 날짜
						bw.write(guName + "," + dongName + "," + complexName.replaceAll(",", "") + "," + complexAddress + "," + mintradeSize + "," + mintradePrice + "," + recentRealtradePrice + "," + recentRealtradeDate + "," + minRentSize + ","  + minRentPrice + "," +  recentRealRentPrice + "," + recentRealRentDate);
						System.out.println("* " + guName + "," + dongName + "," + complexName.replaceAll(",", "") + "," + complexAddress + "," + mintradeSize + "," + mintradePrice + "," + recentRealtradePrice + "," + recentRealtradeDate + "," + minRentSize + ","  + minRentPrice + "," +  recentRealRentPrice + "," + recentRealRentDate);
						System.out.println("---------------------------------------");
						bw.newLine();
						
						// 현재 단지 닫기
						closeComplexInformation(wait);
					}
					break;
				}
				break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		bw.flush();
		bw.close();
	}
	


	

	
	private static void clickRecentReal( WebDriverWait wait) {			 //*[@id="detailTab2"]/span
		//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div[2]/div/a[2]/span"))).click();
		//																 /html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div/div/a[2]/span
		//																 /html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div[2]/div/a[2]/span
		//																 /html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[1]/div[2]/div/a[2]/span
		try {
			System.out.println("t1");
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div[2]/div/a[2]/span"))).click();
		} catch (Exception e) {
			try {
				System.out.println("t2");
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(" /html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div/div/a[2]/span"))).click();
			} catch (Exception ex) {
				try {
					System.out.println("t3");
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div[2]/div/a[2]/span"))).click();
				} catch (Exception exc) {
					System.out.println("t4");
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[1]/div[2]/div/a[2]/span"))).click();
				}
			}
		}
		wait500ms();
		
	}
	
	
	private static void clickFirstOne(WebDriver driver, WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[1]/div/div[2]/div[2]/div/div/div[1]/div/a/div[1]/span"))).click();
		wait500ms();
		Set<String> windows = driver.getWindowHandles();
		System.out.println("창개수" + windows.size());
		//driver.switchTo().window(windows.iterator().next()).
		if (windows.size() > 1) {
			Iterator<String> iter = windows.iterator();
			String firstWindow = iter.next();
			while (iter.hasNext()) {
				driver.switchTo().window(iter.next()).close();
			}
			driver.switchTo().window(firstWindow);
		}		
	}
	
	private static String gettradePrice(WebDriverWait wait) {
		String temptradePriceKor = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"articleListArea\"]/div[1]/div/a/div[2]/span[2]"))).getText();	// "21억 5,000"
		wait500ms();
		return temptradePriceKor;
	}
	
	private static String getTradeInfo(WebDriverWait wait) {
		String temptradeInfo = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"articleListArea\"]/div[1]/div/a/div[3]/p[1]/span"))).getText();	//  "79A/59m², 중/27층, 남동향"
		wait500ms();
		return temptradeInfo;
	}
	
	private static void closeDealMethod(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[1]/div/button/i"))).click();
		wait500ms();
	}
	
	private static void selectDealMethod_trade(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[1]/div/div/ul/li[2]/label"))).click();
		wait500ms();
	}

	private static void openDealMethod(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[1]/button/span"))).click();
		wait500ms();
	}
	
	private static void setPriceSortAsc(WebDriver driver, WebDriverWait wait) {
		WebElement priceSort = driver.findElement(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[3]/a[3]"));
		if (priceSort.getAttribute("class").equals("sorting_type is-descending")) {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[3]/a[3]"))).click();
			wait500ms();
		}
	}
	
	private static void clickPriceSort(WebDriver driver, WebDriverWait wait) {
		WebElement priceSort = driver.findElement(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[3]/a[3]"));
		if (priceSort.getAttribute("aria-pressed").equals("false")) {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[3]/a[3]"))).click();
			wait500ms();
		}
	}
	
	private static String getComplexAddress(WebDriverWait wait) {
		String complexAddress = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("address"))).getText();
		wait500ms();
		return complexAddress;
	}

	private static void closeSelection(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"region_filter\"]/div/a/span[3]"))).click();
		wait500ms();
	}

	private static String collectComplexName(WebDriverWait wait) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexTitle\"]"))).getText();
	}

	private static void openDongSelection(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"region_filter\"]/div/a/span[3]"))).click();
		wait500ms();
	}

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

	private static void selectComplex(WebDriverWait wait, int k) {
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
			Thread.sleep(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void wait5000ms() {
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}