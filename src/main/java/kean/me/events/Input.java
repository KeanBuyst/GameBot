package kean.me.events;

import kean.me.games.NACGame;
import kean.me.games.TileGame;
import kean.me.games.multiplayer.Multiplayer;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Input extends ListenerAdapter {
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String[] args = event.getButton().getId().split(":");
        UUID session = UUID.fromString(args[0]);
        String buttonId = args[1];
        for (TileGame game : TileGame.GAMES){
            if (game.getSessionID().equals(session)){
                long id = event.getUser().getIdLong();
                if (id != game.getSender().getIdLong() || buttonId.equals("ACCEPT")){
                    if (game instanceof Multiplayer multiplayer) {
                        if (Arrays.stream(multiplayer.getOpponents()).noneMatch(user -> user.getIdLong() == id)) return;
                    } else {
                        return;
                    }
                }
                game.onPress(buttonId,event);
                return;
            }
        }
        event.reply("Session is inactive")
                .setEphemeral(true)
                .queue();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        Message reference = message.getReferencedMessage();
        if (reference == null) return;
        if (reference.getAuthor().getIdLong() != 755479888445833246L) return;
        String[] bArgs = reference.getButtons().get(0).getId().split(":");
        UUID session = UUID.fromString(bArgs[0]);
        long id = message.getAuthor().getIdLong();
        TileGame game = TileGame.getBySession(session);
        if (game instanceof NACGame nac){
            String c;
            if (nac.getOpponents()[0].getIdLong() == id){
                c = "<:1b:1035853532064133170>";
            } else if (nac.getSender().getIdLong() == id){
                c = "<:0b:1035853496911667230>";
            } else {
                return;
            }
            String[] args = message.getContentStripped().trim().split(",");
            if (nac.isTurn(id)){
                if (!nac.place(c,Integer.parseInt(args[0]),Integer.parseInt(args[1]))){
                    message.reply("Invalid position!!").queue(a -> a.delete().queueAfter(5, TimeUnit.SECONDS));
                }
            }
            message.delete().queueAfter(1,TimeUnit.SECONDS);
        }
    }
}
