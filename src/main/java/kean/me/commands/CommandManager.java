package kean.me.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {

    private final List<Command> commands = new ArrayList<>();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String name = event.getName();
        for (Command command : commands){
            if (command.getName().equalsIgnoreCase(name)) command.execute(event);
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        Guild guild = event.getGuild();
        if (guild.getIdLong() == 755814840995414118L || guild.getIdLong() == 348192581424906252L){
            for (Command command : commands){
                if (command.isPublished()) continue;
                guild.upsertCommand(command.getName(),command.getDescription())
                        .addOptions(command.getOptions() == null ? new OptionData[0] : command.getOptions())
                        .queue();
            }
        }
    }

    public void register(Command command){
        commands.add(command);
    }
}
