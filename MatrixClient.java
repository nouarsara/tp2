
package tp2;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MatrixClient {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 5000;
        String username = "";
        String password = "";
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your username: ");
            username = scanner.nextLine();
            System.out.print("Enter your password: ");
            password = scanner.nextLine();
            Socket socket = new Socket(host, port);
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter outputWriter = new PrintWriter(socket.getOutputStream(), true);
            outputWriter.println(username);
            outputWriter.println(password);
            String response = inputReader.readLine();
            if (response.equals("SUCCESS")) {
                System.out.println("You are now logged in.");
                while (true) {
                    System.out.println("Choose an operation:");
                    System.out.println("1. Add two matrices");
                    System.out.println("2. Multiply two matrices");
                    System.out.println("3. Transpose a matrix");
                    System.out.println("4. Exit");
                    String operationId = scanner.nextLine();
                    if (operationId.equals("4")) {
                        break;
                    }
                    System.out.print("Enter matrix parameters (separated by commas): ");
                    String params = scanner.nextLine();
                    outputWriter.println(operationId);
                    outputWriter.println(params);
                    String result = inputReader.readLine();
                    System.out.println("Result: " + result);
                }
                socket.close();
            } else {
                System.out.println("Invalid username or password.");
                socket.close();
            }
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + host);
        } catch (IOException e) {
            System.out.println("Error connecting to server.");
        }
    }
}
