package kean.me.games;

import kean.me.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TimerTask;
import java.util.UUID;

public abstract class TileGame {
    public static final ArrayList<TileGame> GAMES = new ArrayList<>();

    protected int width = 10,height = 10;
    protected final String name;
    protected final UUID sessionID;
    protected final User sender;
    private boolean ended = false;
    private TimerTask task;

    public TileGame(String name, User sender){
        this.name = name;
        this.sender = sender;
        sessionID = UUID.randomUUID();

        // Loading data
        JSONObject data = Main.DATA_BASE.read(sender.getId()+".json");
        if (data.has("games")){
            JSONObject games = data.getJSONObject("games");
            if (games.has(name)){
                load(games.getJSONObject(name));
            }
        }

        GAMES.add(this);
        System.out.println("New "+name+" session["+sessionID+"] has been started by "+sender.getName());
        System.out.println(GAMES.size()+" session(s) active");
    }

    public MessageEmbed view(){
        EmbedBuilder builder = new EmbedBuilder().setTitle(name);
        onViewTick(builder);
        if (ended) task.cancel();
        return builder.build();
    }

    public void start(InteractionHook reply){
        task = new TimerTask() {
            public void run() {
                reply.editOriginal(MessageEditData.fromEmbeds(view())).queue();
            }
        };
        Main.TIMER.scheduleAtFixedRate(task,0,1200);
    }
    public void end() {
        ended = true;
        GAMES.remove(this);
        System.out.println("Session["+sessionID+"] closed");

        // Save data
        String file = sender.getId()+".json";
        JSONObject data = Main.DATA_BASE.read(file);
        data.put("name",sender.getName());
        JSONObject games = data.optJSONObject("games", new JSONObject());
        JSONObject game = games.optJSONObject(name,new JSONObject());
        save(game);
        games.put(name,game);
        data.put("games",games);
        Main.DATA_BASE.write(file,data);
        System.out.println("Data saved");
    }

    public abstract void load(JSONObject gameData);
    public abstract Collection<? extends ItemComponent> getInputs();
    public abstract void onPress(String buttonID, ButtonInteractionEvent event);
    protected abstract void onViewTick(EmbedBuilder builder);
    public abstract void save(JSONObject gameData);

    public String getName() {
        return name;
    }
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
    public UUID getSessionID() {
        return sessionID;
    }

    public User getSender() {
        return sender;
    }

    public boolean stopped() {
        return ended;
    }

    public TimerTask getTask() {
        return task;
    }

    public static TileGame getBySession(UUID session){
        for (TileGame game : GAMES){
            if (game.sessionID.equals(session)) return game;
        }
        return null;
    }
}
