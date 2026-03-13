package br.inadThebest.model;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WebhookService {

    private static final String WEBHOOK_URL = "https://broad-dusk-64.webhook.cool";

    public static void post(String message) {
        try {
            URL url = new URL(WEBHOOK_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            byte[] input = message.getBytes(StandardCharsets.UTF_8);
            connection.setFixedLengthStreamingMode(input.length);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(input);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != 200 && responseCode != 204) {
                System.err.println("Erro ao enviar webhook: " + responseCode);
            }

        } catch (Exception e) {
            System.err.println("Erro ao postar no webhook: " + e.getMessage());
        }
    }
}
