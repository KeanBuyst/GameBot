package kean.me;

import kean.me.commands.ArgBuilder;
import kean.me.commands.CommandFactory;
import kean.me.commands.CommandManager;
import kean.me.games.NACGame;
import kean.me.games.SnakeGame;
import kean.me.events.Input;
import kean.me.games.multiplayer.Multiplayer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import kean.me.storage.DataBase;

import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class Main {

    public static final DataBase DATA_BASE = new DataBase();
    public static final Timer TIMER = new Timer();

    public static void main(String[] args) {
        CommandManager manager = new CommandManager();
        JDA client = JDABuilder.create(System.getenv("TOKEN"),List.of(GatewayIntent.GUILD_PRESENCES,GatewayIntent.MESSAGE_CONTENT,GatewayIntent.GUILD_MEMBERS,GatewayIntent.GUILD_MESSAGES)).build();

        manager.register(CommandFactory.create("snake")
                .description("A classic game of snake.")
                .onSlash(event -> {
                    // Create game
                    SnakeGame snake = new SnakeGame(event.getUser());
                    event.reply(MessageCreateData.fromEmbeds(snake.view()))
                            .addActionRow(snake.getInputs())
                            .queue(snake::start);
                })
                .build());

        manager.register(CommandFactory.create("Noughts-Crosses")
                .description("A games of noughts and crosses. One user is X other user is 0")
                .addArg(ArgBuilder.create("opponent", OptionType.USER)
                        .description("The user you will be versing in noughts and crosses")
                        .build())
                .onSlash(event -> {
                    User opponent = event.getOption("opponent").getAsUser();
                    if (opponent.getIdLong() == event.getUser().getIdLong()){
                        event.reply("You can't verse yourself. No friends lol?").setEphemeral(true).queue();
                        return;
                    }
                    NACGame game = new NACGame(event.getUser(),opponent);
                    event.reply(Multiplayer.request(game))
                            .addActionRow(Button.success(game.getSessionID()+":ACCEPT","✔"),
                                    Button.danger(game.getSessionID()+":CANCEL","❌"))
                            .queue(r -> r.deleteOriginal().queueAfter(5, TimeUnit.MINUTES));
                }).build());

        client.addEventListener(manager,new Input());
    }
}