package com.security.config;

public class ApiConfig {
    
    // Base URLs
    public static final String BASE_URL = "https://stg-app.bosta.co/api/v2";
    public static final String BUSINESS_BASE_URL = "https://stg-business.bosta.co";
    
    // API Endpoints
    public static final String PICKUPS_ENDPOINT = "/pickups";
    public static final String BANK_INFO_ENDPOINT = "/businesses/add-bank-info";
    public static final String FORGET_PASSWORD_ENDPOINT = "/users/forget-password";
    public static final String GENERATE_TOKEN_ENDPOINT = "/users/generate-token-for-interview-task";
    
    // Authentication Tokens
    public static final String PICKUP_AUTH_TOKEN = "bca27763f5f30353ba0ee3d2ebd8951994f5016e269bbd781798e2884274d631";
    public static final String BANK_INFO_BEARER_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6InVydmIxOWNKT0E4M044eHY2d0lzUCIsInJvbGVzIjpbIkJVU0lORVNTX0FETUlOIl0sImJ1c2luZXNzQWRtaW5JbmZvIjp7ImJ1c2luZXNzSWQiOiI2U2xuRmpodjVPRWFESWFBZWVjdGciLCJidXNpbmVzc05hbWUiOiJ0ZXN0IGNvbXBhbnkgbmFtZSJ9LCJlbWFpbCI6ImFtaXJhLm1vc2ErOTkxQGJvc3RhLmNvIiwicGhvbmUiOiIrMjAxMDU1NTkyODI5IiwiY291bnRyeSI6eyJfaWQiOiI2MGU0NDgyYzdjYjdkNGJjNDg0OWM0ZDUiLCJuYW1lIjoiRWd5cHQiLCJuYW1lQXIiOiLZhdi12LEiLCJjb2RlIjoiRUcifSwidG9rZW5UeXBlIjoiQUNDRVNTIiwidG9rZW5WZXJzaW9uIjoiVjIiLCJzZXNzaW9uSWQiOiIwMUs4TTdSOEJHNFFGTVEwTjUwQkZYVkZUVCIsImlhdCI6MTc2MTYxNTgxNSwiZXhwIjoxNzYyODI1NDE1fQ.v-yp1hWozOsQoNKX0wcWhMMONzBUKt0CnvdrpsyuDl8";
    
    // Device Information
    public static final String DEVICE_ID_1 = "01JV70TKSFGV9Z1QWEYV3N5APC";
    public static final String DEVICE_ID_2 = "01K0ZH74759AR8ER1NZSS478R6";
    public static final String DEVICE_FINGERPRINT_1 = "1hgtilh";
    public static final String DEVICE_FINGERPRINT_2 = "1iwjpzb";
    
    // Test Data
    public static final String BUSINESS_LOCATION_ID = "MFqXsoFhxO";
    public static final String CONTACT_PERSON_ID = "_sCFBrHGi";
    public static final String TEST_EMAIL = "amira.mosa991@bosta.co";
    public static final String TEST_PHONE = "+201055592829";
    public static final String TEST_NAME = "test name";
    
    // User Agent
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36";
    
    // Security Testing Payloads
    public static final String[] SQL_INJECTION_PAYLOADS = {
        "' OR '1'='1",
        "'; DROP TABLE users--",
        "' OR 1=1--",
        "admin'--",
        "' OR 'x'='x",
        "1' UNION SELECT NULL--",
        "' AND 1=0 UNION ALL SELECT 'admin', '81dc9bdb52d04dc20036dbd8313ed055'",
        "1' ORDER BY 1--+",
        "1' ORDER BY 2--+",
        "1' ORDER BY 3--+",
        "1' UNION SELECT NULL, NULL, NULL--"
    };
    
    public static final String[] XSS_PAYLOADS = {
        "<script>alert('XSS')</script>",
        "<img src=x onerror=alert('XSS')>",
        "<svg onload=alert('XSS')>",
        "javascript:alert('XSS')",
        "<body onload=alert('XSS')>",
        "<iframe src='javascript:alert(\"XSS\")'></iframe>",
        "<input type=\"text\" value=\"\" onfocus=\"alert('XSS')\">",
        "<marquee onstart=alert('XSS')></marquee>"
    };
    
    public static final String[] COMMAND_INJECTION_PAYLOADS = {
        "; ls -la",
        "| cat /etc/passwd",
        "&& whoami",
        "; cat /etc/shadow",
        "| id",
        "; pwd",
        "&& uname -a"
    };
    
    public static final String[] PATH_TRAVERSAL_PAYLOADS = {
        "../../../etc/passwd",
        "..\\..\\..\\windows\\system32\\config\\sam",
        "....//....//....//etc/passwd",
        "..%2F..%2F..%2Fetc%2Fpasswd",
        "..%252f..%252f..%252fetc%252fpasswd"
    };
    
    public static final String[] NOSQL_INJECTION_PAYLOADS = {
        "{'$gt':''}",
        "{'$ne':null}",
        "{'$regex':'.*'}",
        "{$where: '1==1'}",
        "[$ne]=1"
    };
}
