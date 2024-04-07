package mp9.uf1.cryptoutils;
import java.io.*;
import java.net.*;
import java.security.*;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 54321);
            System.out.println("Conectado al servidor.");

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Generar par de claves para el cliente
            KeyPair clientKeyPair = MyCryptoUtils.randomGenerate(2048);
            PublicKey clientPublicKey = clientKeyPair.getPublic();
            PrivateKey clientPrivateKey = clientKeyPair.getPrivate();

            // Enviar clave pública del cliente al servidor
            out.writeObject(clientPublicKey);

            // Recibir clave pública del servidor
            PublicKey serverPublicKey = (PublicKey) in.readObject();

            // Crear subproceso para recibir mensajes del servidor
            Thread recibirMensajes = new Thread(() -> {
                try {
                    while (true) {
                        // Recibir mensaje cifrado del servidor y descifrarlo con la clave privada del cliente
                        byte[] mensajeCifradoServidor = (byte[]) in.readObject();
                        byte[] mensajeDescifrado = MyCryptoUtils.decryptData(mensajeCifradoServidor, clientPrivateKey);
                        String mensajeOriginal = new String(mensajeDescifrado);

                        System.out.println("Servidor: " + mensajeOriginal);
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
                // Leer mensaje desde consola y cifrarlo con la clave pública del servidor
                System.out.print("Yo: ");
                mensaje = entrada.readLine();
                byte[] mensajeCifrado = MyCryptoUtils.encryptData(mensaje.getBytes(), serverPublicKey);

                // Enviar mensaje cifrado al servidor
                out.writeObject(mensajeCifrado);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

