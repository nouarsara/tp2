package tp2;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class MatrixServer {
    private static List<User> userList = new ArrayList<>();

    public static void main(String[] args) {
        int port = 5000;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port + ".");
            while (true) {
                Socket socket = serverSocket.accept();
                Thread thread = new Thread(new ClientHandler(socket));
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("Error starting server.");
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader inputReader;
        private PrintWriter outputWriter;
        private User user;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outputWriter = new PrintWriter(socket.getOutputStream(), true);
                String username = inputReader.readLine();
                String password = inputReader.readLine();
                user = getUser(username, password);
                if (user != null) {
                    outputWriter.println("SUCCESS");
                    while (true) {
                        String operationId = inputReader.readLine();
                        String params = inputReader.readLine();
                        String result = performOperation(operationId, params);
                        outputWriter.println(result);
                    }
                } else {
                    outputWriter.println("ERROR");
                    socket.close();
                }
            } catch (IOException e) {
                System.out.println("Error handling client.");
            }
        }

        private User getUser(String username, String password) {
            for (User user : userList) {
                if (user.getUsername().equals(username)) {
                    if (user.getPassword().equals(password)) {
                        return user;
                    } else {
                        return null;
                    }
                }
            }
            User user = new User(username, password);
            userList.add(user);
            return user;
        }

       private String performOperation(String operationId, String params) {
    String[] matrixParams = params.split(",");
    int[][] matrixA = parseMatrix(matrixParams[0]);
    int[][] matrixB = parseMatrix(matrixParams[1]);
    int m = matrixA.length;
    int n = matrixA[0].length;
    int p = matrixB[0].length;
    int[][] resultMatrix;
    switch (operationId) {
        case "1":
            // Addition of matrices
            if (matrixA.length != matrixB.length || matrixA[0].length != matrixB[0].length) {
                return "Error: matrices must have the same dimensions for addition.";
            }
            resultMatrix = new int[m][n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    resultMatrix[i][j] = matrixA[i][j] + matrixB[i][j];
                }
            }
            break;
        case "2":
            // Multiplication of matrices
            if (matrixA[0].length != matrixB.length) {
                return "Error: number of columns in matrix A must equal number of rows in matrix B for multiplication.";
            }
            resultMatrix = new int[m][p];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < p; j++) {
                    int sum = 0;
                    for (int k = 0; k < n; k++) {
                        sum += matrixA[i][k] * matrixB[k][j];
                    }
                    resultMatrix[i][j] = sum;
                }
            }
            break;
        case "3":
            // Transposition of matrix
            resultMatrix = new int[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    resultMatrix[i][j] = matrixA[j][i];
                }
            }
            break;
        default:
            return "Error: invalid operation ID.";
    }
    return matrixToString(resultMatrix);
}

private int[][] parseMatrix(String matrixString) {
    String[] rows = matrixString.split(";");
    int m = rows.length;
    int n = rows[0].split(",").length;
    int[][] matrix = new int[m][n];
    for (int i = 0; i < m; i++) {
        String[] values = rows[i].split(",");
        for (int j = 0; j < n; j++) {
            matrix[i][j] = Integer.parseInt(values[j]);
        }
    }
    return matrix;
}

private String matrixToString(int[][] matrix) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix[0].length; j++) {
            sb.append(matrix[i][j]);
            if (j < matrix[0].length - 1) {
                sb.append(",");
            }
        }
        if (i < matrix.length - 1) {
            sb.append(";");
        }
    }
    return sb.toString();
}

            
        
    }

    private static class User {
        private String username;
        private String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
    
    
    
}

