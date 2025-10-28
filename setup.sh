#!/bin/bash

echo "=========================================="
echo "  DAST Pipeline - Local Setup Script"
echo "=========================================="
echo ""

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker first."
    echo "   Visit: https://docs.docker.com/get-docker/"
    exit 1
fi

echo "✓ Docker is installed"

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven first."
    echo "   Visit: https://maven.apache.org/install.html"
    exit 1
fi

echo "✓ Maven is installed"

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F '.' '{print $1}')
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java 17 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

echo "✓ Java $JAVA_VERSION is installed"
echo ""

# Create reports directory
echo "Creating reports directory..."
mkdir -p reports
echo "✓ Reports directory created"
echo ""

# Build the project
echo "Building the project with Maven..."
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "❌ Maven build failed"
    exit 1
fi
echo "✓ Project built successfully"
echo ""

# Start ZAP
echo "Starting OWASP ZAP Docker container..."
docker run -d --name zap \
  -u zap \
  -p 8080:8080 \
  -v "$(pwd)":/zap/wrk/:rw \
  ghcr.io/zaproxy/zaproxy:stable \
  zap.sh -daemon -host 0.0.0.0 -port 8080 -config api.addrs.addr.name=.* -config api.addrs.addr.regex=true -config api.disablekey=true

if [ $? -ne 0 ]; then
    echo "❌ Failed to start ZAP container"
    exit 1
fi

echo "✓ ZAP container started"
echo ""

# Wait for ZAP to be ready
echo "Waiting for ZAP to start (this may take 30-60 seconds)..."
COUNTER=0
MAX_ATTEMPTS=60

while ! curl -s http://localhost:8080 > /dev/null; do
    sleep 2
    COUNTER=$((COUNTER + 1))
    if [ $COUNTER -ge $MAX_ATTEMPTS ]; then
        echo "❌ ZAP failed to start within 120 seconds"
        docker stop zap && docker rm zap
        exit 1
    fi
    if [ $((COUNTER % 5)) -eq 0 ]; then
        echo "   Still waiting... ($((COUNTER * 2)) seconds)"
    fi
done

echo "✓ ZAP is ready"
echo ""

echo "=========================================="
echo "  Setup Complete!"
echo "=========================================="
echo ""
echo "To run security tests:"
echo "  mvn test"
echo ""
echo "To generate reports:"
echo "  mvn exec:java -Dexec.mainClass=\"com.security.report.ReportGenerator\""
echo ""
echo "To view reports:"
echo "  open reports/index.html"
echo ""
echo "To stop ZAP:"
echo "  docker stop zap && docker rm zap"
echo ""
echo "Or use the provided scripts:"
echo "  ./run-tests.sh"
echo "  ./generate-reports.sh"
echo "  ./cleanup.sh"
echo ""
