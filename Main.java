import java.util.Scanner;

// Interface

interface Calculator {

void add(double a, double b);

void subtract(double a, double b);

void multiply(double a, double b);

void divide(double a, double b);

}


// Implementation class

class Arithmetic implements Calculator {


public void add(double a, double b) {

System.out.println("Addition = " + (a + b));

}


public void subtract(double a, double b) {

System.out.println("Subtraction = " + (a - b));

}


public void multiply(double a, double b) {

System.out.println("Multiplication = " + (a * b));

}




public void divide(double a, double b) {

if (b != 0)

System.out.println("Division = " + (a / b));

else

System.out.println("Division by zero is not possible.");

}

}


// Main class

public class Main {

public static void main(String[] args) {


Scanner sc = new Scanner(System.in);


System.out.print("Enter first number: ");

double num1 = sc.nextDouble();


System.out.print("Enter second number: ");

double num2 = sc.nextDouble();


Arithmetic obj = new Arithmetic();


obj.add(num1, num2);

obj.subtract(num1, num2);

obj.multiply(num1, num2);

obj.divide(num1, num2);


sc.close();

}

}
