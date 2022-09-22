package ru.itis.diffie_hellman;

public class ExchangeApp {

    protected static final int P_NUMBER_SIZE = 1024;
    protected static final int SECRET_KEY_SIZE = 2048;

    public static void main(String[] args) {
        Channel sharedChannel = new Channel();
        Alice alice = new Alice(P_NUMBER_SIZE, SECRET_KEY_SIZE);
        Bob bob = new Bob(P_NUMBER_SIZE, SECRET_KEY_SIZE);
        Eva eva = new Eva();

        alice.sendOpenKey(sharedChannel);
        bob.receiveOpenKey(sharedChannel);
        bob.sendOpenKey(sharedChannel);
        alice.receiveOpenKey(sharedChannel);
        eva.eavesdropChannel(sharedChannel);

        alice.generateSecretSharedKey();
        bob.generateSecretSharedKey();

        eva.findSecretSharedKey();

        if (eva.getKey() != null){
            System.out.println("Alice's open key: " + alice.getA());
            System.out.println("Bob's open key: " + bob.getB());
            System.out.println("/---------------------------------------------------------\\");
            System.out.println("Alice's key: " + alice.getKey());
            System.out.println("Bob's key: " + bob.getKey());
            System.out.println("Eva's key: " + eva.getKey());
            System.out.println("Is the key correct:? "
                    + (alice.getKey().equals(eva.getKey()) && bob.getKey().equals(eva.getKey())));
        }
    }

}
