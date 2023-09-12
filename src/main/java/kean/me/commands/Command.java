package kean.me.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class Command {
    private final String name, description;
    private final boolean publish;
    private final SlashEvent event;
    private final OptionData[] options;

    public Command(String name, String description, boolean publish, SlashEvent event, OptionData[] options) {
        this.name = name;
        this.description = description;
        this.publish = publish;
        this.event = event;
        this.options = options;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPublished() {
        return publish;
    }

    public void execute(SlashCommandInteractionEvent event){
        this.event.onSlash(event);
    }

    public OptionData[] getOptions() {
        return options;
    }
}
