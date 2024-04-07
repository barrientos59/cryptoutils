package mp9.uf1.cryptoutils;
import java.io.*;
import java.net.*;
import java.security.*;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(54321);
            System.out.println("Servidor iniciado. Esperando conexiones...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado desde: " + clientSocket.getInetAddress().getHostName());

            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            // Generar par de claves para el servidor
            KeyPair serverKeyPair = MyCryptoUtils.randomGenerate(2048);
            PublicKey serverPublicKey = serverKeyPair.getPublic();
            PrivateKey serverPrivateKey = serverKeyPair.getPrivate();

            // Enviar clave pública del servidor al cliente
            out.writeObject(serverPublicKey);

            // Recibir clave pública del cliente
            PublicKey clientPublicKey = (PublicKey) in.readObject();

            // para recibir mensajes del cliente
            Thread recibirMensajes = new Thread(() -> {
                try {
                    while (true) {
                        // Recibir mensaje cifrado del cliente y descifrarlo con la clave privada del servidor
                        byte[] mensajeCifradoCliente = (byte[]) in.readObject();
                        byte[] mensajeDescifrado = MyCryptoUtils.decryptData(mensajeCifradoCliente, serverPrivateKey);
                        String mensajeOriginal = new String(mensajeDescifrado);

                        System.out.println("Cliente: " + mensajeOriginal);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            recibirMensajes.start();

            // Bucle para enviar mensajes
            String mensaje;
            BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                // Leer mensaje desde consola y cifrarlo con la clave pública del cliente
                System.out.print("Yo: ");
                mensaje = entrada.readLine();
                byte[] mensajeCifrado = MyCryptoUtils.encryptData(mensaje.getBytes(), clientPublicKey);

                // Enviar mensaje cifrado al cliente
                out.writeObject(mensajeCifrado);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
