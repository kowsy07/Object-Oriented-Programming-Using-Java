import java.util.*;

class Main {

static <T extends Comparable<T>> T maximum (T[] a) {

T max = a[0];

for (T x : a)

if (x.compareTo(max) > 0)

max = x;

return max;

}

public static void main(String[] args) {

Integer[] a = {10, 25, 15, 40, 30};

Float[] b = {2.5f, 8.5f, 4.5f, 6.5f};

String[] c = {"Apple", "Orange", "Mango", "Banana"};

System.out.println("Maximum Integer: " + maximum(a));

System.out.println("Maximum Float: " + maximum(b));

System.out.println("Maximum String:)
}
}
