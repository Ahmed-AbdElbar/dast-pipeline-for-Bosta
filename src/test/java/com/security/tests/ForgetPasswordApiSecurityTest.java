package com.security.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static com.security.config.ApiConfig.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ForgetPasswordApiSecurityTest extends BaseSecurityTest {
    
    @Test
    @Order(1)
    @DisplayName("Test 1: Baseline Forget Password Request")
    public void testBaselineForgetPassword() {
        System.out.println("\n=== Testing Baseline Forget Password ===");
        
        Response response = getForgetPasswordRequestSpec()
            .body(getForgetPasswordPayload())
            .post(FORGET_PASSWORD_ENDPOINT);
        
        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Response: " + response.asString());
    }
    
    @Test
    @Order(2)
    @DisplayName("Test 2: Email Enumeration")
    public void testEmailEnumeration() {
        System.out.println("\n=== Testing Email Enumeration ===");
        
        String[] testEmails = {
            "nonexistent@bosta.co",
            "invalid@invalid.com",
            "admin@bosta.co",
            "test@test.com",
            TEST_EMAIL
        };
        
        for (String email : testEmails) {
            String payload = "{\"email\": \"" + email + "\"}";
            
            Response response = getForgetPasswordRequestSpec()
                .body(payload)
                .post(FORGET_PASSWORD_ENDPOINT);
            
            System.out.println("Email: " + email);
            System.out.println("Status: " + response.getStatusCode());
            System.out.println("Response length: " + response.asString().length());
            System.out.println("---");
        }
        
        System.out.println("⚠️ Check if responses differ for valid/invalid emails");
    }
    
    @Test
    @Order(3)
    @DisplayName("Test 3: Rate Limiting on Password Reset")
    public void testRateLimiting() {
        System.out.println("\n=== Testing Rate Limiting ===");
        
        int successfulRequests = 0;
        String payload = getForgetPasswordPayload();
        
        for (int i = 0; i < 50; i++) {
            Response response = getForgetPasswordRequestSpec()
                .body(payload)
                .post(FORGET_PASSWORD_ENDPOINT);
            
            if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
                successfulRequests++;
            }
            
            if (i % 10 == 0) {
                System.out.println("Requests sent: " + (i + 1));
            }
        }
        
        System.out.println("Successful requests: " + successfulRequests + "/50");
        
        if (successfulRequests > 10) {
            logSecurityIssue("Forget Password API: Insufficient rate limiting - " + successfulRequests + " requests succeeded");
        }
    }
    
    @Test
    @Order(4)
    @DisplayName("Test 4: SQL Injection in Email Field")
    public void testSqlInjection() {
        System.out.println("\n=== Testing SQL Injection ===");
        
        for (String sqlPayload : SQL_INJECTION_PAYLOADS) {
            String payload = "{\"email\": \"" + sqlPayload.replace("\"", "\\\"") + "\"}";
            
            Response response = getForgetPasswordRequestSpec()
                .body(payload)
                .post(FORGET_PASSWORD_ENDPOINT);
            
            String responseBody = response.asString().toLowerCase();
            
            if (responseBody.contains("sql") || 
                responseBody.contains("syntax") || 
                responseBody.contains("mysql") ||
                responseBody.contains("database") ||
                response.getStatusCode() == 500) {
                logSecurityIssue("Forget Password API: SQL Injection detected with: " + sqlPayload);
            }
        }
    }
    
    @Test
    @Order(5)
    @DisplayName("Test 5: Email Format Validation")
    public void testEmailValidation() {
        System.out.println("\n=== Testing Email Validation ===");
        
        String[] invalidEmails = {
            "notanemail",
            "@test.com",
            "test@",
            "test@@test.com",
            "test@.com",
            "<script>alert('xss')</script>@test.com",
            "test@test@test.com",
            "",
            "null",
            "../../../etc/passwd",
            "A".repeat(1000) + "@test.com"
        };
        
        for (String email : invalidEmails) {
            String payload = "{\"email\": \"" + email.replace("\"", "\\\"") + "\"}";
            
            Response response = getForgetPasswordRequestSpec()
                .body(payload)
                .post(FORGET_PASSWORD_ENDPOINT);
            
            System.out.println("Email: " + email + " -> Status: " + response.getStatusCode());
            
            if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
                logSecurityIssue("Forget Password API: Invalid email format accepted: " + email);
            }
        }
    }
    
    @Test
    @Order(6)
    @DisplayName("Test 6: NoSQL Injection in Email")
    public void testNoSqlInjection() {
        System.out.println("\n=== Testing NoSQL Injection ===");
        
        String[] nosqlPayloads = {
            "{\"$gt\":\"\"}",
            "{\"$ne\":null}",
            "{\"$regex\":\".*\"}",
            "[$ne]=1"
        };
        
        for (String nosqlPayload : nosqlPayloads) {
            String payload = "{\"email\": " + nosqlPayload + "}";
            
            Response response = getForgetPasswordRequestSpec()
                .body(payload)
                .post(FORGET_PASSWORD_ENDPOINT);
            
            System.out.println("NoSQL: " + nosqlPayload + " -> Status: " + response.getStatusCode());
            
            if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
                logSecurityIssue("Forget Password API: NoSQL injection successful with: " + nosqlPayload);
            }
        }
    }
    
    @Test
    @Order(7)
    @DisplayName("Test 7: Account Lockout Testing")
    public void testAccountLockout() {
        System.out.println("\n=== Testing Account Lockout ===");
        
        String payload = getForgetPasswordPayload();
        
        for (int i = 0; i < 20; i++) {
            Response response = getForgetPasswordRequestSpec()
                .body(payload)
                .post(FORGET_PASSWORD_ENDPOINT);
            
            System.out.println("Attempt " + (i + 1) + " -> Status: " + response.getStatusCode());
        }
        
        System.out.println("⚠️ Check if account gets locked after multiple password reset requests");
    }
    
    @Test
    @Order(8)
    @DisplayName("Test 8: Mass Password Reset Attack")
    public void testMassPasswordReset() {
        System.out.println("\n=== Testing Mass Password Reset ===");
        
        String[] targetEmails = {
            "admin@bosta.co",
            "support@bosta.co",
            "ceo@bosta.co",
            "info@bosta.co"
        };
        
        for (String email : targetEmails) {
            String payload = "{\"email\": \"" + email + "\"}";
            
            Response response = getForgetPasswordRequestSpec()
                .body(payload)
                .post(FORGET_PASSWORD_ENDPOINT);
            
            System.out.println("Target: " + email + " -> Status: " + response.getStatusCode());
        }
    }
    
    @Test
    @Order(9)
    @DisplayName("Test 9: Special Characters in Email")
    public void testSpecialCharactersInEmail() {
        System.out.println("\n=== Testing Special Characters ===");
        
        String[] specialEmails = {
            "test+tag@bosta.co",
            "test..test@bosta.co",
            "test@bosta..co",
            "test%40bosta.co",
            "test@bosta.co\r\nBCC:attacker@evil.com",
            "test@bosta.co\nX-Injected-Header: value"
        };
        
        for (String email : specialEmails) {
            String payload = "{\"email\": \"" + email.replace("\"", "\\\"") + "\"}";
            
            Response response = getForgetPasswordRequestSpec()
                .body(payload)
                .post(FORGET_PASSWORD_ENDPOINT);
            
            System.out.println("Special email: " + email + " -> Status: " + response.getStatusCode());
        }
    }

    @Test
    @Order(10)
    @DisplayName("Test: Missing Authorization Header")
    public void testMissingAuthorization() {
        System.out.println("\n=== Testing Missing Authorization ===");

        // Forget password should work WITHOUT auth - it's a public endpoint
        Response response = RestAssured.given()
                .header("accept", "application/json, text/plain, */*")
                .header("content-type", "application/json; charset=utf-8")
                .header("x-device-id", DEVICE_ID_1)
                .body(getForgetPasswordPayload())
                .post(BASE_URL + FORGET_PASSWORD_ENDPOINT);

        System.out.println("Status without auth: " + response.getStatusCode());

        // For forget password, 200/201 WITHOUT auth is EXPECTED
        if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
            System.out.println("✓ Forget password works without auth (correct behavior)");
        } else {
            logSecurityIssue("Forget Password API: Doesn't work without auth - this breaks the user flow!", "HIGH");
        }
    }
    
    @Test
    @Order(11)
    @DisplayName("Test 11: Content-Type Manipulation")
    public void testContentTypeManipulation() {
        System.out.println("\n=== Testing Content-Type Manipulation ===");
        
        String[] contentTypes = {
            "application/xml",
            "text/plain",
            "text/html",
            "multipart/form-data",
            ""
        };
        
        for (String contentType : contentTypes) {
            Response response = getForgetPasswordRequestSpec()
                .header("Content-Type", contentType)
                .body(getForgetPasswordPayload())
                .post(FORGET_PASSWORD_ENDPOINT);
            
            System.out.println("Content-Type: " + contentType + " -> Status: " + response.getStatusCode());
        }
    }
    
    @Test
    @Order(12)
    @DisplayName("Test 12: Parallel Password Reset Requests")
    public void testParallelRequests() {
        System.out.println("\n=== Testing Parallel Requests ===");
        
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                Response response = getForgetPasswordRequestSpec()
                    .body(getForgetPasswordPayload())
                    .post(FORGET_PASSWORD_ENDPOINT);
                System.out.println("Parallel request -> Status: " + response.getStatusCode());
            }).start();
        }
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    @Order(13)
    @DisplayName("Test 13: Run ZAP Active Scan")
    public void testZapActiveScan() {
        System.out.println("\n=== Running ZAP Active Scan ===");
        runActiveScan(BASE_URL + FORGET_PASSWORD_ENDPOINT);
    }
}
