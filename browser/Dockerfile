## STAGE 1
FROM node:20.9.0-alpine as frontend-builder
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend .
RUN npm run build

## STAGE 2
FROM node:20.9.0-alpine
WORKDIR /app/backend
COPY backend/package*.json ./
RUN npm install
COPY backend .
COPY --from=frontend-builder /app/frontend/dist /app/backend/frontend/dist
EXPOSE 8080
CMD ["npm", "start"]