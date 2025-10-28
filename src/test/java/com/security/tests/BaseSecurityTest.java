package com.security.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.security.config.ApiConfig.*;

public class BaseSecurityTest {

    protected static ClientApi zapApi;
    protected static String zapProxyHost;
    protected static int zapProxyPort;
    protected static List<SecurityFinding> customFindings = new ArrayList<>();
    private static final String FINDINGS_FILE = "target/custom-findings.json";

    @BeforeAll
    public static void setupZap() {
        zapProxyHost = System.getProperty("zap.proxy.host", "localhost");
        zapProxyPort = Integer.parseInt(System.getProperty("zap.proxy.port", "8080"));

        zapApi = new ClientApi(zapProxyHost, zapProxyPort);

        try {
            // Set up context
            String contextId = zapApi.context.newContext("BostaAPI").toString();
            zapApi.context.includeInContext("BostaAPI", BASE_URL + ".*");

            System.out.println("✓ ZAP Proxy configured at " + zapProxyHost + ":" + zapProxyPort);
            System.out.println("✓ ZAP Context created: BostaAPI");
        } catch (ClientApiException e) {
            System.err.println("Failed to configure ZAP: " + e.getMessage());
        }

        // Configure RestAssured to use ZAP proxy
        RestAssuredConfig config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", 30000)
                        .setParam("http.socket.timeout", 30000));

        RestAssured.config = config;
        RestAssured.proxy(zapProxyHost, zapProxyPort);
        RestAssured.useRelaxedHTTPSValidation();

        System.out.println("✓ RestAssured configured to use ZAP proxy");
        System.out.println("═══════════════════════════════════════════════════════════════");
    }

    @AfterAll
    public static void saveFindings() {
        // Save custom findings to JSON file for report generation
        try {
            File findingsFile = new File(FINDINGS_FILE);
            findingsFile.getParentFile().mkdirs();

            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(findingsFile, customFindings);

            System.out.println("\n═══════════════════════════════════════════════════════════════");
            System.out.println("✓ Custom findings saved to: " + FINDINGS_FILE);
            System.out.println("✓ Total custom findings: " + customFindings.size());
            System.out.println("═══════════════════════════════════════════════════════════════\n");
        } catch (IOException e) {
            System.err.println("Failed to save findings: " + e.getMessage());
        }
    }

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    protected RequestSpecification getPickupRequestSpec() {
        return RestAssured.given()
                .header("accept", "application/json, text/plain, */*")
                .header("accept-language", "en")
                .header("content-type", "application/json; charset=utf-8")
                .header("origin", BUSINESS_BASE_URL)
                .header("referer", BUSINESS_BASE_URL + "/")
                .header("user-agent", USER_AGENT)
                .header("x-device-fingerprint", DEVICE_FINGERPRINT_1)
                .header("x-device-id", DEVICE_ID_1)
                .header("Authorization", PICKUP_AUTH_TOKEN);
    }

    protected RequestSpecification getBankInfoRequestSpec() {
        return RestAssured.given()
                .header("Accept", "application/json, text/plain, */*")
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Referer", BUSINESS_BASE_URL + "/")
                .header("x-device-id", DEVICE_ID_2)
                .header("X-DEVICE-FINGERPRINT", DEVICE_FINGERPRINT_2)
                .header("Authorization", "Bearer " + BANK_INFO_BEARER_TOKEN);
    }

    protected RequestSpecification getForgetPasswordRequestSpec() {
        return RestAssured.given()
                .header("accept", "application/json, text/plain, */*")
                .header("accept-language", "en")
                .header("content-type", "application/json; charset=utf-8")
                .header("origin", BUSINESS_BASE_URL)
                .header("referer", BUSINESS_BASE_URL + "/")
                .header("user-agent", USER_AGENT)
                .header("x-device-id", DEVICE_ID_1)
                .header("Authorization", PICKUP_AUTH_TOKEN);
    }

    protected void runActiveScan(String url) {
        try {
            System.out.println("Starting active scan for: " + url);
            String scanId = zapApi.ascan.scan(url, "True", "False", null, null, null).toString();

            // Wait for scan to complete
            int progress;
            do {
                Thread.sleep(2000);
                progress = Integer.parseInt(zapApi.ascan.status(scanId).toString());
                System.out.println("Scan progress: " + progress + "%");
            } while (progress < 100);

            System.out.println("Active scan completed for: " + url);
        } catch (Exception e) {
            System.err.println("Error during active scan: " + e.getMessage());
        }
    }

    protected void runSpiderScan(String url) {
        try {
            System.out.println("Starting spider scan for: " + url);
            String scanId = zapApi.spider.scan(url, null, null, null, null).toString();

            // Wait for scan to complete
            int progress;
            do {
                Thread.sleep(1000);
                progress = Integer.parseInt(zapApi.spider.status(scanId).toString());
                System.out.println("Spider progress: " + progress + "%");
            } while (progress < 100);

            System.out.println("Spider scan completed for: " + url);
        } catch (Exception e) {
            System.err.println("Error during spider scan: " + e.getMessage());
        }
    }

    protected void logSecurityIssue(String issue) {
        logSecurityIssue(issue, "HIGH", getCallingTestName());
    }

    protected void logSecurityIssue(String issue, String severity) {
        logSecurityIssue(issue, severity, getCallingTestName());
    }

    protected void logSecurityIssue(String issue, String severity, String testName) {
        SecurityFinding finding = new SecurityFinding();
        finding.issue = issue;
        finding.severity = severity;
        finding.testName = testName;
        finding.api = getApiName();
        finding.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        customFindings.add(finding);

        String severityIcon = getSeverityIcon(severity);
        System.err.println(severityIcon + " SECURITY ISSUE [" + severity + "]: " + issue);
    }

    private String getCallingTestName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getMethodName().startsWith("test") &&
                    !element.getClassName().equals(this.getClass().getName())) {
                return element.getMethodName();
            }
        }
        return "unknown";
    }

    private String getApiName() {
        String className = this.getClass().getSimpleName();
        if (className.contains("Pickup")) return "Create Pickup API";
        if (className.contains("BankInfo")) return "Update Bank Info API";
        if (className.contains("ForgetPassword")) return "Forget Password API";
        return "Unknown API";
    }

    private String getSeverityIcon(String severity) {
        switch (severity.toUpperCase()) {
            case "CRITICAL":
            case "HIGH":
                return "🔴";
            case "MEDIUM":
                return "🟡";
            case "LOW":
                return "🟢";
            default:
                return "🔵";
        }
    }

    protected String getPickupPayload() {
        return "{\n" +
                "  \"businessLocationId\": \"" + BUSINESS_LOCATION_ID + "\",\n" +
                "  \"contactPerson\": {\n" +
                "    \"_id\": \"" + CONTACT_PERSON_ID + "\",\n" +
                "    \"name\": \"" + TEST_NAME + "\",\n" +
                "    \"email\": \"" + TEST_EMAIL + "\",\n" +
                "    \"phone\": \"" + TEST_PHONE + "\"\n" +
                "  },\n" +
                "  \"scheduledDate\": \"2025-06-30\",\n" +
                "  \"numberOfParcels\": \"3\",\n" +
                "  \"hasBigItems\": false,\n" +
                "  \"repeatedData\": {\n" +
                "    \"repeatedType\": \"One Time\"\n" +
                "  },\n" +
                "  \"creationSrc\": \"Web\"\n" +
                "}";
    }

    protected String getBankInfoPayload() {
        return "{\n" +
                "  \"bankInfo\": {\n" +
                "    \"beneficiaryName\": \"" + TEST_NAME + "\",\n" +
                "    \"bankName\": \"NBG - البنك الأهلي المصري\",\n" +
                "    \"accountNumber\": \"123\",\n" +
                "    \"ibanNumber\": \"EG1234567890123456789012\"\n" +
                "  },\n" +
                "  \"paymentInfoOtp\": \"123\"\n" +
                "}";
    }

    protected String getForgetPasswordPayload() {
        return "{\n" +
                "  \"email\": \"" + TEST_EMAIL + "\"\n" +
                "}";
    }

    // Inner class for findings
    public static class SecurityFinding {
        public String issue;
        public String severity;
        public String testName;
        public String api;
        public String timestamp;

        public SecurityFinding() {}
    }
}