package kean.me.games;

import kean.me.Main;
import kean.me.games.Entities.Direction;
import kean.me.games.Entities.Snake;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class SnakeGame extends TileGame {

    private static final String background = "<:0a:1033306207353569350>";

    private final Snake snake = new Snake(0,0);
    private Food food = spawn();

    private int highScore;
    private int score;
    public SnakeGame(User sender) {
        super("Snake",sender);
    }

    public void load(JSONObject gameData) {
        highScore = gameData.getInt("score");
    }

    public Collection<? extends ItemComponent> getInputs() {
        Collection<Button> buttons = new ArrayList<>();
        buttons.add(Button.primary(sessionID+":WEST","◀"));
        buttons.add(Button.primary(sessionID+":NORTH","▲"));
        buttons.add(Button.primary(sessionID+":SOUTH","▼"));
        buttons.add(Button.primary(sessionID+":EAST","▶"));
        return buttons;
    }
    public void onPress(String buttonID, ButtonInteractionEvent event) {
        snake.setDirection(Direction.valueOf(buttonID));
        event.deferEdit().queue();
    }

    protected void onViewTick(EmbedBuilder builder) {
        if (snake.isDead()) {
            end();
            ArrayList<String> names = new ArrayList<>();
            ArrayList<Integer> scores = new ArrayList<>();
            for (Map.Entry<String,Integer> entry : getLeaderBoard().entrySet()){
                names.add(entry.getKey());
                scores.add(entry.getValue());
            }
            StringBuilder b = new StringBuilder("\n");

            b.append("**SCORE**: ").append(score).append('\n');
            b.append("**HIGH SCORE**: ").append(highScore).append("\n\n");
            b.append("-------- LEADER BOARD --------").append("\n");

            int size = names.size()-1;
            for (int i = 0; i < 5; i++){
                b.append(i+1).append(": ").append(names.get(size - i)).append(" *with* ");
                b.append(scores.get(size - i)).append('\n');
            }
            builder.setDescription(b.toString());
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                String view = snake.render(x,y);
                if (view == null){
                    if (food.x == x && food.y == y){
                        stringBuilder.append(Food.view);
                    } else {
                        stringBuilder.append(background);
                    }
                } else {
                    stringBuilder.append(view);
                    if (food.x == x && food.y == y){
                        snake.grow();
                        score++;
                        food = spawn();
                    }
                }
            }
            stringBuilder.append('\n');
        }
        Direction d = bounds(snake.nextX(), snake.nextY());
        if (d != null) snake.setDirection(d);
        snake.move();
        builder.setDescription(stringBuilder.toString());
        builder.setFooter("Score: "+score+"\tHigh Score: "+highScore);
    }

    public void save(JSONObject gameData) {
        if (score > highScore) highScore = score;
        gameData.put("score", highScore);
    }

    private record Food(int x, int y){
        final static String view = "<:1a:1033369625318137856>";
    }

    private Food spawn(){
        int x = (int) (Math.random() * width);
        int y = (int) (Math.random() * height);
        if (snake.isEmptySpace(x,y)){
            return new Food(x,y);
        }
        return spawn();
    }

    private Direction bounds(int x,int y){
        /*
                    ------------- <- Smaller Y
                    |           |
       Smaller X -> |           | <- Greater X
                    |           |
                    ------------- <- Greater Y
         */
        if (x > width-1 || x < 0){
            if (y > height / 2){
                return Direction.NORTH;
            } else {
                return Direction.SOUTH;
            }
        }
        if (y > height-1 || y < 0){
            if (x > width / 2){
                return Direction.WEST;
            } else {
                return Direction.EAST;
            }
        }
        return null;
    }

    private LinkedHashMap<String,Integer> getLeaderBoard(){
        JSONObject[] objects = Main.DATA_BASE.getAllUserData();
        Map<String,Integer> top = new HashMap<>();
        for (JSONObject object : objects){
            if (!object.has("games")) continue;
            JSONObject games = object.getJSONObject("games");
            if (!games.has(name)) continue;
            String user = object.getString("name");
            JSONObject snake = games.getJSONObject(name);
            top.put(user, snake.getInt("score"));
        }
        return top.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a,b)->b, LinkedHashMap::new));
    }
}
