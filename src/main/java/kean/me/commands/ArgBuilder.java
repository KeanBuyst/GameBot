package kean.me.commands;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class ArgBuilder {
    private final String name;
    private final OptionType type;
    private String description = "...";
    private boolean required = true;
    private boolean autoComplete = true;

    private ArgBuilder(String name,OptionType type){
        this.name = name;
        this.type = type;
    }

    public ArgBuilder description(String description){
        this.description = description;
        return this;
    }
    public ArgBuilder required(boolean required){
        this.required = required;
        return this;
    }
    public ArgBuilder autoComplete(boolean autoComplete){
        this.autoComplete = autoComplete;
        return this;
    }
    public OptionData build(){
        if (type == OptionType.USER || type == OptionType.MENTIONABLE) autoComplete = false;
        return new OptionData(type,name,description,required,autoComplete);
    }

    public static ArgBuilder create(String name, OptionType type){
        return new ArgBuilder(name,type);
    }
}
