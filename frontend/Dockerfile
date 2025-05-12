# Stage 1: Build the application
FROM node:20-alpine AS builder

WORKDIR /app

# Copy package files first to leverage Docker cache
COPY package*.json ./

# Install dependencies (including devDependencies)
RUN npm install

# Copy all source files
COPY . .

# Build the application (adjust if your build script is different)
RUN npm run build

# Stage 2: Serve the application
FROM node:20-alpine AS production

WORKDIR /app

# Copy package files again
COPY package*.json ./
COPY tsconfig.docker.json ./tsconfig.json

# Install only production dependencies
RUN npm install --omit=dev

# Copy built assets from builder stage
COPY --from=builder /app/build ./build

# If you're using a custom server (like Express), copy it too
# COPY server.js ./

# If you're using Create React App's static server:
RUN npm install -g serve

# Expose port
EXPOSE 3000

# Start the server
CMD ["serve", "-s", "build", "-l", "3000"]