import java.io.*;
import java.net.*;
import java.util.Base64;

public class SmtpClient {
    public static void main(String[] args) {
        String smtpServer = "smtp.mailtrap.io";
        int smtpPort = 2525;
        String user = "seu-usuario";  // Substitua com seu usuário
        String password = "sua-senha";  // Substitua com sua senha

        try {
            // Conectar ao servidor SMTP
            Socket socket = new Socket(smtpServer, smtpPort);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Ler a saudação do servidor
            System.out.println("Resposta do servidor: " + reader.readLine());

            // Enviar comando HELO
            sendCommand(writer, reader, "HELO " + smtpServer);

            // Autenticação
            sendCommand(writer, reader, "AUTH LOGIN");
            sendCommand(writer, reader, Base64.getEncoder().encodeToString(user.getBytes()));  // Enviar usuário codificado em Base64
            sendCommand(writer, reader, Base64.getEncoder().encodeToString(password.getBytes()));  // Enviar senha codificada em Base64

            // Definir remetente
            sendCommand(writer, reader, "MAIL FROM:<seu-email@mail.com>");

            // Definir destinatário
            sendCommand(writer, reader, "RCPT TO:<destinatario@mail.com>");

            // Enviar corpo do e-mail
            sendCommand(writer, reader, "DATA");
            writer.println("Subject: Teste de e-mail");
            writer.println("From: seu-email@mail.com");
            writer.println("To: destinatario@mail.com");
            writer.println();  // Linha em branco entre cabeçalhos e corpo
            writer.println("Este é o corpo do e-mail.");
            writer.println(".");  // Finaliza o corpo do e-mail
            System.out.println("Resposta: " + reader.readLine());

            // Finaliza a sessão SMTP
            sendCommand(writer, reader, "QUIT");

            // Fechar o socket
            socket.close();
            System.out.println("Conexão encerrada.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Função para enviar comandos e ler a resposta do servidor
    private static void sendCommand(PrintWriter writer, BufferedReader reader, String command) throws IOException {
        writer.println(command);  // Envia o comando
        System.out.println("Enviado: " + command);
        String response = reader.readLine();  // Lê a resposta do servidor
        System.out.println("Resposta do servidor: " + response);

        // Verificar se a resposta é uma resposta de sucesso
        if (!response.startsWith("250") && !response.startsWith("354") && !response.startsWith("221")) {
            throw new IOException("Erro ao processar comando: " + response);
        }
    }
}
