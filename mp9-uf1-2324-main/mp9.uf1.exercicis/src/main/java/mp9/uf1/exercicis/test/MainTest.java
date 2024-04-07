package mp9.uf1.exercicis.test;

import mp9.uf1.cryptoutils.MyCryptoUtils;

import java.security.KeyPair;


public class MainTest {

    public static void main(String[] args) {
        System.out.println("prova " + MainTest.class.toString());
        MyCryptoUtils.keygenKeyGeneration(128);

        String msg = "hello world!!!";
        byte[] encriptedData = null;
        byte[] dencriptedData = null;

        //exerici 1.1
        System.out.println("Exercici 1.1");
        KeyPair parelldeclaus = MyCryptoUtils.randomGenerate(1024);
        encriptedData = MyCryptoUtils.encryptData(msg.getBytes(), parelldeclaus.getPublic());
        System.out.println(new String(encriptedData));

        dencriptedData = MyCryptoUtils.decryptData(encriptedData, parelldeclaus.getPrivate());
        System.out.println(new String(dencriptedData));

        System.out.println("pública:\n" + parelldeclaus.getPublic());
        System.out.println("tipus de clau privada:" + parelldeclaus.getPrivate().getFormat());

    }
}
