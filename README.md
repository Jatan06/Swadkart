# 🍴 Swadkart - Food Ordering System

Swadkart is a **CLI-based food ordering system** built in **Java**. It simulates a real-world food delivery platform where customers can browse restaurants, view dishes, place orders, and make payments.

## ✨ Features

- 👤 **User Management**
  - Customer registration with OTP verification
  - Secure login with password validation
  - Password reset via OTP
  - Profile management

- 🍽️ **Restaurant & Food**
  - Browse restaurants
  - View dishes by restaurant
  - Search by cuisine type
  - View dish details and ratings

- 🛒 **Order Management**
  - Add dishes to cart (custom linked list implementation)
  - Modify order quantities
  - Place orders
  - Track order status

- 💳 **Payment Processing**
  - Multiple payment methods (Cash, Card, UPI)
  - Payment verification
  - Receipt generation

- ⭐ **Reviews & Ratings**
  - Rate restaurants and dishes
  - View ratings and reviews

- 👨‍💼 **Admin Panel**
  - Manage restaurants and dishes
  - View user data
  - Generate reports

## 🛠️ Tech Stack

- **Language**: Java
- **Database**: MySQL (via JDBC)
- **Architecture**: MVC pattern with DAO layer
- **Data Structures**: Custom linked list implementation
- **Security**: OTP verification via Twilio API
- **UI**: Console-based with ANSI color formatting

## 🏗️ Project Structure

```
Swadkart
├── Admin/                # Admin functionality
├── Constants/            # Application constants
├── Dao/                  # Data Access Objects
├── Db/                   # Database connection
├── Ds/                   # Data structures
├── Main.java             # Entry point
├── Menus/                # UI menus
├── Models/               # Data models
├── Services/             # Business logic
├── Session/              # Session management
└── Utils/                # Utility functions
```

## 🚀 Getting Started

### Prerequisites

- Java JDK 8 or higher
- MySQL Server
- Maven (optional, for dependency management)

### Database Setup

1. Create a MySQL database named `swadkart`
2. Import the database schema (SQL file not included in this repository)
3. Update the database connection details in `Db/DBConnection.java` if needed

### Running the Application

1. Compile the Java files
2. Run the Main class
3. Follow the on-screen instructions to navigate the application

```bash
java -cp ".;lib/*" Main
```

## 🔐 Authentication Flow

1. **New User Registration**:
   - Enter phone number for OTP verification
   - Verify OTP
   - Set password
   - Enter personal details
   - Receive user ID

2. **Login**:
   - Enter user ID (format: u-xxxx)
   - Enter password
   - Option for password reset via OTP

## 🛍️ Order Flow

1. Browse restaurants
2. Select dishes and quantities
3. Review cart
4. Place order
5. Select payment method
6. Complete payment
7. Receive confirmation

## 👨‍💻 Development

### Code Organization

- **DAO Pattern**: Separates database operations from business logic
- **Service Layer**: Handles business logic and operations
- **Model Classes**: Represents data entities
- **Menu Classes**: Manages user interface and interaction

### Future Enhancements

- Graphical user interface
- Order tracking with real-time updates
- Expanded payment options
- Loyalty program integration
- Mobile app version

## 📝 License

This project is for educational purposes only.

---

*Where every bite meets delight!* 🍕🍔🍰
