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

public class Full5  {
	final static int startGuIndex = 13;
	
	//private static final String filePath = "c:\\KOPO\\git_tarcking\\기본프로그래밍_java\\Pro\\Data.csv";
	private static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
	//private static final String WEB_DRIVER_PATH = "D:\\KOPO\\utility\\chromedriver_win32\\chromedriver.exe";
	private static final String WEB_DRIVER_PATH = "C:\\KOPO\\유틸\\chromedriver_win32 (2)\\chromedriver.exe";

	public static void main(String[] args) throws IOException {
		File f = new File("C:\\KOPO\\git_tracking\\project\\estate\\도곡동.csv");
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
				for (int j = 4 ; j <= dongSize ; j++) {
//				for (int j = 1 ; j <= dongSize ; j++) {
//					if(i==23 && j==1) {
//						j = 86;
//					}
					// 두번째 동부터
					if(j != 4) {
						// 지역 확인 후 바꼈으면 재설정
						compareAndRearrangeGu(driver, wait, guName, i);
					}
					
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
					for (int k = 14 ; k <= complexSize ; k++) {
//						if(i==23 && j==86 && k==1) {
//							k = 1;
//						}
						// 단지 목록이 닫혀잇으면
						if (driver.findElement(By.xpath("//*[@id=\"region_filter\"]/div/div")).getAttribute("aria-hidden").equals("true")) {
							// 단지 목록 열기
							openCitySelection(wait);
						}
						System.out.println(complexSize + " / " + k);

						//if (!(i==1 && j==3 && k==1)) {
						if (!(k==14)) {
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
						
						/* 정렬 조건 : 낮은 가격 순 */
						// 가격순 정렬 버튼이 안 눌려있으면 클릭하여 낮은가격순으로 변경
						clickPriceSort(driver, wait);
						
						// 가격순이 높은 가격 순이면 클릭하여 낮은 가격순으로 변경
						setPriceSortAsc(driver, wait);
						
						 
						
						/* 면적마다 최저가 확인 */
						int indexSize = 6; //default 2
						while (true) {	// 면적 반복
							try {
								/* 거래방식 : 매매  */
								// 전체거래방식 클릭하여 선택지 열기
								openDealMethod(wait);
								
								// 전체거래방식 전체로 초기화
								selectDealMethod_all(wait);
								
								// 전체거래방식 매매로 선택
								selectDealMethod_trade(wait);
								
								// 선택지 목록 닫기
								closeDealMethod(wait);
								
								/* 매매 데이터 저장될 변수 */
								int tradeSize = 0;
								int tradePrice = 0;
								
								int indexRealTrade = 1;			// 매매 실거래 한줄에 해당하는 번호
								String recentTradeDate = "0";	// 해당 면적의 최근 거래 연월
								int recentTradePrice = 0;		// 해당 면적의 최근 거래가
								String maxTradeDate = "0";		// 해당 면적의 최고가의 거래 연월
								int maxTradePrice = 0;			// 해당 명적의 최고 거래가
							
								// 면접 옵션 선택창 열기
								wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[2]/button/span"))).click();
								wait200ms();
								// 확인하고자 하는 면적 옵션 클릭하여 선택
								wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[2]/div/div/ul/li[" + indexSize + "]/label"))).click();
								wait200ms();
								// 이전 선택된 면적 옵션 클릭하여 해제
								if (indexSize != 6) {
									wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[2]/div/div/ul/li[" + (indexSize - 1) + "]/label"))).click();
									wait200ms();
								}
								// 면접 옵션 선책창 닫기
								wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[2]/div/button/i"))).click();
								wait200ms();
								
								try {
									// 최저가 매물 면적 정보 가져오기
									String tradeInfo = getTradeInfo(wait);
									//tradeSize = Integer.parseInt(tradeInfo.split(",")[0].split("/")[0].replaceAll("[^0-9]", ""));
									tradeSize = Integer.parseInt(tradeInfo.split(",")[0].split("/")[0].split("[A-Z]")[0]);
									System.out.println("tradeSize");
									// 최저가 매물 가격 정보 가져오기
									String tradePriceKor = gettradePrice(wait);
									tradePrice = convertPrice(tradePriceKor);
									
									// 최저가 매물 들어가기
									clickFirstOne(driver, wait);
									
									// 시세/실거래가 베너 들어가기
									clickRecentReal(wait);
									wait200ms();
									
									
									// 최근 실거래가의 정보 가져오기
									while (true) {	// 한달(한줄) 반복
										int minPrice = 0;	// 거래가 발행한 해당 달의 최저 거래가
										int maxPrice = 0;	// 거래가 발생한 해당 달의 최고 거래가
										String realTradeDate = "";
										
										try {	// 한 달에 거래가 하나인 경우
											System.out.println("실거래 있는 경우:" + indexRealTrade);
											int indexPrice = 1;
											realTradeDate = driver.findElement(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[" + indexRealTrade + "]/th")).getText().replaceAll("\\.", "");
											if (Integer.parseInt(realTradeDate) < 202001) {
												break;
											}
											//System.out.println("날짜" + realTradeDate);
											while (true) {	// 한 달 내 거래가 반복
												System.out.println("indexPrice" + indexPrice);
												int oneOfPrices = 0;
												try {
													String realTradePrice = driver.findElement(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[" + indexRealTrade + "]/td/div/div/span[" + indexPrice + "]")).getText();
													System.out.println(indexRealTrade + "&" + indexPrice + ":" + realTradeDate + "/" + realTradePrice);
													if (realTradePrice.contains("계약취소")) {
														indexPrice++;
														continue;
													}
													oneOfPrices = convertPrice(realTradePrice);
													
													// 최신 실거래 데이터 저장 & 최고 실거래가 확인&저장
													if (indexRealTrade == 1 && indexPrice == 1) {
														recentTradeDate = realTradeDate;
														recentTradePrice = oneOfPrices;	
														minPrice = oneOfPrices;
														maxPrice = oneOfPrices;
														maxTradeDate = realTradeDate;
													} else if (oneOfPrices < minPrice) {
														minPrice = oneOfPrices;
													} else if (oneOfPrices > maxPrice) {
														maxPrice = oneOfPrices;
													}
												} catch (Exception ex) { // 한 달 내 거래 모두 조회 후
													System.out.println("여러 개 모두 조회 완료");
													break;
												}
												indexPrice++;
											}
										} catch (Exception ex) {
											System.out.println("끝");
											try {
												System.out.println("더보기");
												wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/button"))).click();
												wait200ms();
											} catch (Exception exc) {
												System.out.println("종료");
												break;
											}
										}
										
										if (maxTradePrice < maxPrice) {	// 실거래 최고가 저장
											maxTradePrice = maxPrice;
											maxTradeDate = realTradeDate;
										}
										indexRealTrade++;
										System.out.println(maxTradeDate + ":" + maxTradePrice + "////" + realTradeDate + ":" + maxPrice);
									}
								} catch (Exception ex) {
									System.out.println("매물 없음");
								}
								
								
								
								
								
								/* 거래방식 전세 */
								// 전체거래방식 클릭하여 선택지 열기
								openDealMethod(wait);
								
								// 기존 선택 방식 초기화 - 전체 선택
								selectDealMethod_all(wait);
								
								// 전체거래방식 매매로 선택
								selectDealMethod_rent(wait);
								
								// 선택지 목록 닫기
								closeDealMethod(wait);
								
								/* 전세 데이터 저장될 변수 */
								int rentSize = 0;
								int rentPrice = 0;
								
								int indexRealRent = 1;			// 전세 실거래 한줄에 해당하는 번호
								String recentRentDate = "0";	// 해당 면적의 최근 거래 연월
								int recentRentPrice = 0;		// 해당 면적의 최근 거래가
								String maxRentDate = "0";		// 해당 면적의 최고가의 거래 연월
								int maxRentPrice = 0;			// 해당 명적의 최고 거래가
								
								try {
									// 최저가 전세 평수
									String rentInfo = getTradeInfo(wait);
									//rentSize = Integer.parseInt(rentInfo.split(",")[0].split("/")[0].replaceAll("[^0-9]", ""));
									rentSize = Integer.parseInt(rentInfo.split(",")[0].split("/")[0].split("[A-Z]")[0]);
									
									// 최저가 매물 가격 정보 가져오기
									String rentPriceKor = gettradePrice(wait);
									rentPrice = convertPrice(rentPriceKor);
									
									// 최저가 매물 들어가기
									clickFirstOne(driver, wait);
									
									// 시세/실거래가 베너 들어가기
									clickRecentReal(wait);
									wait200ms();
									
									
									// 최근 실거래가의 정보 가져오기
									while (true) {	// 한달(한줄) 반복
										int minPrice = 0;	// 거래가 발행한 해당 달의 최저 거래가
										int maxPrice = 0;	// 거래가 발생한 해당 달의 최고 거래가
										String realRentDate = "";
										
										try {	// 한 달에 거래가 하나인 경우
											System.out.println("실거래 있는 경우:" + indexRealRent);
											int indexPrice = 1;
											realRentDate = driver.findElement(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[" + indexRealRent + "]/th")).getText().replaceAll("\\.", "");
											if (Integer.parseInt(realRentDate) < 202001) {
 												break;
											}
											//System.out.println("날짜" + realTradeDate);
											while (true) {	// 한 달 내 거래가 반복
												System.out.println("indexPrice" + indexPrice);
												int oneOfPrices = 0;
												try {
													String realRentPrice = driver.findElement(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/table/tbody/tr[" + indexRealRent + "]/td/div/div/span[" + indexPrice + "]")).getText();
													System.out.println(indexRealRent + "&" + indexPrice + ":" + realRentDate + "/" + realRentPrice);
													if (realRentPrice.contains("계약취소")) {
														indexPrice++;
														continue;
													}
													oneOfPrices = convertPrice(realRentPrice);
													
													// 최신 실거래 데이터 저장 & 최고 실거래가 확인&저장
													if (indexRealRent == 1 && indexPrice == 1) {
														recentRentDate = realRentDate;
														recentRentPrice = oneOfPrices;	
														minPrice = oneOfPrices;
														maxPrice = oneOfPrices;
														maxRentDate = realRentDate;
													} else if (oneOfPrices < minPrice) {
														minPrice = oneOfPrices;
													} else if (oneOfPrices > maxPrice) {
														maxPrice = oneOfPrices;
													}
												} catch (Exception ex) { // 한 달 내 거래 모두 조회 후
													System.out.println("여러 개 모두 조회 완료");
													break;
												}
												indexPrice++;
											}
										} catch (Exception ex) {
											System.out.println("끝");
											try {
												System.out.println("더보기");
												wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"tabpanel1\"]/div[4]/button"))).click();
												wait200ms();
											} catch (Exception exc) {
												System.out.println("종료");
												break;
											}
										}
										
										if (maxRentPrice < maxPrice) {	// 실거래 최고가 저장
											maxRentPrice = maxPrice;
											maxRentDate = realRentDate;
										}
										System.out.println(maxRentDate + ":" + maxRentPrice + "////" + realRentDate + ":" + maxPrice);
										indexRealRent ++;
									}
									
									int size = tradeSize == 0 ? rentSize : tradeSize;
									
									
									/* 수집한 데이터 저장 */
									// 저장할 필드 : 지역구 / 지역동 / 아파트명 / 아파트주소 // 최저가 매매 면적 / 최저가 매매의 가격 / 최저가 매매의 최근 실거래가 / 최저가 매매의 최근 거래날짜 / 최저가 매매의 최근 실거래 최고가의 가격 / 최저가 매매의 최근 실거래 최고가의 거래날짜 // 최저가 전세 / 최저가 전세의 면적 / 최저가 전세의 최근 실거래가 / 최저가 전세의 최근 거래 날짜
									System.out.println(guName + "," + dongName + "," + complexName.replaceAll(",", "") + "," + complexAddress + "," + size);
									System.out.println(tradePrice + "," + recentTradeDate + "," + recentTradePrice + "," + maxTradeDate + ","  + maxTradePrice);
									System.out.println(rentPrice + "," + recentRentDate + "," + recentRentPrice + "," + maxRentDate + ","  + maxRentPrice);
									bw.write(guName + "," + dongName + "," + complexName.replaceAll(",", "") + "," + complexAddress + "," + tradeSize + "," + tradePrice + "," + recentTradeDate + "," + recentTradePrice + "," + maxTradeDate + ","  + maxTradePrice + "," + rentPrice + "," + recentRentDate + "," + recentRentPrice + "," + maxRentDate + ","  + maxRentPrice);
									System.out.println("---------------------------------------");
									bw.newLine();
								} catch (Exception ex) {
									System.out.println("매물 없음");
								}
								
								indexSize++;
							} catch (Exception e) {
								System.out.println("아파트 단지 끝");
								break;
							}
						}
						
						// 현재 단지 닫기
						closeComplexInformation(wait);
					}
					break;
				}
				break;
			}
		} catch(Exception e) {
			e.printStackTrace();
			bw.flush();
			bw.close();
		}

		bw.flush();
		bw.close();
	}
	

	
	
	private static void compareAndRearrangeGu(WebDriver driver, WebDriverWait wait, String guName, int i) {
		String CurrentGuName = driver.findElement(By.xpath("//*[@id=\"region_filter\"]/div/a/span[2]")).getText();
		if (!guName.equals(CurrentGuName)) {
			openGuSelection(wait);
			selectGu(wait, i);
		}
	}
	
	private static int convertPrice(String priceKor) {
		String sPrice = priceKor.split("\\(")[0].replace(",", "").replace(" ", "");
		String[] prices = sPrice.split("억");
		int price = 0;
		if (prices.length == 1) {
			price = Integer.parseInt(prices[0]) * 10000;
		} else {
			price =  Integer.parseInt(prices[0]) * 10000 + Integer.parseInt(prices[1]);
		}
		return price;
	}

	private static void clickRecentReal(WebDriverWait wait) {
		try {
			//System.out.println("1");
			if (wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div[2]/div/a[2]/span"))).getText().equals("시세/실거래가")) {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div[2]/div/a[2]/span"))).click();
			}
		} catch (Exception ex) {
			try {
				//System.out.println("7");
				if (wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[1]/div[2]/div/a[2]/span"))).getText().equals("시세/실거래가")) {
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[1]/div[2]/div/a[2]/span"))).click();
				}																 
			} catch (Exception exc) {
				try {
					//System.out.println("3");
					if (wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div[2]/div/a[2]/span"))).getText().equals("시세/실거래가")) {
						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div[2]/div/a[2]/span"))).click();
					}
				} catch (Exception excp) {
					try {
						//System.out.println("4");
						if (wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[1]/div[2]/div/a[2]/span"))).getText().equals("시세/실거래가")) {
							wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[1]/div[2]/div/a[2]/span"))).click();
						}
					} catch (Exception excep) {
						try {																 
							//System.out.println("5");
							if (wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[3]/div/div[4]/table/tbody/tr[1]/td/div/div/span"))).getText().equals("시세/실거래가")) {
								wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[3]/div/div[4]/table/tbody/tr[1]/td/div/div/span"))).click();
							}
						} catch (Exception except) {
							try {
								//System.out.println("6");
								if (wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div[2]/div/a[2]/span"))).getText().equals("시세/실거래가")) {
									wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div[2]/div/a[2]/span"))).click();
								}
							} catch (Exception excepti) {
								//System.out.println("2");
								if (wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div/div/a[2]/span"))).getText().equals("시세/실거래가")) {
									wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div/div/a[2]/span"))).click();
								}	
							}
						}
					}
				}
			}
		}
		wait200ms();
	}

	
	private static void clickFirstOne(WebDriver driver, WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[1]/div/div[2]/div[2]/div/div/div[1]/div/a/div[1]/span"))).click();
		wait200ms();
		Set<String> windows = driver.getWindowHandles();
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
		wait200ms();
		return temptradePriceKor;
	}
	
	private static String getTradeInfo(WebDriverWait wait) {
		String temptradeInfo = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"articleListArea\"]/div[1]/div/a/div[3]/p[1]/span"))).getText();	//  "79A/59m², 중/27층, 남동향"
		wait200ms();
		return temptradeInfo;
	}
	
	private static void closeDealMethod(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[1]/div/button/i"))).click();
		wait200ms();
	}
	
	private static void selectDealMethod_trade(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[1]/div/div/ul/li[2]/label"))).click();
		wait200ms();
	}
	
	private static void selectDealMethod_all(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[1]/div/div/ul/li[1]/label"))).click();
		wait200ms();
	}
	
	private static void selectDealMethod_rent(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[1]/div/div/ul/li[3]/label"))).click();
		wait200ms();
	}

	private static void openDealMethod(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[2]/div/div[1]/button/span"))).click();
		wait200ms();
	}
	
	private static void setPriceSortAsc(WebDriver driver, WebDriverWait wait) {
		WebElement priceSort = driver.findElement(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[3]/a[3]"));
		if (priceSort.getAttribute("class").equals("sorting_type is-descending")) {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[3]/a[3]"))).click();
			wait200ms();
		}
	}
	
	private static void clickPriceSort(WebDriver driver, WebDriverWait wait) {
		WebElement priceSort = driver.findElement(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[3]/a[3]"));
		if (priceSort.getAttribute("aria-pressed").equals("false")) {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexOverviewList\"]/div[2]/div[1]/div[3]/a[3]"))).click();
			wait200ms();
		}
	}
	
	private static String getComplexAddress(WebDriverWait wait) {
		String complexAddress = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("address"))).getText();
		wait200ms();
		return complexAddress;
	}

	private static void closeSelection(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"region_filter\"]/div/a/span[3]"))).click();
		wait200ms();
	}

	private static String collectComplexName(WebDriverWait wait) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexTitle\"]"))).getText();
	}

	private static void openDongSelection(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"region_filter\"]/div/a/span[3]"))).click();
		wait200ms();
	}

	private static void openGuSelection(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"region_filter\"]/div/a/span[2]"))).click();
		wait200ms();
	}
	
	private static void openCitySelection(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"region_filter\"]/div/a/span[1]"))).click();
		wait200ms();
	}

	private static String getDongName(WebDriver driver) {
		String dongName = driver.findElement(By.xpath("//*[@id=\"region_filter\"]/div/div/div[1]/div/a[3]")).getText();
		wait200ms();
		return dongName;
	}

	private static String getGuName(WebDriver driver) {
		String guName = driver.findElement(By.xpath("//*[@id=\"region_filter\"]/div/div/div[1]/div/a[2]")).getText();
		wait200ms();
		return guName;
	}

	private static void closeComplexInformation(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/button"))).click();
		wait200ms();
	}

	private static void selectComplex(WebDriverWait wait, int k) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"region_filter\"]/div/div/div[3]/ul/li[" + k + "]/a"))).click();
		wait200ms();
	}
	
	private static int checkComplexSize(WebDriver driver) {
		
		return driver.findElements(By.xpath("//*[@id=\"region_filter\"]/div/div/div[3]/ul/li")).size();
	}
	
	private static void selectDong(WebDriverWait wait, int j) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div/div[1]/div/div/div/div[2]/ul/li[" + j + "]/label"))).click();
		wait200ms();
	}

	private static int checkRegionSize(WebDriver driver) {
		return driver.findElements(By.xpath("//*[@class=\"area_item\"]")).size();
	}

	private static void clickSelectionCity(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"region_filter\"]/div/a/span[1]"))).click();
		wait200ms();
	}

	private static void selectSeoul(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div/div[1]/div/div/div/div[2]/ul/li[1]/label"))).click();
		wait200ms();
	}

	public static void selectGu(WebDriverWait wait, int guIndex) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div/div[1]/div/div/div/div[2]/ul/li[" + guIndex + "]/label"))).click();
		wait200ms();
	}
	
	public static void wait200ms() {
		try {
			Thread.sleep(200);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}