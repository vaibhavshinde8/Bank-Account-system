# Bank Management System

This is a simple Java-based Bank Management System with a graphical user interface (GUI) using Swing.

## Features
- Create a new bank account
- Deposit money
- Withdraw money
- View account details
- Get transaction statement
- Data persistence using file serialization

## Requirements
- Java (JDK 8 or later)

## How to Run
1. Compile the program:
   ```sh
   javac BankGUI.java
   ```
2. Run the program:
   ```sh
   java BankGUI
   ```

## File Storage
- Account data is stored in `data.txt` using Java object serialization.
- Account statements are saved as individual text files in the format `AccountNumber_Name.txt`.

## Author
Vaibhav Shinde

