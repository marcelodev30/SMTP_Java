import java.io.*;
import java.net.*;
import java.util.Base64;

public class SmtpClient {
    public static void main(String[] args) {
        String smtpServer = "smtp.mailtrap.io";
        int smtpPort = 2525;
        String user = "";  
        String password = ""; 

        try(Socket socket = new Socket(smtpServer, smtpPort)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Resposta do servidor: " + reader.readLine());
            sendCommand(writer, reader, "HELO " + smtpServer);
            sendCommand(writer, reader, "AUTH LOGIN");
            sendCommand(writer, reader, Base64.getEncoder().encodeToString(user.getBytes()));  
            sendCommand(writer, reader, Base64.getEncoder().encodeToString(password.getBytes()));  
            sendCommand(writer, reader, "MAIL FROM:<seu-email@mail.com>");
            sendCommand(writer, reader, "RCPT TO:<destinatario@mail.com>");
            sendCommand(writer, reader, "DATA");
            writer.println("Subject: Teste de e-mail");
            writer.println("From: seu-email@mail.com");
            writer.println("To: destinatario@mail.com");
            writer.println(); 
            writer.println("Este é o corpo do e-mail.");
            writer.println(".");  
            System.out.println("Resposta: " + reader.readLine());
            sendCommand(writer, reader, "QUIT");
            socket.close();
            System.out.println("Conexão encerrada.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendCommand(PrintWriter writer, BufferedReader reader, String command) throws IOException {
        writer.println(command);  
        System.out.println("Enviado: " + command);
        String response = reader.readLine();  
        System.out.println("Resposta do servidor: " + response);
        if (!response.startsWith("250") && !response.startsWith("354") && !response.startsWith("221")) {
            throw new IOException("Erro ao processar comando: " + response);
        }
    }
}
