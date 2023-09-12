package kean.me.games;

import kean.me.games.multiplayer.Multiplayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.json.JSONObject;

import java.util.*;

public class NACGame extends TileGame implements Multiplayer {
    private final User opponent;
    private String winner = null;
    private long isTurn;

    // noughts and crosses
    private final String[][] grid = new String[3][3];
    public NACGame(User sender,User opponent) {
        super("Noughts And Crosses", sender);
        this.opponent = opponent;
        isTurn = sender.getIdLong();
        width = 5;
        height = 5;
        for (String[] row : grid){
            Arrays.fill(row,"<:5b:1035854079877992468>");
        }
    }

    public void load(JSONObject gameData) {}

    public Collection<? extends ItemComponent> getInputs() {
        return Collections.singleton(Button.danger(sessionID + ":CANCEL", "❌"));
    }
    public void onPress(String buttonID, ButtonInteractionEvent event) {
        if (buttonID.equals("ACCEPT")){
            event.reply(MessageCreateData.fromEmbeds(view()))
                    .addActionRow(getInputs())
                    .queue(this::start);
            event.getMessage().delete().queue();
        } else if (buttonID.equals("CANCEL")){
            if (getTask() == null){
                event.getMessage().delete().queue();
            } else {
                end();
                event.deferEdit().queue();
            }
        }
    }

    protected void onViewTick(EmbedBuilder builder) {
        if (stopped()){
            builder.setDescription("**WINNER** is "+winner);
            return;
        }
        StringBuilder b = new StringBuilder();
        for (int y = 1; y <= height; y++){
            for (int x = 1; x <= width; x++){
                if (x % 2 == 0 && y % 2 == 0){
                    b.append("<:4b:1035853677711339570>");
                } else if (x % 2 == 0){
                    b.append("<:2b:1035853577601695814>");
                } else if (y % 2 == 0){
                    b.append("<:3b:1035853648401539093>");
                } else {
                    b.append(grid[(x-1)/2][(y-1)/2]);
                }
            }
            b.append('\n');
        }
        builder.setDescription(b.toString());
    }

    public boolean place(String c, int x, int y){
        if (x > width || x < 0 || y > height || y < 0) return false;
        if (!grid[x][y].equals("<:5b:1035854079877992468>")) return false;
        grid[x][y] = c;
        winner = check();
        if (winner != null) end();
        if (isTurn == sender.getIdLong()){
            isTurn = opponent.getIdLong();
        } else {
            isTurn = sender.getIdLong();
        }
        return true;
    }
    public void save(JSONObject gameData) {}

    public User[] getOpponents() {
        return new User[]{opponent};
    }

    public boolean multi_channeled() {
        return false;
    }

    private String check(){
        if (grid.length == 9) return "nobody (draw)";
        // check straights
        // Y
        for (String[] columns : grid){
            short o = 0;
            short x = 0;
            for (String column : columns){
                if (column.equals("<:1b:1035853532064133170>")){
                    o++;
                } else if (column.equals("<:0b:1035853496911667230>")) {
                    x++;
                }
            }
            if (o == 3) return "<:1b:1035853532064133170>";
            if (x == 3) return "<:0b:1035853496911667230>";
        }
        // X
        for (int i = 0; i < 3; i++){
            short o = 0;
            short x = 0;
            for (String[] columns : grid){
                if (columns[i].equals("<:1b:1035853532064133170>")){
                    o++;
                } else if (columns[i].equals("<:0b:1035853496911667230>")) {
                    x++;
                }
            }
            if (o == 3) return "<:1b:1035853532064133170>";
            if (x == 3) return "<:0b:1035853496911667230>";
        }
        // check diagonals
        return diagonal();
    }

    private String diagonal(){
        short o1 = 0,o2 = 0;
        short x1 = 0,x2 = 0;
        for (int i = 0; i < 3;i++){
            if (grid[i][i].equals("<:1b:1035853532064133170>")){
                o1++;
            } else if (grid[i][i].equals("<:0b:1035853496911667230>")){
                x1++;
            } else if (grid[i][2-i].equals("<:1b:1035853532064133170>")){
                o2++;
            } else if (grid[i][2-i].equals("<:0b:1035853496911667230>")){
                x2++;
            }
        }
        if (o1 == 3 || o2 == 3) return "<:1b:1035853532064133170>";
        if (x1 == 3 || x2 == 3) return "<:0b:1035853496911667230>";
        return null;
    }

    public boolean isTurn(long id){
        return isTurn == id;
    }
}
