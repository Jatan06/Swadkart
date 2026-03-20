# 🍔 Swadkart - Food Ordering System

Swadkart is a **Java-based console food ordering system** designed to simulate real-world restaurant and ordering workflows.
It follows a layered architecture using **DAO, Services, and Models**, making it modular and scalable.

---

## 🚀 Key Features

### 👤 User Features

* User Registration & Login
* OTP-based Authentication (Email/Password reset)
* Browse Restaurants & Dishes
* Place Orders
* Make Payments
* Add Reviews & Ratings

### 🛠️ Admin Features

* Manage Restaurants
* Manage Dishes
* View Orders & Payments
* Monitor system activity

### ⚙️ System Features

* Session Management
* Input Validation
* Sound & Text-to-Speech Feedback
* Logging System
* Database Connectivity (JDBC)

---

## 🧠 Architecture Overview

The project follows a **multi-layered architecture**:

```id="arch1"
Presentation Layer (Menus)
        ↓
Service Layer (Business Logic)
        ↓
DAO Layer (Database Access)
        ↓
Database (MySQL / JDBC)
```

---

## 📂 Project Structure

```id="struct1"
src/
│
├── Admin/                # Admin functionalities
├── Constants/            # Application constants
├── Dao/                  # Data Access Layer
├── Db/                   # Database connection
├── Ds/                   # Custom Data Structures (Linked List)
├── Menus/                # CLI User Interface
├── Models/               # Entity classes
├── Services/             # Business Logic
├── Session/              # Session handling
├── Utils/                # Validation utilities
│
└── Main.java             # Entry point
```

---

## 🛠️ Tech Stack

* **Language:** Java (Core + OOP)
* **Database:** MySQL (via JDBC)
* **Concepts Used:**

  * Object-Oriented Programming
  * DAO Design Pattern
  * Service Layer Architecture
  * File Handling
  * Session Management

---

## ⚙️ Setup Instructions

### 1️⃣ Clone the repository

```id="cmd1"
git clone https://github.com/Jatan06/Swadkart.git
cd Swadkart
```

---

### 2️⃣ Configure Database

Update your database credentials in:

```id="cmd2"
src/Db/DBConnection.java
```

Make sure:

* MySQL is running
* Required tables are created

---

### 3️⃣ Compile the project

```id="cmd3"
javac -d bin src/**/*.java
```

---

### 4️⃣ Run the application

```id="cmd4"
java -cp bin Main
```

---

## 🔐 Security Notes

* Do NOT commit real database credentials
* Use `db.properties` (recommended improvement)
* OTP system should be secured in production

---

## 📌 Unique Highlights

* 🔑 OTP-based login & password reset
* 🔊 Sound & speech integration (rare in CLI projects)
* 🧩 Clean separation of layers (DAO + Service)
* 📊 Real-world simulation of food ordering system

---

## 🚀 Future Enhancements

* 🌐 Convert to Web App (Spring Boot / MERN)
* 📱 Mobile App Integration
* 💳 Payment Gateway Integration
* 📈 Admin Dashboard (Analytics)
* 🔐 JWT Authentication

---

## 👨‍💻 Author

**[Jatan Parikh](https://www.linkedin.com/in/jatanparikh/)**
**[Raj Desai](https://www.linkedin.com/in/raj-desai-017979233/)**
**[Nishtha Dave](https://www.linkedin.com/in/nishtha-dave-71992038b/)**

---

## ⭐ Support

If you found this project useful:

* ⭐ Star this repository
* 🍴 Fork it
* 🧠 Improve it

---

## 📬 Contact

Feel free to connect for collaboration or suggestions!
