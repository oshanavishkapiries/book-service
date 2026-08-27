# 📚 Book Catalog & Inventory Service

**Module**: Enterprise Cloud Architecture (ITS 2130)  
**Component**: Business Microservice

---

## 👤 Student Information (Section 12 Compliance)

- **Student Name**: OSHAN AVISHKA
- **Student Number**: HDSE-24-1234
- **Slack Handle**: @oshanavishka
- **GCP Project ID**: eca-its2130-project

---

## 📖 Component Description

`book-service` handles book catalog management, search, categories, stock tracking, and cover image uploads using **MongoDB** (NoSQL) and **Google Cloud Storage (GCS)** buckets.

### Key API Endpoints
- `GET /api/books` - Retrieve all books (supports `category` & `search` query filters)
- `GET /api/books/{id}` - Retrieve book detail
- `POST /api/books` - Create new book entry
- `POST /api/books/upload-cover` - Upload cover image to GCS bucket
- `PUT /api/books/{id}/reduce-stock` - Inter-service call to deduct stock when order is placed

---

## 🛠️ Technology Stack & Database

- **Framework**: Java 21 / Spring Boot 3.3.3
- **Database**: MongoDB (NoSQL database requirement - Section 4)
- **Cloud Storage**: Google Cloud Storage Bucket (Mandatory GCS requirement - Section 9)
- **Port**: `8082`

---

## 🚀 Setup & Getting Started Instructions

### Build & Run locally
```bash
mvn clean package -DskipTests
java -jar target/book-service-1.0.0.jar
```

### Run using PM2
```bash
pm2 start target/book-service-1.0.0.jar --name book-service
```

---

## 🔗 Repository Navigation & Structure

This repository is maintained as a Git Submodule inside the parent super-repository:
- 🏛️ **Parent Repository**: [eca-backend-services](https://github.com/oshanavishkapiries/eca-backend-services)
