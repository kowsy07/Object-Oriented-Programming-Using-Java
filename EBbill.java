import java.util.Scanner;

public class EBBill {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int consumerNo, previous, current, units, choice;
        String name;
        double bill = 0;

        System.out.print("Enter Consumer Number: ");
        consumerNo = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Consumer Name: ");
        name = sc.nextLine();

        System.out.print("Enter Previous Reading: ");
        previous = sc.nextInt();

        System.out.print("Enter Current Reading: ");
        current = sc.nextInt();

        units = current - previous;

        System.out.println("\nChoose Connection Type");
        System.out.println("1. Domestic");
        System.out.println("2. Commercial");
        System.out.print("Enter Choice: ");
        choice = sc.nextInt();

        if (choice == 1) {

            if (units <= 100) {
                bill = 0;
            } else if (units <= 200) {
                bill = units * 2;
            } else if (units <= 500) {
                bill = units * 4;
            } else {
                bill = units * 6;
            }

        } else if (choice == 2) {

            if (units <= 100) {
                bill = units * 2;
            } else if (units <= 200) {
                bill = units * 4;
            } else if (units <= 500) {
                bill = units * 6;
            } else {
                bill = units * 7;
            }

        } else {
            System.out.println("Invalid Choice");
            sc.close();
            return;
        }

        System.out.println("\n\t\t----- EB BILL -----");
        System.out.println("Consumer No : " + consumerNo);
        System.out.println("Consumer Name : " + name);
        System.out.println("Units Consumed : " + units);
        System.out.println("Bill Amount : Rs. " + bill);

        sc.close();
    }
}

Mm
