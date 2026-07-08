#!/bin/bash

# Stop the script if any command fails
set -e

# Define the colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}🔨 Starting Multi-Service scan Process...${NC}\n"

# Array of service directories
SERVICES=(
    "user-service"
    "product-service"
)

    # "api-gateway"
    # "discovery-service"
    # "media-service"

# Loop through each service and build
for SERVICE in "${SERVICES[@]}"; do
    if [ -d "$SERVICE" ]; then
        echo -e "${BLUE}📦 scaning Service:${NC} $SERVICE"
        
        # Move into directory, build, and come back automatically
        (
            cd "$SERVICE"
            # Ensure Maven Wrapper is executable
            chmod +x mvnw
            # Clean and Package
            ./mvnw clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
                -Dsonar.projectKey=$SERVICE \
                -Dsonar.projectName="$SERVICE" \
                -Dsonar.host.url=http://sonarqube:9000 \
                -Dsonar.token=sqa_7a604cd9494962f78dfe3a95d16ba31aaffa9d59
        )
        
        echo -e "${GREEN}✅ Successfully scaned $SERVICE${NC}\n"
    else
        echo -e "${RED}❌ Directory $SERVICE not found. Skipping...${NC}\n"
    fi
done

echo -e "${GREEN}🚀 All services scaned successfully!${NC}"