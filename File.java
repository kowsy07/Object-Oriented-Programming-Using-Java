import java. util.*;

import java.io.*;

class FileCopy

{

public static void main(String[] args)throws Exception

{

FileInputStream in = new FileInputStream("input.txt");

FileOutputStream out = new FileOutputStream("output.txt");

int ch;

while((ch=in.read())!=-1) out.write(ch);

System.out.println("Content of input file copied successfully into the output file");

}

}

