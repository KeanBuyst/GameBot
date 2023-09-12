package kean.me.commands;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommandFactory {
    private final String name;
    private String description = "...";
    private SlashEvent event;
    private boolean publish = false;
    private List<OptionData> options = new ArrayList<>();

    private CommandFactory(String name){
        this.name = name;
    }

    public static CommandFactory create(String name){
        return new CommandFactory(name.toLowerCase(Locale.ROOT));
    }
    public CommandFactory description(String description){
        this.description = description;
        return this;
    }
    public CommandFactory publish(boolean publish){
        this.publish = publish;
        return this;
    }
    public CommandFactory onSlash(SlashEvent event){
        this.event = event;
        return this;
    }
    public CommandFactory addArg(OptionData data){
        options.add(data);
        return this;
    }
    public Command build(){
        return new Command(name,description,publish,event,options.toArray(OptionData[]::new));
    }
}
