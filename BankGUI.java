import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

class Account implements Serializable {
    static int accCount = 36;
    String accNo;



    String name;
    String email;
    String phone;
    double balance;
    java.util.List<String> records;

    public Account(String name, String email, String phone) {
        this.accNo = String.valueOf(accCount++);
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.balance = 0.0;
        this.records = new java.util.ArrayList<>();
        addRecord("Account created with balance: 0.0");
    }



    public void addMoney(double amount) {
        balance += amount;
        addRecord("Added: " + amount);
    }




    public void takeMoney(double amount) {
        if (amount > balance) {

            JOptionPane.showMessageDialog(null, "Not enough balance.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            balance -= amount;
            addRecord("Taken: " + amount);
        }
    }

    public void saveRecords() {
        String fileName = accNo + "_" + name + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Account Number: " + accNo + "\n");
            writer.write("Name: " + name + "\n");
            writer.write("Email: " + email + "\n");
            writer.write("Phone: " + phone + "\n");
            writer.write("Balance: " + balance + "\n");
            
            writer.write("Records:\n");
            for (String record : records) {
                writer.write("- " + record + "\n");
            }
            JOptionPane.showMessageDialog(null, "Records saved to: " + fileName, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving records.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addRecord(String record) {
        records.add(record);
    }
}

public class BankGUI {
    private static final String FILE = "data.txt";
    private static Map<String, Account> data = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static void loadData() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE))) {
            data = (Map<String, Account>) in.readObject();
            if (!data.isEmpty()) {
                Account.accCount = Integer.parseInt(Collections.max(data.keySet())) + 1;
            }
        } catch (Exception e) {
            System.out.println("No data found. Starting fresh.");
        }
    }

    public static void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE))) {
            out.writeObject(data);
        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }

    public static void main(String[] args) {
        loadData();

        JFrame frame = new JFrame("Bank Management System");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(6, 1));

        JButton newAccButton = new JButton("Create New Account");
        JButton depositButton = new JButton("Deposit Money");
        JButton withdrawButton = new JButton("Withdraw Money");
        JButton viewDetailsButton = new JButton("View Account Details");
        JButton statementButton = new JButton("Get Statement");
        JButton exitButton = new JButton("Exit");

        newAccButton.addActionListener(e -> {
            JTextField nameField = new JTextField();
            JTextField emailField = new JTextField();
            JTextField phoneField = new JTextField();
            
            JPanel panel = new JPanel(new GridLayout(3, 2));
            panel.add(new JLabel("Name: "));
            panel.add(nameField);
            panel.add(new JLabel("Email: "));
            panel.add(emailField);
            panel.add(new JLabel("Phone: "));
            panel.add(phoneField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Enter Account Details", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                
                if (!name.matches("[a-zA-Z ]+") || !email.contains("@") || !phone.matches("[0-9]+")) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Account acc = new Account(name, email, phone);
                data.put(acc.accNo, acc);
                saveData();
                JOptionPane.showMessageDialog(null, "Account created! Number: " + acc.accNo, "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        depositButton.addActionListener(e -> {
            String accNo = JOptionPane.showInputDialog("Enter Account Number:");
            Account acc = data.get(accNo);
            if (acc == null) {
                JOptionPane.showMessageDialog(null, "Account not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double amount = Double.parseDouble(JOptionPane.showInputDialog("Enter amount to deposit:"));
            acc.addMoney(amount);
            saveData();
        });

        withdrawButton.addActionListener(e -> {
            String accNo = JOptionPane.showInputDialog("Enter Account Number:");
            Account acc = data.get(accNo);
            if (acc == null) {
                JOptionPane.showMessageDialog(null, "Account not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double amount = Double.parseDouble(JOptionPane.showInputDialog("Enter amount to withdraw:"));
            acc.takeMoney(amount);
            saveData();
        });

        viewDetailsButton.addActionListener(e -> {
            String accNo = JOptionPane.showInputDialog("Enter Account Number:");
            Account acc = data.get(accNo);
            if (acc == null) {
                JOptionPane.showMessageDialog(null, "Account not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(null, "Name: " + acc.name + "\nBalance: " + acc.balance);
        });

        statementButton.addActionListener(e -> {
            String accNo = JOptionPane.showInputDialog("Enter Account Number:");
            Account acc = data.get(accNo);
            if (acc != null) {
                acc.saveRecords();
            }
        });

        frame.add(newAccButton);
        frame.add(depositButton);
        frame.add(withdrawButton);
        frame.add(viewDetailsButton);
        frame.add(statementButton);
        frame.add(exitButton);
        frame.setVisible(true);
    }
}
