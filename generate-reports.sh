#!/bin/bash

echo "=========================================="
echo "  Generating Security Reports"
echo "=========================================="
echo ""

# Check if ZAP is running
if ! curl -s http://localhost:8080 > /dev/null; then
    echo "⚠️  ZAP is not running, but we'll try to generate reports anyway"
    echo ""
fi

# Generate ZAP reports
if docker ps | grep -q zap; then
    echo "Generating ZAP reports..."
    
    docker exec zap zap-cli report -o /zap/wrk/reports/zap-report.html -f html 2>/dev/null || echo "  (ZAP HTML report generation skipped)"
    docker exec zap zap-cli report -o /zap/wrk/reports/zap-report.json -f json 2>/dev/null || echo "  (ZAP JSON report generation skipped)"
    docker exec zap zap-cli report -o /zap/wrk/reports/zap-report.xml -f xml 2>/dev/null || echo "  (ZAP XML report generation skipped)"
    
    echo "✓ ZAP reports generated"
    echo ""
fi

# Generate consolidated reports
echo "Generating consolidated reports..."
mvn exec:java -Dexec.mainClass="com.security.report.ReportGenerator"

if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "  ✓ Reports Generated Successfully!"
    echo "=========================================="
    echo ""
    echo "Reports available in ./reports/ directory:"
    echo "  • index.html           - Main dashboard"
    echo "  • security-report.html - Full HTML report"
    echo "  • security-report.json - JSON format"
    echo "  • security-report.md   - Markdown format"
    echo "  • zap-report.html      - ZAP scan results"
    echo ""
    echo "To view reports:"
    echo "  open reports/index.html"
    echo ""
    echo "Or on Linux:"
    echo "  xdg-open reports/index.html"
    echo ""
    echo "Or manually navigate to:"
    echo "  file://$(pwd)/reports/index.html"
    echo ""
else
    echo ""
    echo "❌ Report generation failed"
    exit 1
fi
