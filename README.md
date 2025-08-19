# 🍴 Swadkart - Food Ordering System

Swadkart is a **CLI-based food ordering system** built in **Java**.  
It simulates a real-world food delivery platform where customers can browse restaurants, view dishes, place orders, and make payments.  

---

## ✨ Features
- 👤 Customer registration, login, and password reset  
- 🍕 Browse and search dishes by restaurant or cuisine  
- 🛒 Add dishes to cart (custom linked list implementation)  
- 📦 Place and track orders  
- 💳 Payment integration (Cash, Card, UPI)  
- ⭐ Rate restaurants and dishes  
- 📊 Admin access for data management  

---

## 🛠️ Tech Stack
- **Language**: Java  
- **Database**: MySQL (via JDBC)  
- **Tools**: IntelliJ IDEA / Eclipse  
- **Version Control**: Git  

---

## ⚡ Getting Started

### 1. Clone Repository
```bash
git clone https://github.com/Jatan06/swadkart.git
cd swadkart
```

### 2. Setup Database
- Import `swadkart.sql` into MySQL  
- Update DB credentials in `AppConstants.java`

### 3. Run Application
```bash
javac App/Main.java
java App.Main
```

---

## 📂 Project Structure (High-level)

```
Swadkart
├── Ds/                  # Core data structures
│   ├── Dish.java
│   ├── Node.java
│   ├── CustomerMenu.java
│   ├── Payment.java
│   └── ...
├── Database/            # JDBC + DB operations
│   ├── ConnectionFactory.java
│   ├── QueryExecutor.java
│   └── ...
├── App/                 # Main entry + constants
│   ├── AppConstants.java
│   └── Main.java
├── README.md
```

---

## 📦 Detailed Project Structure  

### Package: `Ds`

#### Class: `Dish`
- **Attributes:**
  - `String dish_id` → unique identifier for the dish
  - `String name` → dish name
  - `String cuisine` → cuisine type
  - `String restaurant` → restaurant name
  - `double rating` → dish rating
  - `double price` → price of dish
- **Methods:**
  - `Dish(...)` → constructor to initialize dish
  - `getDish_id()` → returns dish id
  - `getName()` → returns dish name
  - `getCuisine()` → returns cuisine type
  - `getRestaurant()` → returns restaurant name
  - `getRestaurantId(String name)` → retrieves restaurant id from name
  - `getRating()` → returns dish rating
  - `getPrice()` → returns price  

---

#### Class: `Node`
- **Attributes:**
  - `Dish data` → stores a Dish object
  - `int quantity` → quantity ordered
  - `Node next` → link to next node
- **Methods:**
  - `Node(Dish data,int quantity)` → constructor
  - `insert(Dish dish, int quantity)` → adds new dish to linked list
  - `display()` → prints node data
  - `displayTabular()` → prints in tabular form
  - `delete(String dishId)` → deletes a dish by id
  - `clearList()` → clears the list
  - `repeat(char ch, int count)` → helper for table formatting
  - `safe(String s)` → avoids null pointer  

---

#### Class: `CustomerMenu`
- **Methods:**
  - `newCustomer()` → registers a new customer
  - `forgotPassword(String id)` → handles password reset
  - `customerValidator(String id, String password)` → validates login
  - `customerMenu(String id)` → loads customer menu
  - `displayMenu()` → prints menu options
  - `getUserInput()` → takes input from user
  - `processAction(int option, String id)` → processes menu actions  

---

#### Class: `MainMenu`
- **Methods:**
  - `run()` → starts main menu loop  
  - `show()` → displays available options  

---

#### Class: `Payment`
- **Attributes:**
  - `double amount` → transaction amount
  - `String mode` → payment mode (cash/card/UPI)
- **Methods:**
  - `pay(double amount, String mode)` → processes payment  

---

### Package: `Database`

#### Class: `ConnectionFactory`
- **Methods:**
  - `getConnection()` → establishes and returns DB connection  

---

#### Class: `QueryExecutor`
- **Methods:**
  - `executeQuery(String sql)` → runs SQL query  
  - `executeUpdate(String sql)` → runs insert/update/delete  

---

### Package: `App`

#### Class: `AppConstants`
- **Attributes:**
  - `public static Connection connection` → DB connection reference  
  - `public static Scanner s` → global scanner for input  
  - `public static String dburl, dbuser, dbpass, driverName` → DB configs  

---

#### Class: `Main`
- **Methods:**
  - `main(String[] args)` → entry point of application  
  - initializes DB connection  
  - invokes `MainMenu.run()`  

---

## 🤝 Contributing
1. Fork the repo  
2. Create a feature branch  
3. Commit changes  
4. Submit a PR 🚀  

---

## 👨‍💻 Author
- Developed by [Jatan Parikh](https://github.com/Jatan06),
  [Raj Desai](https://github.com/RajDesai87),
  [Nishtha Dave](https://github.com/DaveNishtha)
