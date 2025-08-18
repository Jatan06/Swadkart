# Swadkart

Swadkart is a command-line food ordering system that connects customers with restaurants. It delivers the full flow from browsing menus to ordering, tracking, and reviewing. Admins can manage restaurants, menus, and order lifecycles.

---

## Table of Contents
- Overview
- Features
- Architecture
- Tech Stack
- Prerequisites
- Setup
- Configuration
- Database Setup
- Run
- Typical CLI Flow
- Troubleshooting
- Roadmap
- Contributing
- License

---

## Overview

Swadkart offers a clean, menu-driven CLI to:
- Explore restaurants and dishes
- Add items to a cart and place orders
- Simulate payments and track status
- Leave ratings and reviews
- Enable admins to manage restaurants, menus, and order statuses

---

## Features

### Customer
- Browse restaurants and dishes (with categories, pricing, availability)
- Add items to cart and place orders
- Simulate payment
- Track order status (Placed → Preparing → Out for Delivery → Delivered/Cancelled)
- Rate and review dishes/restaurants
- View past orders and reorder

### Admin
- Add, edit, and remove restaurants
- Manage dishes for owned restaurants (create/update/toggle availability/delete)
- View and update order statuses
- Review customer feedback and aggregate ratings

### Authentication & Sessions
- Register as Customer or Admin
- Secure login/logout
- Session-aware menus

---

## Architecture

- CLI interface with guided, validated input
- Layered design for maintainability:
  - UI: CLI navigation and input handling
  - Service: business logic and orchestration
  - DAO: data access via JDBC
  - Model: core entities (User, Restaurant, Dish, Order, Review, etc.)
- Parameterized queries and password hashing
- Centralized error handling

---

## Tech Stack

- Java (JDK 24)
- JDBC (Prepared Statements)
- MySQL (8.x or compatible)
- Build: Gradle or Maven

Notes
- Replace <version> and your.main.Class as applicable.
- Ensure the database is reachable and credentials are correct.

---

## Prerequisites

- Java 24 installed and on PATH
- MySQL running locally or accessible remotely
- Gradle or Maven (or use project wrappers, if available)

---

## Setup

1) Clone the repository

2) Create a database (see “Database Setup” below)

3) Configure database credentials (see “Configuration” below)

---

## Configuration

Choose one approach:

Option A: Environment variables
- DB_URL=jdbc:mysql://localhost:3306/swadkart?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
- DB_USER=your_mysql_user
- DB_PASSWORD=your_mysql_password

Option B: Properties file
Create a file named swadkart.properties in the project root with:
- db.url=jdbc:mysql://localhost:3306/swadkart?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
- db.user=your_mysql_user
- db.password=your_mysql_password

Use only one configuration method to avoid conflicts.

---

## Database Setup

2) Suggested tables (simplified)
- users: id, role (CUSTOMER/ADMIN), name, email (unique), password_hash, created_at
- restaurants: id, name, description, owner_user_id (FK), rating_avg, created_at
- dishes: id, restaurant_id (FK), name, description, price, is_available, created_at
- orders: id, user_id (FK), restaurant_id (FK), status, total_amount, created_at
- order_items: id, order_id (FK), dish_id (FK), quantity, unit_price
- reviews: id, user_id (FK), restaurant_id (FK), dish_id (nullable FK), rating (1–5), comment, created_at

3) Indexing recommendations
- users(email)
- dishes(restaurant_id, is_available)
- orders(user_id, restaurant_id, status, created_at)
- reviews(restaurant_id, dish_id, created_at)

4) Seed data (optional)
- One Admin and one Customer
- Sample Restaurants and Dishes
