import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CurrencyConverter {
    private static final String API_BASE_URL = "https://api.exchangerate-api.com/v4/latest/";

    private static Map<String, Double> getExchangeRates() throws IOException {
        URL url = new URL(API_BASE_URL + "USD");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        Gson gson = new Gson();
        ExchangeRatesResponse ratesResponse = gson.fromJson(response.toString(), ExchangeRatesResponse.class);

        return ratesResponse.getRates();
    }

    static class ExchangeRatesResponse {
        private Map<String, Double> rates;

        public Map<String, Double> getRates() {
            return rates;
        }
    }

    private static int displayMenuAndGetOption(Scanner scanner) {
        System.out.println("Escolha uma opção:");
        System.out.println("1. Dólar para Peso Argentino");
        System.out.println("2. Peso Argentino para Dólar");
        System.out.println("3. Dólar para Real Brasileiro");
        System.out.println("4. Real Brasileiro para Dólar");
        System.out.println("5. Dólar para Peso Colombiano");
        System.out.println("6. Peso Colombiano para Dólar");
        System.out.println("0. Sair");

        return scanner.nextInt();
    }

    private static double convertCurrency(int option, double amount, Map<String, Double> rates) {
        double exchangeRate;
        switch (option) {
            case 1:
                exchangeRate = rates.get("ARS"); // Taxa de conversão de dólar para peso argentino
                break;
            case 2:
                exchangeRate = 1 / rates.get("ARS"); // Taxa de conversão de peso argentino para dólar
                break;
            case 3:
                exchangeRate = rates.get("BRL"); // Taxa de conversão de dólar para real brasileiro
                break;
            case 4:
                exchangeRate = 1 / rates.get("BRL"); // Taxa de conversão de real brasileiro para dólar
                break;
            case 5:
                exchangeRate = rates.get("COP"); // Taxa de conversão de dólar para peso colombiano
                break;
            case 6:
                exchangeRate = 1 / rates.get("COP"); // Taxa de conversão de peso colombiano para dólar
                break;
            default:
                System.out.println("Opção inválida.");
                return 0;
        }
        return amount * exchangeRate;
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            Map<String, Double> exchangeRates = getExchangeRates();

            int option;
            do {
                option = displayMenuAndGetOption(scanner);
                if (option != 0) {
                    System.out.println("Digite o valor a ser convertido:");
                    double amount = scanner.nextDouble();

                    double convertedAmount = convertCurrency(option, amount, exchangeRates);
                    System.out.println("Valor convertido: " + convertedAmount);
                }
            } while (option != 0);
        } catch (IOException e) {
            System.err.println("Erro ao conectar à API: " + e.getMessage());
        }
    }
}
