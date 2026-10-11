import java.util.Scanner;


// Interface

interface BankAccount

{

void deposit(double amount);

void withdraw(double amount);

void balanceEnquiry();

}


// Implementation class

class Account implements BankAccount

{

private double balance = 0;


// Deposit Method

public void deposit(double amount)

{

try

{

if (amount <= 0)

{

throw new IllegalArgumentException("Invalid deposit amount!");

}


balance += amount;

System.out.println("Amount Deposited: Rs. " + amount);

}

catch (IllegalArgumentException e)

{

System.out.println(e.getMessage());

}

}


// Withdraw Method

public void withdraw(double amount)

{

try

{

if (amount <= 0)

{

throw new IllegalArgumentException("Invalid withdrawal amount!");

}


if (amount > balance)

{

throw new ArithmeticException("Insufficient Balance!");

}


balance -= amount;

System.out.println("Amount Withdrawn: Rs. " + amount);

}

catch (IllegalArgumentException | ArithmeticException e)

{

System.out.println(e.getMessage());

}

}


// Balance Enquiry

public void balanceEnquiry()

{

System.out.println("Current Balance: Rs. " + balance);

}

}


// Main Class

public class Main

{

public static void main(String[] args)

{

Scanner sc = new Scanner(System.in);

Account acc = new Account();


System.out.print("Enter Deposit Amount: ");

double deposit = sc.nextDouble();

acc.deposit(deposit);


System.out.print("Enter Withdrawal Amount: ");

double withdraw = sc.nextDouble();

acc.withdraw(withdraw);


acc.balanceEnquiry();


sc.close();

}

}

