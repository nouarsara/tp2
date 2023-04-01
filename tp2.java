CLIENT
package TP2;


import java.io.*;
import java.net.*;




public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 9999);
            System.out.println("Connecté au serveur : " + socket.getInetAddress().getHostAddress());

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            double[][] matrice1 = {{1, 2, 3}, {4, 5, 6}};
            double[][] matrice2 = {{7, 8}, {9, 10}, {11, 12}};

            out.writeObject(matrice1);
            out.writeObject(matrice2);

            double[][] resultat = (double[][]) in.readObject();

            System.out.println("Résultat :");
            for (int i = 0; i < resultat.length; i++) {
                for (int j = 0; j < resultat[0].length; j++) {
                    System.out.print(resultat[i][j] + " ");
                }
                System.out.println();
            }

            out.close();
            in.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}




____________________________________________________________________
SERVEUR
package TP2;



import java.io.*;
import java.net.*;

public class Serveur {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("Serveur en attente de connexion...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connecté : " + socket.getInetAddress().getHostAddress());

                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                double[][] matrice1 = (double[][]) in.readObject();
                double[][] matrice2 = (double[][]) in.readObject();

                double[][] resultat = multiplierMatrices(matrice1, matrice2);

                out.writeObject(resultat);

                in.close();
                out.close();
                socket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static double[][] multiplierMatrices(double[][] matrice1, double[][] matrice2) {
        int n = matrice1.length;
        int m = matrice2[0].length;
        double[][] resultat = new double[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < matrice2.length; k++) {
                    resultat[i][j] += matrice1[i][k] * matrice2[k][j];
                }
            }
        }

        return resultat;
    }
}
