#!/bin/bash

echo "=========================================="
echo "  Cleanup Script"
echo "=========================================="
echo ""

# Stop and remove ZAP container
if docker ps -a | grep -q zap; then
    echo "Stopping ZAP container..."
    docker stop zap 2>/dev/null
    echo "Removing ZAP container..."
    docker rm zap 2>/dev/null
    echo "✓ ZAP container stopped and removed"
else
    echo "ℹ️  No ZAP container found"
fi

echo ""

# Ask about cleaning build artifacts
read -p "Do you want to clean Maven build artifacts? (y/n): " -n 1 -r
echo ""
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "Cleaning Maven artifacts..."
    mvn clean
    echo "✓ Maven artifacts cleaned"
fi

echo ""

# Ask about cleaning reports
read -p "Do you want to delete generated reports? (y/n): " -n 1 -r
echo ""
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "Deleting reports..."
    rm -rf reports/
    echo "✓ Reports deleted"
fi

echo ""
echo "=========================================="
echo "  Cleanup Complete!"
echo "=========================================="
echo ""
