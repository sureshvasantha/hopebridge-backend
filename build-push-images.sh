#!/bin/bash
# --------------------------------------------
# HopeBridge Docker Build & Push Script
# --------------------------------------------

# Exit immediately if any command fails
set -e

# Docker Hub credentials (optional, can be set in env)
DOCKER_USER="sureshvasantha"
BACKEND_IMAGE="hopebridge-backend"
FRONTEND_IMAGE="hopebridge-frontend"
BACKEND_PATH="./hopebridge"
FRONTEND_PATH="./hopebridge-frontend"

# Colors for output
GREEN='\033[0;32m'
NC='\033[0m' # No Color

echo -e "${GREEN}🚀 Starting Docker build & push process...${NC}"

# Step 1: Build backend
echo -e "${GREEN}📦 Building backend image...${NC}"
docker build -t ${BACKEND_IMAGE}:latest ${BACKEND_PATH}

# Step 2: Build frontend
echo -e "${GREEN}📦 Building frontend image...${NC}"
docker build -t ${FRONTEND_IMAGE}:latest ${FRONTEND_PATH}

# Step 3: Tag images for Docker Hub
echo -e "${GREEN}🏷️  Tagging images...${NC}"
docker tag ${BACKEND_IMAGE}:latest ${DOCKER_USER}/${BACKEND_IMAGE}:latest
docker tag ${FRONTEND_IMAGE}:latest ${DOCKER_USER}/${FRONTEND_IMAGE}:latest

# Step 4: Login to Docker Hub
# echo -e "${GREEN}🔑 Logging into Docker Hub...${NC}"
# docker login -u ${DOCKER_USER}

# Step 5: Push images
echo -e "${GREEN}⬆️  Pushing images to Docker Hub...${NC}"
docker push ${DOCKER_USER}/${BACKEND_IMAGE}:latest
docker push ${DOCKER_USER}/${FRONTEND_IMAGE}:latest

# Step 6: Completion message
echo -e "${GREEN}✅ All images built and pushed successfully!${NC}"
