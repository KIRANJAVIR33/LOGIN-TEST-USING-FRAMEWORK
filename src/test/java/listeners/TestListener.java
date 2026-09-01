package listeners;

import base.DriverManager;
import com.aventstack.extentreports.Status;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ConfigReader;
import utils.ExtentReportManager;
import java.io.File;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        ExtentReportManager.createTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.getTest().log(Status.PASS, "Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentReportManager.getTest().log(Status.FAIL, result.getThrowable().getMessage());
        WebDriver driver = DriverManager.getDriver();
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String path = ConfigReader.get("screenshot.path") + result.getMethod().getMethodName() + ".png";
            FileUtils.copyFile(src, new File(path));
        } catch (Exception ignored) {}
    }

    @Override
    public void onFinish(org.testng.ITestContext context) {
        ExtentReportManager.flushReport();
    }
}