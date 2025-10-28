# 🚀 Quick Start Guide

Get your DAST pipeline running in 3 minutes!

## Option 1: Automated (Recommended)

```bash
# Make scripts executable
chmod +x *.sh

# Run everything at once
./quick-start.sh

# View reports
open reports/index.html  # macOS
xdg-open reports/index.html  # Linux
start reports/index.html  # Windows
```

## Option 2: Step by Step

### 1. Setup Environment
```bash
./setup.sh
```
This will:
- Check prerequisites (Docker, Maven, Java)
- Build the project
- Start OWASP ZAP
- Wait for ZAP to be ready

### 2. Run Security Tests
```bash
./run-tests.sh
```
This runs all security tests against the 3 APIs

### 3. Generate Reports
```bash
./generate-reports.sh
```
This creates HTML, JSON, and Markdown reports

### 4. View Reports
```bash
open reports/index.html
```

### 5. Cleanup (When Done)
```bash
./cleanup.sh
```

## Option 3: GitHub Actions (Zero Setup)

1. **Push to GitHub:**
```bash
git init
git add .
git commit -m "Initial commit"
git remote add origin YOUR_REPO_URL
git push -u origin main
```

2. **Enable GitHub Pages:**
   - Go to Settings → Pages
   - Source: gh-pages branch
   - Save

3. **View Results:**
   - Pipeline runs automatically
   - Reports at: `https://YOUR_USERNAME.github.io/YOUR_REPO/reports/RUN_NUMBER/`

## What Gets Tested?

✅ **Authentication & Authorization**
- Missing tokens
- Invalid tokens
- Expired tokens
- Token manipulation

✅ **Injection Attacks**
- SQL Injection (11 payloads)
- NoSQL Injection
- Command Injection
- XSS (8 payloads)

✅ **Business Logic**
- IDOR vulnerabilities
- Mass assignment
- OTP bypass
- Rate limiting

✅ **Input Validation**
- Email validation
- IBAN validation
- Excessive data
- Malformed input

✅ **OWASP ZAP Scanning**
- Spider scan
- Active scan
- Automated vulnerability detection

## Understanding Results

### Report Files:
- `index.html` - Dashboard with all reports
- `security-report.html` - Full detailed findings
- `security-report.json` - Machine-readable format
- `security-report.md` - Documentation format
- `zap-report.html` - OWASP ZAP scan results

### Severity Badges:
- 🔴 **Critical/High** - Fix immediately
- 🟡 **Medium** - Fix soon
- 🟢 **Low** - Fix during maintenance
- 🔵 **Info** - Best practices

## Troubleshooting

### "Docker not found"
Install Docker: https://docs.docker.com/get-docker/

### "Maven not found"
Install Maven: https://maven.apache.org/install.html

### "Java version too old"
Install Java 17+: https://adoptium.net/

### "ZAP won't start"
```bash
docker stop zap && docker rm zap
./setup.sh
```

### "Port 8080 already in use"
```bash
# Find and kill process using port 8080
lsof -ti:8080 | xargs kill -9
# Or change ZAP port in setup.sh
```

## Manual Testing (No Docker)

1. Download OWASP ZAP: https://www.zaproxy.org/download/
2. Start ZAP in daemon mode on port 8080
3. Run: `mvn clean test`
4. Generate reports: `mvn exec:java -Dexec.mainClass="com.security.report.ReportGenerator"`

## Next Steps

1. Review the security reports
2. Read `SECURITY_GUIDE.md` for remediation advice
3. Prioritize findings by severity
4. Fix vulnerabilities
5. Re-run tests to verify fixes

## Support

- 📖 Full documentation: `README.md`
- 🔒 Security guide: `SECURITY_GUIDE.md`
- 🐛 Issues: Create a GitHub issue
- 📊 Reports not generating? Check `target/surefire-reports/` for test logs

## Tips

- Tests will find issues - that's the point!
- Some "failures" are expected (security tests)
- Review findings before fixing
- False positives are normal
- Re-run after each fix

---

**Happy Security Testing! 🔒**
