#!/bin/bash

echo "=========================================="
echo "  Running Security Tests"
echo "=========================================="
echo ""

# Check if ZAP is running
if ! curl -s http://localhost:8080 > /dev/null; then
    echo "❌ ZAP is not running. Please run ./setup.sh first"
    exit 1
fi

echo "✓ ZAP is running"
echo ""

# Run tests
echo "Executing security tests..."
echo ""
mvn test -Dtest=SecurityTestSuite

TEST_RESULT=$?

echo ""
if [ $TEST_RESULT -eq 0 ]; then
    echo "=========================================="
    echo "  ✓ Tests completed successfully!"
    echo "=========================================="
else
    echo "=========================================="
    echo "  ⚠️  Tests completed with some failures"
    echo "  (This is normal for security testing)"
    echo "=========================================="
fi

echo ""
echo "Next steps:"
echo "  1. Generate reports: ./generate-reports.sh"
echo "  2. View reports: open reports/index.html"
echo ""
