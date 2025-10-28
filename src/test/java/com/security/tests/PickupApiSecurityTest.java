package com.security.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static com.security.config.ApiConfig.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PickupApiSecurityTest extends BaseSecurityTest {
    
    @Test
    @Order(1)
    @DisplayName("Test 1: Baseline Pickup Request")
    public void testBaselinePickupRequest() {
        System.out.println("\n=== Testing Baseline Pickup Request ===");
        
        Response response = getPickupRequestSpec()
            .body(getPickupPayload())
            .post(PICKUPS_ENDPOINT);
        
        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Response: " + response.asString());
        
        if (response.getStatusCode() < 500) {
            System.out.println("✓ Baseline request successful");
        }
    }
    
    @Test
    @Order(2)
    @DisplayName("Test 2: Missing Authorization Header")
    public void testMissingAuthorizationHeader() {
        System.out.println("\n=== Testing Missing Authorization ===");

        Response response = RestAssured.given()
                .header("accept", "application/json, text/plain, */*")
                .header("accept-language", "en")
                .header("content-type", "application/json; charset=utf-8")
                .header("origin", BUSINESS_BASE_URL)
                .header("referer", BUSINESS_BASE_URL + "/")
                .header("user-agent", USER_AGENT)
                .header("x-device-fingerprint", DEVICE_FINGERPRINT_1)
                .header("x-device-id", DEVICE_ID_1)
                // No Authorization header
                .body(getPickupPayload())
                .post(BASE_URL + PICKUPS_ENDPOINT);
        
        System.out.println("Status: " + response.getStatusCode());
        
        if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
            logSecurityIssue("Pickup API: Missing authentication allows access!");
        } else {
            System.out.println("✓ Properly rejects missing authentication");
        }
    }
    
    @Test
    @Order(3)
    @DisplayName("Test 3: Invalid Authorization Token")
    public void testInvalidAuthorizationToken() {
        System.out.println("\n=== Testing Invalid Authorization Token ===");
        
        String[] invalidTokens = {
            "invalid_token_123",
            "Bearer invalid_token",
            "",
            "null",
            "undefined"
        };
        
        for (String token : invalidTokens) {
            Response response = getPickupRequestSpec()
                .header("Authorization", token)
                .body(getPickupPayload())
                .post(PICKUPS_ENDPOINT);
            
            System.out.println("Token: " + token + " -> Status: " + response.getStatusCode());
            
            if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
                logSecurityIssue("Pickup API: Invalid token '" + token + "' allows access!");
            }
        }
    }
    
    @Test
    @Order(4)
    @DisplayName("Test 4: SQL Injection in Contact Person Fields")
    public void testSqlInjectionInFields() {
        System.out.println("\n=== Testing SQL Injection ===");
        
        for (String payload : SQL_INJECTION_PAYLOADS) {
            String maliciousPayload = "{\n" +
                "  \"businessLocationId\": \"" + payload + "\",\n" +
                "  \"contactPerson\": {\n" +
                "    \"_id\": \"" + payload + "\",\n" +
                "    \"name\": \"" + payload + "\",\n" +
                "    \"email\": \"test@test.com\",\n" +
                "    \"phone\": \"+201234567890\"\n" +
                "  },\n" +
                "  \"scheduledDate\": \"2025-06-30\",\n" +
                "  \"numberOfParcels\": \"3\",\n" +
                "  \"hasBigItems\": false,\n" +
                "  \"repeatedData\": {\"repeatedType\": \"One Time\"},\n" +
                "  \"creationSrc\": \"Web\"\n" +
                "}";
            
            Response response = getPickupRequestSpec()
                .body(maliciousPayload)
                .post(PICKUPS_ENDPOINT);
            
            String responseBody = response.asString().toLowerCase();
            
            if (responseBody.contains("sql") || 
                responseBody.contains("syntax") || 
                responseBody.contains("mysql") ||
                responseBody.contains("database") ||
                response.getStatusCode() == 500) {
                logSecurityIssue("Pickup API: SQL Injection vulnerability detected with payload: " + payload);
            }
        }
    }
    
    @Test
    @Order(5)
    @DisplayName("Test 5: XSS in Contact Person Fields")
    public void testXssInFields() {
        System.out.println("\n=== Testing XSS Vulnerabilities ===");
        
        for (String payload : XSS_PAYLOADS) {
            String xssPayload = "{\n" +
                "  \"businessLocationId\": \"MFqXsoFhxO\",\n" +
                "  \"contactPerson\": {\n" +
                "    \"_id\": \"_sCFBrHGi\",\n" +
                "    \"name\": \"" + payload.replace("\"", "\\\"") + "\",\n" +
                "    \"email\": \"test@test.com\",\n" +
                "    \"phone\": \"+201234567890\"\n" +
                "  },\n" +
                "  \"scheduledDate\": \"2025-06-30\",\n" +
                "  \"numberOfParcels\": \"3\",\n" +
                "  \"hasBigItems\": false,\n" +
                "  \"repeatedData\": {\"repeatedType\": \"One Time\"},\n" +
                "  \"creationSrc\": \"Web\"\n" +
                "}";
            
            Response response = getPickupRequestSpec()
                .body(xssPayload)
                .post(PICKUPS_ENDPOINT);
            
            String responseBody = response.asString();
            
            if (responseBody.contains("<script>") || 
                responseBody.contains("onerror=") ||
                responseBody.contains("onload=")) {
                logSecurityIssue("Pickup API: XSS vulnerability detected - unescaped script in response");
            }
        }
    }
    
    @Test
    @Order(6)
    @DisplayName("Test 6: NoSQL Injection")
    public void testNoSqlInjection() {
        System.out.println("\n=== Testing NoSQL Injection ===");
        
        for (String payload : NOSQL_INJECTION_PAYLOADS) {
            String nosqlPayload = "{\n" +
                "  \"businessLocationId\": " + payload + ",\n" +
                "  \"contactPerson\": {\n" +
                "    \"_id\": \"_sCFBrHGi\",\n" +
                "    \"name\": \"test\",\n" +
                "    \"email\": \"test@test.com\",\n" +
                "    \"phone\": \"+201234567890\"\n" +
                "  },\n" +
                "  \"scheduledDate\": \"2025-06-30\",\n" +
                "  \"numberOfParcels\": \"3\",\n" +
                "  \"hasBigItems\": false,\n" +
                "  \"repeatedData\": {\"repeatedType\": \"One Time\"},\n" +
                "  \"creationSrc\": \"Web\"\n" +
                "}";
            
            Response response = getPickupRequestSpec()
                .body(nosqlPayload)
                .post(PICKUPS_ENDPOINT);
            
            System.out.println("NoSQL Payload: " + payload + " -> Status: " + response.getStatusCode());
        }
    }
    
    @Test
    @Order(7)
    @DisplayName("Test 7: Excessive Data in Request")
    public void testExcessiveData() {
        System.out.println("\n=== Testing Excessive Data ===");
        
        String largeString = "A".repeat(10000);
        String excessivePayload = "{\n" +
            "  \"businessLocationId\": \"" + largeString + "\",\n" +
            "  \"contactPerson\": {\n" +
            "    \"_id\": \"" + largeString + "\",\n" +
            "    \"name\": \"" + largeString + "\",\n" +
            "    \"email\": \"test@test.com\",\n" +
            "    \"phone\": \"+201234567890\"\n" +
            "  },\n" +
            "  \"scheduledDate\": \"2025-06-30\",\n" +
            "  \"numberOfParcels\": \"999999\",\n" +
            "  \"hasBigItems\": false,\n" +
            "  \"repeatedData\": {\"repeatedType\": \"One Time\"},\n" +
            "  \"creationSrc\": \"Web\"\n" +
            "}";
        
        Response response = getPickupRequestSpec()
            .body(excessivePayload)
            .post(PICKUPS_ENDPOINT);
        
        if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
            logSecurityIssue("Pickup API: No input length validation - accepts excessive data");
        }
    }
    
    @Test
    @Order(8)
    @DisplayName("Test 8: Malformed JSON")
    public void testMalformedJson() {
        System.out.println("\n=== Testing Malformed JSON ===");
        
        String[] malformedPayloads = {
            "{invalid json}",
            "{\"businessLocationId\": }",
            "{\"test\": \"unclosed",
            "null",
            "[]",
            ""
        };
        
        for (String payload : malformedPayloads) {
            Response response = getPickupRequestSpec()
                .body(payload)
                .post(PICKUPS_ENDPOINT);
            
            System.out.println("Malformed payload -> Status: " + response.getStatusCode());
            
            String responseBody = response.asString().toLowerCase();
            if (responseBody.contains("error") && 
                (responseBody.contains("stack") || responseBody.contains("trace"))) {
                logSecurityIssue("Pickup API: Stack trace exposed in error response");
            }
        }
    }
    
    @Test
    @Order(9)
    @DisplayName("Test 9: IDOR - Unauthorized Business Location Access")
    public void testIdorBusinessLocation() {
        System.out.println("\n=== Testing IDOR on Business Location ===");
        
        String[] otherBusinessIds = {
            "AAAAAAAAAA",
            "0000000000",
            "9999999999",
            "../../../admin",
            "admin",
            "test123"
        };
        
        for (String businessId : otherBusinessIds) {
            String idorPayload = "{\n" +
                "  \"businessLocationId\": \"" + businessId + "\",\n" +
                "  \"contactPerson\": {\n" +
                "    \"_id\": \"_sCFBrHGi\",\n" +
                "    \"name\": \"test\",\n" +
                "    \"email\": \"test@test.com\",\n" +
                "    \"phone\": \"+201234567890\"\n" +
                "  },\n" +
                "  \"scheduledDate\": \"2025-06-30\",\n" +
                "  \"numberOfParcels\": \"3\",\n" +
                "  \"hasBigItems\": false,\n" +
                "  \"repeatedData\": {\"repeatedType\": \"One Time\"},\n" +
                "  \"creationSrc\": \"Web\"\n" +
                "}";
            
            Response response = getPickupRequestSpec()
                .body(idorPayload)
                .post(PICKUPS_ENDPOINT);
            
            if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
                logSecurityIssue("Pickup API: IDOR vulnerability - can create pickup for business ID: " + businessId);
            }
        }
    }
    
    @Test
    @Order(10)
    @DisplayName("Test 10: Run ZAP Active Scan")
    public void testZapActiveScan() {
        System.out.println("\n=== Running ZAP Active Scan ===");
        runActiveScan(BASE_URL + PICKUPS_ENDPOINT);
    }
}
