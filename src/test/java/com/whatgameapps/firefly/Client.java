package com.whatgameapps.firefly;

import com.google.gson.Gson;
import com.whatgameapps.firefly.rest.AllianceNavCard;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.http.HttpStatus;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

public class Client {
    public static final String NAVCARD_PATH = "/alliance/nav";
    public static final String LOCK_PATH = "/alliance/nav/lock";
    public static final Gson gson = new Gson();

    private String hostPort;

    public Client(String[] args) {
        processCommandLine(args);
    }

    private void processCommandLine(String[] args) {
        if (args.length != 1) {
            System.out.println("Expected 1 args-host:hostPort e.g., localhost:4567, got " + Arrays.asList(args));
            System.exit(-1);
        }
        hostPort = args[0];
        if (hostPort.indexOf(":") < 0) {
            this.hostPort = "localhost:" + hostPort;
        }
    }

    public static void main(String[] args) throws IOException {
        new Client(args).start();
    }

    private void start() throws IOException {
        System.out.println("Connecting to " + getUrl(""));
        char action;
        do {
            System.out.print("1 - draw card\n2 - shuffle\n3 - lock\n4 - unlock\n> ");
            System.out.flush();
            final byte[] buffer = new byte[1000];
            System.in.read(buffer);
            action = (char) buffer[0];
            if ((action >= '0') && (action <= '4'))
                processRequest(action);
        } while (action != '0');
    }

    private void processRequest(int action) {

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            switch (action) {
                case '1':
                    drawCard(httpClient);
                    break;
                case '2':
                    shuffle(httpClient);
                    break;
                case '3':
                    lock(httpClient);
                    break;
                case '4':
                    unlock(httpClient);
                    break;
                default:
                    System.out.println("\nEnter 0 to stop.\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void unlock(CloseableHttpClient httpClient) throws IOException {
        final HttpResponse response = delete(LOCK_PATH, httpClient);
        processResponse(response, "unlock", null);
    }

    private void lock(CloseableHttpClient httpClient) throws IOException {
        final HttpResponse response = post(LOCK_PATH, httpClient);
        processResponse(response, "lock", null);

    }

    private void shuffle(CloseableHttpClient httpClient) throws IOException {
        final HttpResponse response = post(NAVCARD_PATH, httpClient);
        processResponse(response, "shuffle", null);
    }

    private void drawCard(CloseableHttpClient httpClient) throws IOException {
        final HttpResponse response = get(NAVCARD_PATH, httpClient);
        processResponse(response, "draw", this::displayCard);
    }

    private void processResponse(HttpResponse response, String title, Consumer<HttpResponse> successAction) throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        switch (statusCode) {
            case HttpStatus.OK_200:
                System.out.println("Success: " + title);
                if (successAction != null) successAction.accept(response);
                break;
            case HttpStatus.NOT_FOUND_404:
                System.err.println("Needs Shuffling");
                break;
            case HttpStatus.CONFLICT_409:
                System.err.println("Wait until deck unlocked");
                break;
            default:
                System.err.println("unrecognized status " + statusCode + " on " + response.getStatusLine());
        }
        System.out.println();
    }

    private void displayCard(HttpResponse response) {
        try {
            String jsonCard = EntityUtils.toString(response.getEntity());
            AllianceNavCard card = gson.fromJson(jsonCard, AllianceNavCard.class);
            System.out.println(card);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    HttpResponse post(String path, CloseableHttpClient httpClient) throws IOException {
        String url = getUrl(path);
        System.out.println("Posting to " + url);
        HttpPost request = new HttpPost(url);
        return httpClient.execute(request);
    }

    HttpResponse get(String path, CloseableHttpClient httpClient) throws IOException {
        String url = getUrl(path);
        System.out.println("Getting from " + url);
        HttpGet request = new HttpGet(url);
        return httpClient.execute(request);
    }

    HttpResponse delete(String path, CloseableHttpClient httpClient) throws IOException {
        String url = getUrl(path);
        System.out.println("Deleting from " + url);
        HttpDelete request = new HttpDelete(url);
        return httpClient.execute(request);
    }

    private String getUrl(String path) {
        return String.format("http://%s%s", this.hostPort, path);
    }
}
