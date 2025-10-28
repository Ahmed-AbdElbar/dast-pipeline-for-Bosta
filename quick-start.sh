#!/bin/bash

echo "=========================================="
echo "  DAST Pipeline - Quick Start"
echo "  Complete Automated Security Testing"
echo "=========================================="
echo ""

# Run setup
echo "📦 Step 1: Setting up environment..."
echo ""
./setup.sh
if [ $? -ne 0 ]; then
    echo "❌ Setup failed"
    exit 1
fi

echo ""
echo "🧪 Step 2: Running security tests..."
echo ""
sleep 2
./run-tests.sh

echo ""
echo "📊 Step 3: Generating reports..."
echo ""
sleep 2
./generate-reports.sh

echo ""
echo "=========================================="
echo "  🎉 All Done!"
echo "=========================================="
echo ""
echo "Your security assessment is complete!"
echo ""
echo "View the reports:"
echo "  open reports/index.html"
echo ""
echo "When finished, cleanup:"
echo "  ./cleanup.sh"
echo ""
