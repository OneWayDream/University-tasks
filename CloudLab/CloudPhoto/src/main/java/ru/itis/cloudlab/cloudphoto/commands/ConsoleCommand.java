package ru.itis.cloudlab.cloudphoto.commands;

import java.util.Map;

public interface ConsoleCommand {

    void execute();
    Command getCommandType();
    void setArguments(Map<String, String> arguments);

}
