// Pfad: src/client/java/com/partymann2000/PterodactylApiHelper.java
package com.partymann2000;

import com.google.gson.Gson;
import com.partymann2000.config.ConfigManager;
import com.partymann2000.interfaces.IApiStatusUpdater;
import net.minecraft.client.network.ServerInfo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class PterodactylApiHelper {

    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final Gson GSON = new Gson();

    public static void updateServerStatus(IApiStatusUpdater updater, ServerInfo serverInfo) {
        CompletableFuture.runAsync(() -> {
            try {
                String[] addressParts = serverInfo.address.split(":");
                if (addressParts.length < 2) return;
                int port = Integer.parseInt(addressParts[addressParts.length - 1]);
                Optional<PteroServerInfo> serverInfoOptional = findServerInfoByPort(port);
                updater.xenority_updateApiStatus(serverInfoOptional);
            } catch (Exception e) {
                ExampleMod.LOGGER.error("Fehler beim Abfragen des Pterodactyl-Serverstatus", e);
                updater.xenority_updateApiStatus(Optional.empty());
            }
        });
    }

    public static void sendPowerSignal(String signal, String serverId, IApiStatusUpdater updater, ServerInfo serverInfo) {
        if (serverId == null) {
            return;
        }
        CompletableFuture.runAsync(() -> {
            try {
                updater.xenority_updateApiStatus(Optional.empty());
                postPowerSignal(serverId, signal);
                Thread.sleep(3000);
                updateServerStatus(updater, serverInfo);
            } catch (Exception e) {
                ExampleMod.LOGGER.error("Fehler beim Senden des Power-Signals", e);
                updateServerStatus(updater, serverInfo);
            }
        });
    }

    private static Optional<PteroServerInfo> findServerInfoByPort(int port) throws Exception {
        String url = getCleanEndpoint() + "/api/client";
        ExampleMod.LOGGER.info("Sende GET-Anfrage an: {}", url);

        HttpRequest request = buildBaseRequest(url).GET().build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            ExampleMod.LOGGER.error("Fehler beim Abrufen der Serverliste: Status Code {}", response.statusCode());
            ExampleMod.LOGGER.error("Antwort vom Server: {}", response.body());
            throw new RuntimeException("Fehler beim Abrufen der Serverliste");
        }

        PterodactylServerList serverList = GSON.fromJson(response.body(), PterodactylServerList.class);
        for (PterodactylServer server : serverList.data) {
            if (server.attributes != null && server.attributes.relationships != null && server.attributes.relationships.allocations != null && server.attributes.relationships.allocations.data != null) {
                for (Allocation allocation : server.attributes.relationships.allocations.data) {
                    if (allocation.attributes != null && allocation.attributes.port == port) {
                        return Optional.of(new PteroServerInfo(server.attributes.identifier));
                    }
                }
            }
        }
        return Optional.empty();
    }

    private static void postPowerSignal(String serverId, String signal) throws Exception {
        String jsonPayload = GSON.toJson(new PowerSignal(signal));
        String url = String.format("%s/api/client/servers/%s/power", getCleanEndpoint(), serverId);
        ExampleMod.LOGGER.info("Sende POST-Anfrage an: {}", url);
        HttpRequest request = buildBaseRequest(url).POST(HttpRequest.BodyPublishers.ofString(jsonPayload)).build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 204) {
            ExampleMod.LOGGER.info("Signal '{}' erfolgreich an Server {} gesendet!", signal.toUpperCase(), serverId);
        } else {
            ExampleMod.LOGGER.warn("Fehler beim Senden des Signals an Server {}: Status {}", serverId, response.statusCode());
            ExampleMod.LOGGER.warn("Antwort vom Server: {}", response.body());
        }
    }

    private static HttpRequest.Builder buildBaseRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + ConfigManager.CONFIG.API_KEY)
                .header("Content-Type", "application/json")
                .header("Accept", "application/vnd.pterodactyl.v1+json");
    }

    // ### NEUE HELFER-METHODE ###
    // Diese Methode holt den Endpunkt und entfernt einen eventuellen "/" am Ende.
    private static String getCleanEndpoint() {
        String endpoint = ConfigManager.CONFIG.API_ENDPOINT;
        if (endpoint != null && endpoint.endsWith("/")) {
            return endpoint.substring(0, endpoint.length() - 1);
        }
        return endpoint;
    }

    public record PteroServerInfo(String identifier) {}
    record PowerSignal(String signal) {}
    record PterodactylServerList(List<PterodactylServer> data) {}
    record PterodactylServer(ServerAttributes attributes) {}
    record ServerAttributes(String identifier, Relationships relationships) {}
    record Relationships(Allocations allocations) {}
    record Allocations(List<Allocation> data) {}
    record Allocation(AllocationAttributes attributes) {}
    record AllocationAttributes(int port) {}
}