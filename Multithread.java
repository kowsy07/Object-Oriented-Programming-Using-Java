import java.util.Random;

class Number {

static Number x = new Number();

static int n;

static boolean ready = false;

synchronized void generate()

{

while (ready)

try { wait(); } catch (Exception e) {}


n = new Random().nextInt(10) + 1;

System.out.println("Number: " + n);

ready = true;

notifyAll();

}


synchronized void square()

{

while (true) {

try {

while (!ready || n % 2 == 0)

wait();


System.out.println("Square: " + n * n);

ready = false;

notifyAll();

} catch (Exception e) {}

}

}


synchronized void cube()

{

while (true) {

try {

while (!ready || n % 2 != 0)

wait();


System.out.println("Cube: " + n * n * n);

ready = false;

notifyAll();

} catch (Exception e) {}

}

}

}


class Generate extends Thread

{

public void run() {

for (int i = 0; i < 5; i++) {

Number.x.generate();

try { sleep(1000); } catch (Exception e) {}

}

}

}


class Square extends Thread

{

public void run() {

Number.x.square();

}

}


class Cube extends Thread

{

public void run() {

Number.x.cube();

}

}






public class Multithread

{

public static void main(String[] args) {

Generate t1 = new Generate();

Square t2 = new Square();

Cube t3 = new Cube();


t1.start();

t2.start();

t3.start();

}

}






















Ex9- GENERRIC PROGRA
