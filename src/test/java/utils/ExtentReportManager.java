package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportManager {
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static ExtentReports getReportInstance() {
        if (extent == null) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            ExtentSparkReporter reporter = new ExtentSparkReporter(ConfigReader.get("report.path") + "Report_" + timestamp + ".html");
            extent = new ExtentReports();
            extent.attachReporter(reporter);
        }
        return extent;
    }

    public static void createTest(String name) { test.set(getReportInstance().createTest(name)); }
    public static ExtentTest getTest() { return test.get(); }
    public static void flushReport() { if (extent != null) extent.flush(); }
}