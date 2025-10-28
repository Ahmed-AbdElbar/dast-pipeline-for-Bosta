# Security Testing Guide

## Understanding DAST (Dynamic Application Security Testing)

DAST is a "black-box" testing methodology that analyzes running applications to identify security vulnerabilities. Unlike static analysis, DAST tests the application from an attacker's perspective.

## Testing Methodology

### 1. Authentication & Authorization Testing

**What We Test:**
- Missing authentication headers
- Invalid tokens
- Expired tokens
- Token manipulation
- Role escalation

**Why It Matters:**
Authentication flaws are the #1 OWASP risk. They allow unauthorized access to sensitive functionality and data.

**How to Fix:**
- Always validate tokens server-side
- Implement proper session management
- Use strong, unique session identifiers
- Set appropriate token expiration times
- Validate user permissions for each request

### 2. Injection Vulnerabilities

**SQL Injection:**
- Tests if user input can manipulate database queries
- Example: `' OR '1'='1` can bypass authentication

**NoSQL Injection:**
- Similar to SQL injection but for NoSQL databases
- Example: `{"$ne": null}` can bypass filters

**Command Injection:**
- Tests if user input can execute system commands
- Example: `; ls -la` can list server directories

**How to Fix:**
- Use parameterized queries/prepared statements
- Validate and sanitize all user input
- Use ORM frameworks properly
- Implement input whitelisting
- Escape special characters

### 3. Cross-Site Scripting (XSS)

**What We Test:**
- Stored XSS (data saved to database)
- Reflected XSS (in URL parameters)
- DOM-based XSS (client-side manipulation)

**Example Payloads:**
```html
<script>alert('XSS')</script>
<img src=x onerror=alert('XSS')>
<svg onload=alert('XSS')>
```

**How to Fix:**
- Encode output (HTML, JavaScript, URL)
- Use Content Security Policy (CSP) headers
- Validate input format
- Use modern frameworks with auto-escaping
- Implement proper sanitization libraries

### 4. Insecure Direct Object Reference (IDOR)

**What We Test:**
- Can users access resources they shouldn't?
- Example: Changing `userId=123` to `userId=124`

**How to Fix:**
- Implement proper authorization checks
- Use indirect references (tokens instead of IDs)
- Validate user ownership of resources
- Use access control lists (ACLs)

### 5. Rate Limiting & Brute Force Protection

**What We Test:**
- Can we make unlimited requests?
- Is there OTP/password brute force protection?
- Are there account lockout mechanisms?

**How to Fix:**
- Implement rate limiting (e.g., 5 requests/minute)
- Add progressive delays after failures
- Use CAPTCHA after multiple attempts
- Monitor for suspicious patterns
- Implement account lockout policies

### 6. Input Validation

**What We Test:**
- Excessive data (buffer overflow)
- Malformed data
- Special characters
- Missing required fields
- Invalid formats (email, IBAN, phone)

**How to Fix:**
- Define strict input schemas
- Validate data type, length, format
- Reject invalid input early
- Use whitelist validation
- Sanitize user input

### 7. Business Logic Flaws

**What We Test:**
- Mass assignment vulnerabilities
- Price manipulation
- Workflow bypasses
- Parameter tampering

**How to Fix:**
- Never trust client-side data
- Validate business rules server-side
- Use explicit allow-lists for updates
- Implement proper state management
- Log and monitor unusual patterns

## Interpreting Results

### Severity Levels

**Critical/High:**
- Direct security risk
- Can lead to data breach
- Requires immediate action
- Examples: SQL Injection, Missing Auth

**Medium:**
- Significant security concern
- Could be exploited under certain conditions
- Should be fixed soon
- Examples: XSS, IDOR without sensitive data

**Low:**
- Minor security issue
- Difficult to exploit or low impact
- Fix during regular maintenance
- Examples: Information disclosure, weak headers

**Informational:**
- Best practice recommendations
- Configuration improvements
- Not a direct vulnerability
- Examples: Missing security headers, verbose errors

### Common False Positives

Some findings may not be actual vulnerabilities:
- **Info disclosure:** May be intentional (API documentation)
- **Missing headers:** May not apply to APIs
- **Low-severity XSS:** May be in admin-only areas

Always verify findings manually before fixing.

## Security Best Practices

### For APIs:

1. **Authentication:**
   - Use OAuth 2.0 or JWT tokens
   - Implement token refresh mechanisms
   - Store tokens securely
   - Use HTTPS only

2. **Authorization:**
   - Check permissions on every request
   - Implement role-based access control (RBAC)
   - Use principle of least privilege
   - Validate ownership of resources

3. **Input Handling:**
   - Validate all inputs server-side
   - Use strong typing
   - Implement request size limits
   - Sanitize and encode outputs

4. **Error Handling:**
   - Never expose stack traces
   - Use generic error messages
   - Log detailed errors server-side only
   - Implement proper exception handling

5. **Rate Limiting:**
   - Implement per-user limits
   - Use distributed rate limiting
   - Add exponential backoff
   - Monitor for abuse patterns

6. **Logging & Monitoring:**
   - Log authentication attempts
   - Monitor suspicious patterns
   - Set up alerts for anomalies
   - Retain logs for forensics

## Remediation Priority

### Priority 1 (Immediate - 0-7 days):
- Missing authentication
- SQL/NoSQL injection
- Exposed sensitive data
- Critical IDOR vulnerabilities

### Priority 2 (High - 7-30 days):
- XSS vulnerabilities
- Missing rate limiting on sensitive endpoints
- Token validation issues
- Weak session management

### Priority 3 (Medium - 30-90 days):
- Input validation issues
- Information disclosure
- Missing security headers
- Business logic flaws

### Priority 4 (Low - 90+ days):
- Informational findings
- Best practice improvements
- Non-critical configuration issues

## Testing Frequency

- **Critical systems:** Weekly automated + Monthly manual
- **User-facing APIs:** Bi-weekly automated + Quarterly manual
- **Internal APIs:** Monthly automated + Semi-annual manual
- **After changes:** Always test before deployment

## Compliance & Standards

This testing covers requirements from:
- OWASP Top 10 (2021)
- OWASP API Security Top 10
- PCI DSS (if handling payments)
- GDPR (data protection)
- ISO 27001 (information security)

## Additional Resources

- **OWASP Testing Guide:** https://owasp.org/www-project-web-security-testing-guide/
- **OWASP API Security:** https://owasp.org/www-project-api-security/
- **CWE (Common Weakness Enumeration):** https://cwe.mitre.org/
- **NIST Cybersecurity Framework:** https://www.nist.gov/cyberframework

## Need Help?

If you find a vulnerability:
1. Document the vulnerability details
2. Create a proof-of-concept (PoC)
3. Assess the risk and impact
4. Prioritize based on severity
5. Develop and test the fix
6. Deploy and verify
7. Update documentation

Remember: Security is an ongoing process, not a one-time check!
