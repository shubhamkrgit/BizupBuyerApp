package buyer.utils;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.google.common.collect.ImmutableMap;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;

public class AndroidUtils {

	AndroidDriver driver;

	// Assign driver to AndroidUtils
	public AndroidUtils(AndroidDriver driver) {
		this.driver = driver;
	}

	public void LaunchApp() throws InterruptedException {
		driver.activateApp("com.sot.bizup.debug");
		System.out.println("App Launched ✔");
		Thread.sleep(5000);
	}

	// Back Method
	public void Back() {
		driver.pressKey(new KeyEvent(AndroidKey.BACK));
	}

	// Enter Method
	public void Enter() {
		driver.pressKey(new KeyEvent(AndroidKey.ENTER));
	}

	// Restart App Method
	public void RestartApp() throws InterruptedException {
		driver.terminateApp("com.sot.bizup.debug");
		driver.activateApp("com.sot.bizup.debug");
		Thread.sleep(4000);
	}

	// Clear App Data Method
	public void clearAppData() {
		try {
			 driver.executeScript("mobile: clearAppData", ImmutableMap.of("package", "com.sot.bizup.debug"));
			System.out.println("App Data Cleared ✔");
		} catch (Exception e) {
			Assert.fail("App Data Not Cleared ❌");
		}
	}

	// Send Method
	public void Send() {
		ClickXp("//android.widget.Button[@text=\"\"]");
	}

	// Click method for ID
	public void ClickId(String element) {
		driver.findElement(By.id(element)).click();
	}

	// Click method for xpath
	public void ClickXp(String element) {
		driver.findElement(By.xpath(element)).click();
	}

	// Send message in the chat
	public void SendKey(String message) throws InterruptedException {
		driver.findElement(By.xpath("//android.widget.EditText")).sendKeys(message);
		Send();
		Thread.sleep(3000);
	}

	// Wait for element
	public void Wait(WebElement element, String text) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
		wait.until(ExpectedConditions.attributeContains(((element)), "text", text));
	}

	// Getting seller name from video page
	public String VpSellerName() {
		String sellerName = driver.findElement(By.id("com.sot.bizup.debug:id/mtSellerNameD")).getText();
		System.out.println("Seller name is :- " + sellerName);
		return sellerName;
	}

	// Home to Video page navigation
	public void HomeFeed() throws InterruptedException {
		ClickXp("(//android.widget.ImageView[@resource-id=\"com.sot.bizup.debug:id/ivThumbnail\"])[1]");
		Thread.sleep(2000);
		System.out.println("Land to the video feed ✔");
	}

	// Getting seller name from seller page
	public String SpSellerName() {
		String sellerName = driver.findElement(By.id("com.sot.bizup.debug:id/mtSellerName")).getText();
		System.out.println("Seller name is :- " + sellerName);
		return sellerName;
	}

	// Home to seller page navigation
	public String HomeSeller() throws InterruptedException {
		ClickXp("(//android.widget.ImageView[@resource-id=\"com.sot.bizup.debug:id/ivThumbnail\"])[1]");
		Thread.sleep(2000);
		ClickId("com.sot.bizup.debug:id/mbGood");
		return SpSellerName();
	}

	// Scroll without element
	public void Scroll() {
		boolean canScrollMore;
		do {
			canScrollMore = (Boolean) ((JavascriptExecutor) driver).executeScript("mobile: scrollGesture", ImmutableMap
					.of("left", 100, "top", 100, "width", 200, "height", 200, "direction", "down", "percent", 3.0));
		} while (canScrollMore);
	}

	// Scroll to find Element
	public void ScrollEle(String ele) {
		driver.findElement(AppiumBy
				.androidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView(text(\"" + ele + "\"));"));
	}

	// Toast Message Method
	public String Toast() {

		String message = "";
		By toast = By.xpath("//android.widget.Toast");

		boolean istoast = driver.findElements(toast).size() > 0;

		if (istoast) {
			message = driver.findElement(toast).getAttribute("name");
			System.out.println("Toast Message Appers ✔");
			System.out.println(message);
		} else {
			System.out.println("Toast Message not Appers ❌");
			Assert.fail("Condition failed, marking test as failed");
		}

		return message;
	}

	// Chat Methods
	public void ShortChat1() throws InterruptedException {
		checkAndSend("Hello");
	}

	public void ShortChat2() throws InterruptedException {
		checkAndSend("Shirt chahiye");
	}

	public void LongChat1() throws InterruptedException {
		checkAndSend("Hi", "Pant chahiye", "aur dekhao");
	}

	public void LongChat2() throws InterruptedException {
		checkAndSend("COD milega??", "Delivery charge kitna lagega??", "Delivery kab tak hogi??");
	}

	private void checkAndSend(String... messages) throws InterruptedException {
		By chatButton = By.xpath("//android.widget.Button[@text=\"\"]");
		By sendButton = By.id("com.whatsapp:id/send");

		if (driver.findElements(chatButton).size() > 0) {
			ClickXp("//android.widget.Button[@text=\"\"]");
			System.out.println("Landed on Chat ✔");
			for (String message : messages) {
				SendKey(message);
				Thread.sleep(2000);
			}
		} else if (driver.findElements(sendButton).size() > 0) {
			System.out.println("Landed on WhatsApp ✔");
			Thread.sleep(2000);
			Back();
			Back();
		}
	}

	public void WhatsAppEnable() throws InterruptedException {

		// WhatsApp Enable Click
		By secondElement = By.xpath("//android.widget.TextView[@text=\"ओनर से बात करे\"]");
		By firstElement = By.xpath("//android.widget.Button[@text=\"बात करे\"]");

		// Check if the first element exists
		if (driver.findElements(firstElement).size() > 0) {
			// If the first element exists, click it
			driver.findElement(firstElement).click();
		} else {
			// If the first element doesn't exist, check if the second element exists
			driver.findElement(secondElement).click();
		}

		// Check for PreEnquiryVideo
		PreEnquiryVideoCheck();

		// WhatsApp Check
		WhatsAppCheck();

		// Answer feedback question
		FeedbackQue();
	}

	public void CatalogEnq() throws InterruptedException {

		// CoachMark Check
		CoachMarkCheck("com.sot.bizup.debug:id/ivDealsCoachmarkText");
		ClickId("com.sot.bizup.debug:id/mtSellerCatalog");

		// Full screen catalog enquiry
		ClickXp("(//android.widget.ImageView[@resource-id=\"com.sot.bizup.debug:id/ivItem\"])[1]");
		ClickXp("(//android.widget.Button[@resource-id=\"com.sot.bizup.debug:id/mbShortlist\"])[1]");
		ClickId("com.sot.bizup.debug:id/ivCross");

		// Selecting Catalog
		ClickXp("(//android.widget.Button[@resource-id=\"com.sot.bizup.debug:id/mbShortlist\"])[2]");
		ClickId("com.sot.bizup.debug:id/mbDealKare");

		// PreEnquiryQuestion Check
		PreEnquiryQue();

		// Check for PreEnquiryVideo
		PreEnquiryVideoCheck();

		// Enter the text on the Chat box
		LongChat1();
		driver.hideKeyboard();
		Back();
	}

	public void CatalogEnquiry() throws InterruptedException {

		// CoachMark Check
		CoachMarkCheck("com.sot.bizup.debug:id/ivDealsCoachmarkText");
		ClickId("com.sot.bizup.debug:id/mtSellerCatalog");

		// Selecting Catalog
		ClickXp("(//android.widget.Button[@resource-id=\"com.sot.bizup.debug:id/mbShortlist\"])[1]");
		ClickXp("(//android.widget.Button[@resource-id=\"com.sot.bizup.debug:id/mbShortlist\"])[2]");
		ClickId("com.sot.bizup.debug:id/mbDealKare");
		Thread.sleep(2000);
	}

	public void VideoEnquiry() throws InterruptedException {

		// ------Video Enquiry------
		// Video Tab Change
		ClickId("com.sot.bizup.debug:id/mtSellerVideos");
		ClickId("com.sot.bizup.debug:id/mtSellerVideos");

		// Click on the video
		ClickXp("(//android.widget.ImageView[@resource-id=\"com.sot.bizup.debug:id/ivThumbnail\"])[1]");

		// Seller video baat kare button click
		ClickId("com.sot.bizup.debug:id/mbChat");

		// Check for PreEnquiryVideo
		PreEnquiryVideoCheck();

	}

	public void shortSellerEnquiry() throws InterruptedException {
		try {
			// Click on Baat Kare button
			ClickId("com.sot.bizup.debug:id/mbDealKare");

			// PreEnquiryQuestion Check
			PreEnquiryQue();

			// Check for PreEnquiryVideo
			PreEnquiryVideoCheck();

			// Enter the text on the Chat box
			ShortChat1();

			// Back from Chat
			Back();

			// Video Enquiry
			VideoEnquiry();

			// Enter the text on the Chat box
			ShortChat2();

			// Back from Chat
			Back();

		} catch (Exception e) {
			System.out.println("Short Seller Enquiry error" + e + " ❌");
		}
	}

	public void shortestEnquiry() throws InterruptedException {

		// Click on Baat Kare button
		ClickId("com.sot.bizup.debug:id/mbDealKare");

		// PreEnquiryQuestion Check
		PreEnquiryQue();

		// Check for PreEnquiryVideo
		PreEnquiryVideoCheck();

		// Enter the text on the Chat box
		ShortChat1();

	}

	// CoachMark Check
	public void CoachMarkCheck(String CoachEle) {
		By coachMark = By.id(CoachEle);

		if (driver.findElements(coachMark).size() > 0) {
			// If the coachMark exists, click it
			driver.findElement(coachMark).click();
			System.out.println("CoachMark Displayed ✔");
		}
	}

	// PreEnquiryVideo Check
	public void PreEnquiryVideoCheck() throws InterruptedException {

		By preEnquiryVideo = By.id("com.sot.bizup.debug:id/mbButton");

		if (driver.findElements(preEnquiryVideo).size() > 0) {
			// If the PreEnquiryVideo exists, skip it
			Thread.sleep(8000);
			driver.findElement(preEnquiryVideo).click();
			System.out.println("Pre-Enquiry Video Displayed ✔");
		}
	}

	// Feedback Question
	public void FeedbackQue() {
		try {
			By FeedbackQ = By.id("com.sot.bizup.debug:id/mtQuestion");

			if (driver.findElements(FeedbackQ).size() > 0) {
				ClickId("com.sot.bizup.debug:id/mbPositive");
				ClickId("com.sot.bizup.debug:id/mbMessage");
				driver.pressKey(new KeyEvent(AndroidKey.BACK));
				System.out.println("Feedback Question working ✔");
			}
		} catch (Exception e) {
			Assert.fail("Feedback Question error" + e);
		}
	}

	// Feedback Question
	public void PreEnquiryQue() {

		By PreEnqQ = By.id("com.sot.bizup.debug:id/mtQuestion");

		if (driver.findElements(PreEnqQ).size() > 0) {
			ClickId("com.sot.bizup.debug:id/mbPositive");
			System.out.println("PreEnquiry Question working ✔");
		}

	}

	// WhatsApp Check
	public void WhatsAppCheck() {
		try {
			By WhatsAppCheck = By.id("com.whatsapp:id/send");

			if (driver.findElements(WhatsAppCheck).size() > 0) {
				System.out.println("Landed on WhatsApp ✔");
				Thread.sleep(2000);
				Back();
				Back();
			}
		} catch (Exception e) {
			Assert.fail("WhatsApp Landing Failed" + e);
		}
	}

	public void Agent() {
		try {
			By agent = By.id("com.sot.bizup.debug:id/fab");

			if (driver.findElements(agent).size() > 0) {
				ClickId("com.sot.bizup.debug:id/fab");
				System.out.println("Agent Clicked ✔");
			}
		} catch (Exception e) {
			Assert.fail("Agent failed " + e);
		}

	}

	public void Chat() {
		try {
			ShortChat1();
			driver.hideKeyboard();
			Thread.sleep(2000);
			System.out.println("Chat Sucessfull ✔");
		} catch (Exception e) {
			Assert.fail("Chat error" + e);
		}
	}

}
