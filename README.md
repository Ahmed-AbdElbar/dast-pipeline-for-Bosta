# 🔒 DAST Security Pipeline for Bosta APIs

Automated Dynamic Application Security Testing (DAST) pipeline with GitHub Actions, OWASP ZAP, and comprehensive reporting.

## 📋 Overview

This project provides a fully automated security testing pipeline that:
- Tests 3 Bosta APIs for security vulnerabilities
- Uses OWASP ZAP for comprehensive scanning
- Implements custom security tests in Java
- Generates beautiful HTML/JSON/Markdown reports
- Deploys reports to GitHub Pages automatically
- Runs on every push, PR, or schedule

## 🎯 APIs Tested

1. **Create Pickup API** (`POST /api/v2/pickups`)
   - Creates pickup requests for delivery agents
   - Tests: Authentication, SQL Injection, XSS, IDOR, Input Validation

2. **Update Bank Info API** (`POST /api/v2/businesses/add-bank-info`)
   - Updates business bank account details
   - Tests: OTP Bypass, Token Validation, IBAN Validation, Rate Limiting

3. **Forget Password API** (`POST /api/v2/users/forget-password`)
   - Sends password reset links
   - Tests: Email Enumeration, Rate Limiting, SQL Injection, Account Lockout

## 🚀 Quick Start

### Prerequisites
- GitHub account
- Git installed locally

### Setup

1. **Clone this repository:**
```bash
git clone <your-repo-url>
cd dast-pipeline
```

2. **Push to GitHub:**
```bash
git init
git add .
git commit -m "Initial commit: DAST pipeline"
git remote add origin <your-github-repo-url>
git push -u origin main
```

3. **Enable GitHub Pages:**
   - Go to your repository Settings
   - Navigate to "Pages" section
   - Source: Select "gh-pages" branch
   - Click Save

4. **The pipeline runs automatically!**
   - On every push to main/develop
   - On every pull request
   - Daily at 2 AM UTC
   - Manually via "Actions" tab

## 📊 Viewing Reports

Reports are automatically deployed to GitHub Pages:
- Main Dashboard: `https://<your-username>.github.io/<repo-name>/reports/<run-number>/`
- HTML Report: `.../security-report.html`
- JSON Report: `.../security-report.json`
- Markdown Report: `.../security-report.md`
- ZAP Report: `.../zap-report.html`

## 🧪 Security Tests Performed

### Authentication & Authorization
- Missing authentication headers
- Invalid/expired tokens
- Token manipulation
- Role escalation attempts

### Injection Attacks
- SQL Injection (11 payloads)
- NoSQL Injection (5 payloads)
- Command Injection
- XSS (8 payloads)
- Path Traversal

### Business Logic
- IDOR (Insecure Direct Object Reference)
- Mass Assignment
- OTP Bypass
- Rate Limiting
- Email Enumeration
- Account Lockout

### Input Validation
- Excessive data
- Malformed JSON
- Special characters
- Invalid formats
- Missing required fields

### OWASP ZAP Scanning
- Spider scan
- Active scan
- Passive scan
- Automated vulnerability detection

## 📁 Project Structure

```
dast-pipeline/
├── .github/
│   └── workflows/
│       └── security-scan.yml          # GitHub Actions workflow
├── src/
│   ├── main/java/com/security/
│   │   ├── config/
│   │   │   └── ApiConfig.java         # API configurations & payloads
│   │   └── report/
│   │       └── ReportGenerator.java   # Report generation
│   └── test/java/com/security/tests/
│       ├── BaseSecurityTest.java      # Base test class with ZAP
│       ├── PickupApiSecurityTest.java # Pickup API tests
│       ├── BankInfoApiSecurityTest.java # Bank API tests
│       ├── ForgetPasswordApiSecurityTest.java # Password tests
│       └── SecurityTestSuite.java     # Test suite runner
├── pom.xml                            # Maven dependencies
└── README.md                          # This file
```

## 🔧 Configuration

### Update API Tokens

If tokens expire, update in `src/main/java/com/security/config/ApiConfig.java`:

```java
public static final String PICKUP_AUTH_TOKEN = "your-token-here";
public static final String BANK_INFO_BEARER_TOKEN = "your-bearer-token-here";
```

Or generate new token using the provided endpoint:
```bash
curl -X POST 'https://stg-app.bosta.co/api/v2/users/generate-token-for-interview-task'
```

### Modify Schedule

Edit `.github/workflows/security-scan.yml`:
```yaml
schedule:
  - cron: '0 2 * * *'  # Daily at 2 AM UTC
```

## 🛠️ Running Locally

### With Docker (Recommended)

```bash
# Start OWASP ZAP
docker run -d --name zap \
  -u zap \
  -p 8080:8080 \
  -v $(pwd):/zap/wrk/:rw \
  ghcr.io/zaproxy/zaproxy:stable \
  zap.sh -daemon -host 0.0.0.0 -port 8080 -config api.disablekey=true

# Wait for ZAP to start (30 seconds)
sleep 30

# Run tests
mvn clean test

# Generate reports
mvn exec:java -Dexec.mainClass="com.security.report.ReportGenerator"

# Stop ZAP
docker stop zap && docker rm zap
```

### Without Docker

1. Download and install [OWASP ZAP](https://www.zaproxy.org/download/)
2. Start ZAP in daemon mode on port 8080
3. Run: `mvn clean test`
4. Generate reports: `mvn exec:java -Dexec.mainClass="com.security.report.ReportGenerator"`

## 📈 Understanding Results

### Severity Levels

- **High/Critical**: Immediate attention required. Direct security risk.
- **Medium**: Significant issue. Should be addressed soon.
- **Low**: Minor issue. Can be addressed during regular maintenance.
- **Informational**: Best practice or configuration suggestion.

### Common Findings

1. **Missing Authentication**: API accepts requests without valid auth
2. **SQL Injection**: Database queries vulnerable to manipulation
3. **XSS**: User input not properly sanitized
4. **IDOR**: Can access/modify other users' data
5. **Rate Limiting**: No protection against brute force

## 🔒 Security Notes

- This pipeline uses real API endpoints and tokens
- Tokens are stored in code (staging environment only)
- All tests are non-destructive
- Tests follow ethical hacking principles
- For production, use GitHub Secrets for credentials

## 📝 Adding New Tests

1. Create new test class in `src/test/java/com/security/tests/`
2. Extend `BaseSecurityTest`
3. Add to `SecurityTestSuite.java`
4. Tests run automatically on next push

Example:
```java
@Test
@DisplayName("Test: Your Test Name")
public void testYourFeature() {
    Response response = getPickupRequestSpec()
        .body(payload)
        .post(ENDPOINT);
    
    if (response.getStatusCode() == 200) {
        logSecurityIssue("Vulnerability found!");
    }
}
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Add your security tests
4. Submit a pull request
5. Pipeline runs automatically on PR

## 📧 Support

For issues or questions:
- Open a GitHub Issue
- Check workflow logs in Actions tab
- Review generated reports

## 📜 License

MIT License - Feel free to use and modify

## 🎓 Learning Resources

- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [OWASP ZAP Documentation](https://www.zaproxy.org/docs/)
- [REST Assured Documentation](https://rest-assured.io/)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)

---

**⚠️ Disclaimer**: This tool is for authorized security testing only. Always obtain proper authorization before testing any system.
