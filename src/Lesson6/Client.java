package Lesson6;

import java.io.*;
import java.net.*;


//        1. Разобраться с исходным кодом клиентской и серверной части, и для закрепления написать консольные эхо-сервер и клиент, без подглядывания в методичку;
//        2. * Написать консольный вариант клиент\серверного приложения, в котором пользователь может писать сообщения, как на клиентской стороне, так и на серверной.
//        Т.е. если на клиентской стороне написать "Привет", нажать Enter, то сообщение должно передаться на сервер и там отпечататься в консоли.
//        Если сделать то же самое на серверной стороне, сообщение, соответственно, передаётся клиенту и печатается у него в консоли.
//        Есть одна особенность, которую нужно учитывать: клиент или сервер может написать несколько сообщений подряд, такую ситуацию необходимо корректно обработать.
//        ВАЖНО! Сервер общается только с одним клиентом, т.е. не нужно запускать цикл, который будет ожидать второго/третьего/... клиентов.
//        (Если будете делать вариант со звездочкой, первую часть дз выполнять НЕ НАДО)

public class Client {
    public static void main(String[] args) throws IOException {
        CLI cli = new CLI();
        System.out.println("Client started. Connecting to localhost: 1111");

        new Thread() {
            public void run() {
                try {
                    cli.readMSG();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    cli.sendMSG();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

class CLI {
    Socket socket = null;
    BufferedReader in = null;
    PrintWriter out = null;
    BufferedReader console = null;
    String userMSG, serverMSG;

    public CLI() throws IOException {
        socket = new Socket("localhost", 1111);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        console = new BufferedReader(new InputStreamReader(System.in));
    }

    void sendMSG() throws IOException {
        while (true) {
            if ((userMSG = console.readLine()) != null) {
                out.println(userMSG);
                if (userMSG.equalsIgnoreCase("close") || userMSG.equalsIgnoreCase("exit")) break;
            }
        }
    }

    void readMSG() throws IOException {
        while (true) {
            if ((serverMSG = in.readLine()) != null) {
                System.out.println(serverMSG);
            }
        }
    }

    void close() throws IOException {
        out.close();
        in.close();
        console.close();
        socket.close();
    }
}