package kean.me.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface SlashEvent {
    void onSlash(SlashCommandInteractionEvent event);
}
