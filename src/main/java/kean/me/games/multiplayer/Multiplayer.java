package kean.me.games.multiplayer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.awt.*;

public interface Multiplayer {
    User[] getOpponents();
    boolean multi_channeled();

    static MessageCreateData request(Multiplayer multiplayer){
        StringBuilder builder = new StringBuilder("**Awaiting...**\n");
        for (User user : multiplayer.getOpponents()){
            builder.append("<@").append(user.getIdLong()).append(">\n");
        }
        return MessageCreateData.fromEmbeds(new EmbedBuilder()
                .setTitle("Accept request")
                .setColor(Color.GREEN)
                .setDescription(builder.toString())
                .build());
    }
}
