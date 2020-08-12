package nl.melvinsnijders.minetopiaeconomy.utils;

import lombok.SneakyThrows;
import nl.melvinsnijders.minetopiaeconomy.MinetopiaEconomy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Statistics extends BukkitRunnable {

    private final String SERVER_URL = "http://127.0.0.1:8000/api/statistic/";

    private MinetopiaEconomy plugin;
    private UUID statisticsId;
    private HttpClient httpClient;

    public Statistics(MinetopiaEconomy plugin) {

        this.plugin = plugin;
        this.httpClient = HttpClients.createDefault();

        this.init();
        this.runTaskTimerAsynchronously(plugin, 10 * 20L, 5 * 60 * 20L); // Send data through every 5 minutes.

    }

    @Override
    public void run() {

        this.sendServerData(true);

    }

    @SneakyThrows
    private void sendServerData(boolean serverStatus) {

        HttpPost httpPost = new HttpPost(this.SERVER_URL + this.statisticsId);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("plugin_id", "1"));
        params.add(new BasicNameValuePair("plugin_version", this.plugin.getDescription().getVersion().replaceAll("-SNAPSHOT", "")));
        params.add(new BasicNameValuePair("minecraft_version", "1.16.1"));
        params.add(new BasicNameValuePair("status", serverStatus ? "1" : "0"));
        params.add(new BasicNameValuePair("players", String.valueOf(Bukkit.getOnlinePlayers().size())));

        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        httpPost.setHeader("User-Agent", "PluginStatistics/1.0.0");

        HttpResponse response = this.httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();

        String text = new BufferedReader(new InputStreamReader(entity.getContent())).lines().collect(Collectors.joining("\n"));
        Logger.log(Logger.Severity.DEBUG, text);

    }

    public void shutdown() {
        this.sendServerData(false);
    }

    @SneakyThrows
    private void init() {

        File file = new File(this.plugin.getDataFolder() + "/data/statistics.json");

        if(!file.exists()) {

            file.createNewFile();

            this.statisticsId = UUID.randomUUID();

            JSONObject json = new JSONObject();
            json.put("statistic_id", this.statisticsId.toString());
            String jsonString = json.toJSONString();

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonString);
            fileWriter.close();
            return;

        }

        FileReader fileReader = new FileReader(file);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);
        fileReader.close();

        this.statisticsId = UUID.fromString((String) jsonObject.get("statistic_id"));

    }

}
