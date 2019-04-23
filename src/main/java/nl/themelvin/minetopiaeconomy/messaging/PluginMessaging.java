package nl.themelvin.minetopiaeconomy.messaging;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import nl.themelvin.minetopiaeconomy.MinetopiaEconomy;
import nl.themelvin.minetopiaeconomy.messaging.incoming.AbstractMessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.util.HashMap;

public class PluginMessaging implements PluginMessageListener {

    private static PluginMessaging instance = new PluginMessaging();

    public static PluginMessaging getInstance() {

        return instance;

    }

    private HashMap<String, Class<? extends AbstractMessage>> registeredObjects = new HashMap<>();
    private HashMap<Class<? extends AbstractMessage>, Class<? extends AbstractMessageHandler>> registeredHandlers = new HashMap<>();

    public void register(String input, Class<? extends AbstractMessage> messageClass, Class<? extends AbstractMessageHandler> handlerClass) {

        registeredObjects.put(input, messageClass);
        registeredHandlers.put(messageClass, handlerClass);

    }

    public void send(String type, AbstractMessage data) {

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF("ALL");
        out.writeUTF("MinetopiaEconomy:" + type);

        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);

        try {
            msgout.writeUTF(data.serialize());
        } catch (IOException exception){
            exception.printStackTrace();
        }

        out.writeShort(msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());

        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

        if(player == null) {
            return;
        }

        player.sendPluginMessage(MinetopiaEconomy.getPlugin(), "BungeeCord", out.toByteArray());

    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {

        if(!channel.equals("BungeeCord")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        if(!subchannel.startsWith("MinetopiaEconomy")) {
            return;
        }

        String dataType = subchannel.split(":")[1];
        System.out.println(dataType);
        Class<?> objectType = registeredObjects.get(dataType);

        if(objectType == null) {
            return;
        }

        short len = in.readShort();
        byte[] msgbytes = new byte[len];
        in.readFully(msgbytes);

        DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));

        try {

            String dataString = msgin.readUTF();

            Gson gson = new Gson();
            AbstractMessage messageObject = (AbstractMessage) gson.fromJson(dataString, objectType);
            Class<? extends AbstractMessageHandler> handler = registeredHandlers.get(objectType);

            if(handler == null) {
                return;
            }

            handler.newInstance().execute(messageObject);

        } catch (IOException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

    }

}
