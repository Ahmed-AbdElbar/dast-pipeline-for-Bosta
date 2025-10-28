package com.security.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static com.security.config.ApiConfig.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BankInfoApiSecurityTest extends BaseSecurityTest {
    
    @Test
    @Order(1)
    @DisplayName("Test 1: Baseline Bank Info Update")
    public void testBaselineBankInfoUpdate() {
        System.out.println("\n=== Testing Baseline Bank Info Update ===");
        
        Response response = getBankInfoRequestSpec()
            .body(getBankInfoPayload())
            .post(BANK_INFO_ENDPOINT);
        
        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Response: " + response.asString());
    }
    
    @Test
    @Order(2)
    @DisplayName("Test 2: Missing Bearer Token")
    public void testMissingBearerToken() {
        System.out.println("\n=== Testing Missing Bearer Token ===");

        Response response = RestAssured.given()
                .header("Accept", "application/json, text/plain, */*")
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Referer", BUSINESS_BASE_URL + "/")
                .header("x-device-id", DEVICE_ID_2)
                .header("X-DEVICE-FINGERPRINT", DEVICE_FINGERPRINT_2)
                // No Authorization header
                .body(getBankInfoPayload())
                .post(BASE_URL + BANK_INFO_ENDPOINT);
        
        System.out.println("Status: " + response.getStatusCode());
        
        if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
            logSecurityIssue("Bank Info API: Missing authentication allows access!");
        } else {
            System.out.println("✓ Properly rejects missing authentication");
        }
    }
    
    @Test
    @Order(3)
    @DisplayName("Test 3: Expired Token Handling")
    public void testExpiredToken() {
        System.out.println("\n=== Testing Expired Token ===");
        
        String expiredToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6InRlc3QiLCJleHAiOjF9.test";
        
        Response response = getBankInfoRequestSpec()
            .header("Authorization", "Bearer " + expiredToken)
            .body(getBankInfoPayload())
            .post(BANK_INFO_ENDPOINT);
        
        System.out.println("Status: " + response.getStatusCode());
        
        if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
            logSecurityIssue("Bank Info API: Expired token still allows access!");
        }
    }
    
    @Test
    @Order(4)
    @DisplayName("Test 4: Invalid OTP Bypass")
    public void testInvalidOtpBypass() {
        System.out.println("\n=== Testing OTP Bypass ===");
        
        String[] invalidOtps = {
            "000",
            "999",
            "111",
            "abc",
            "",
            "null",
            "undefined",
            "' OR '1'='1",
            "123456",
            "0000"
        };
        
        for (String otp : invalidOtps) {
            String payload = "{\n" +
                "  \"bankInfo\": {\n" +
                "    \"beneficiaryName\": \"test name\",\n" +
                "    \"bankName\": \"NBG\",\n" +
                "    \"accountNumber\": \"123\",\n" +
                "    \"ibanNumber\": \"EG1234567890123456789012\"\n" +
                "  },\n" +
                "  \"paymentInfoOtp\": \"" + otp + "\"\n" +
                "}";
            
            Response response = getBankInfoRequestSpec()
                .body(payload)
                .post(BANK_INFO_ENDPOINT);
            
            System.out.println("OTP: " + otp + " -> Status: " + response.getStatusCode());
            
            if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
                logSecurityIssue("Bank Info API: Invalid OTP '" + otp + "' allows bank info update!");
            }
        }
    }
    
    @Test
    @Order(5)
    @DisplayName("Test 5: Missing OTP Field")
    public void testMissingOtp() {
        System.out.println("\n=== Testing Missing OTP ===");
        
        String payload = "{\n" +
            "  \"bankInfo\": {\n" +
            "    \"beneficiaryName\": \"test name\",\n" +
            "    \"bankName\": \"NBG\",\n" +
            "    \"accountNumber\": \"123\",\n" +
            "    \"ibanNumber\": \"EG1234567890123456789012\"\n" +
            "  }\n" +
            "}";
        
        Response response = getBankInfoRequestSpec()
            .body(payload)
            .post(BANK_INFO_ENDPOINT);
        
        if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
            logSecurityIssue("Bank Info API: Can update bank info without OTP!");
        }
    }
    
    @Test
    @Order(6)
    @DisplayName("Test 6: SQL Injection in Bank Fields")
    public void testSqlInjectionInBankFields() {
        System.out.println("\n=== Testing SQL Injection in Bank Fields ===");
        
        for (String payload : SQL_INJECTION_PAYLOADS) {
            String sqlPayload = "{\n" +
                "  \"bankInfo\": {\n" +
                "    \"beneficiaryName\": \"" + payload.replace("\"", "\\\"") + "\",\n" +
                "    \"bankName\": \"" + payload.replace("\"", "\\\"") + "\",\n" +
                "    \"accountNumber\": \"" + payload.replace("\"", "\\\"") + "\",\n" +
                "    \"ibanNumber\": \"" + payload.replace("\"", "\\\"") + "\"\n" +
                "  },\n" +
                "  \"paymentInfoOtp\": \"123\"\n" +
                "}";
            
            Response response = getBankInfoRequestSpec()
                .body(sqlPayload)
                .post(BANK_INFO_ENDPOINT);
            
            String responseBody = response.asString().toLowerCase();
            
            if (responseBody.contains("sql") || 
                responseBody.contains("syntax") || 
                responseBody.contains("mysql") ||
                response.getStatusCode() == 500) {
                logSecurityIssue("Bank Info API: SQL Injection vulnerability detected");
            }
        }
    }
    
    @Test
    @Order(7)
    @DisplayName("Test 7: IBAN Validation Bypass")
    public void testIbanValidationBypass() {
        System.out.println("\n=== Testing IBAN Validation ===");
        
        String[] invalidIbans = {
            "INVALID",
            "123",
            "EG123",
            "AA1234567890123456789012",
            "EG999999999999999999999999999999",
            "",
            "../../../etc/passwd",
            "<script>alert('xss')</script>",
            "' OR '1'='1"
        };
        
        for (String iban : invalidIbans) {
            String payload = "{\n" +
                "  \"bankInfo\": {\n" +
                "    \"beneficiaryName\": \"test\",\n" +
                "    \"bankName\": \"NBG\",\n" +
                "    \"accountNumber\": \"123\",\n" +
                "    \"ibanNumber\": \"" + iban.replace("\"", "\\\"") + "\"\n" +
                "  },\n" +
                "  \"paymentInfoOtp\": \"123\"\n" +
                "}";
            
            Response response = getBankInfoRequestSpec()
                .body(payload)
                .post(BANK_INFO_ENDPOINT);
            
            if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
                logSecurityIssue("Bank Info API: Invalid IBAN accepted: " + iban);
            }
        }
    }
    
    @Test
    @Order(8)
    @DisplayName("Test 8: XSS in Bank Fields")
    public void testXssInBankFields() {
        System.out.println("\n=== Testing XSS in Bank Fields ===");
        
        for (String xss : XSS_PAYLOADS) {
            String payload = "{\n" +
                "  \"bankInfo\": {\n" +
                "    \"beneficiaryName\": \"" + xss.replace("\"", "\\\"") + "\",\n" +
                "    \"bankName\": \"NBG\",\n" +
                "    \"accountNumber\": \"123\",\n" +
                "    \"ibanNumber\": \"EG1234567890123456789012\"\n" +
                "  },\n" +
                "  \"paymentInfoOtp\": \"123\"\n" +
                "}";
            
            Response response = getBankInfoRequestSpec()
                .body(payload)
                .post(BANK_INFO_ENDPOINT);
            
            String responseBody = response.asString();
            
            if (responseBody.contains("<script>") || 
                responseBody.contains("onerror=")) {
                logSecurityIssue("Bank Info API: XSS vulnerability in beneficiary name");
            }
        }
    }
    
    @Test
    @Order(9)
    @DisplayName("Test 9: Rate Limiting on OTP Validation")
    public void testRateLimitingOtp() {
        System.out.println("\n=== Testing Rate Limiting ===");
        
        int successfulRequests = 0;
        
        for (int i = 0; i < 100; i++) {
            Response response = getBankInfoRequestSpec()
                .body(getBankInfoPayload())
                .post(BANK_INFO_ENDPOINT);
            
            if (response.getStatusCode() != 429) {
                successfulRequests++;
            }
            
            if (i % 20 == 0) {
                System.out.println("Requests sent: " + (i + 1));
            }
        }
        
        System.out.println("Successful requests: " + successfulRequests + "/100");
        
        if (successfulRequests > 20) {
            logSecurityIssue("Bank Info API: Insufficient rate limiting - " + successfulRequests + " requests succeeded");
        }
    }
    
    @Test
    @Order(10)
    @DisplayName("Test 10: Token Manipulation - Change User ID")
    public void testTokenManipulation() {
        System.out.println("\n=== Testing Token Manipulation ===");
        
        String[] manipulatedTokens = {
            BANK_INFO_BEARER_TOKEN.replace("BUSINESS_ADMIN", "SUPER_ADMIN"),
            BANK_INFO_BEARER_TOKEN.substring(0, BANK_INFO_BEARER_TOKEN.length() - 10) + "1234567890",
            BANK_INFO_BEARER_TOKEN.replace("business", "admin")
        };
        
        for (String token : manipulatedTokens) {
            Response response = getBankInfoRequestSpec()
                .header("Authorization", "Bearer " + token)
                .body(getBankInfoPayload())
                .post(BANK_INFO_ENDPOINT);
            
            if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
                logSecurityIssue("Bank Info API: Manipulated token accepted!");
            }
        }
    }
    
    @Test
    @Order(11)
    @DisplayName("Test 11: Mass Assignment - Add Extra Fields")
    public void testMassAssignment() {
        System.out.println("\n=== Testing Mass Assignment ===");
        
        String payload = "{\n" +
            "  \"bankInfo\": {\n" +
            "    \"beneficiaryName\": \"test\",\n" +
            "    \"bankName\": \"NBG\",\n" +
            "    \"accountNumber\": \"123\",\n" +
            "    \"ibanNumber\": \"EG1234567890123456789012\"\n" +
            "  },\n" +
            "  \"paymentInfoOtp\": \"123\",\n" +
            "  \"isAdmin\": true,\n" +
            "  \"role\": \"ADMIN\",\n" +
            "  \"verified\": true,\n" +
            "  \"balance\": 999999\n" +
            "}";
        
        Response response = getBankInfoRequestSpec()
            .body(payload)
            .post(BANK_INFO_ENDPOINT);
        
        System.out.println("Mass assignment test -> Status: " + response.getStatusCode());
    }
    
    @Test
    @Order(12)
    @DisplayName("Test 12: Run ZAP Active Scan")
    public void testZapActiveScan() {
        System.out.println("\n=== Running ZAP Active Scan ===");
        runActiveScan(BASE_URL + BANK_INFO_ENDPOINT);
    }
}
