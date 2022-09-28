package ru.itis.cloudlab.cloudphoto.commands;

import lombok.ToString;
import ru.itis.cloudlab.cloudphoto.exceptions.CommandParseException;

import java.util.HashMap;
import java.util.Map;

@ToString
public enum Command {

    UPLOAD("upload"),
    DOWNLOAD("download"),
    LIST("list"),
    DELETE("delete"),
    MK_SITE("mksite"),
    INIT("init");

    private static final Map<String, Command> map;

    private final String value;
    private final Map<String, String> arguments = new HashMap<>();

    static {
        map = new HashMap<>();
        for (Command command: Command.values()) {
            map.put(command.value, command);
        }
    }

    Command(String value){
        this.value = value;
    }
    public Map<String, String> getArguments(){
        return new HashMap<>(arguments);
    }

    public static Command getByCommandLine(String commandLine) {
        Command resultCommand = getCommandFromCommandLine(commandLine);
        if (resultCommand == null){
            throw new CommandParseException();
        }
        return resultCommand;
    }

    private static Command getCommandFromCommandLine(String commandLine){
        Command resultCommand = initCommandFromCommandLine(commandLine);
        setCommandArguments(resultCommand, commandLine);
        return resultCommand;
    }

    private static Command initCommandFromCommandLine(String commandLine){
        String[] commandUnits = commandLine.split(" ");
        String commandPart = commandUnits[0].toLowerCase();
        return map.get(commandPart);
    }

    private static void setCommandArguments(Command command, String commandLine){
        Map<String, String> args = new HashMap<>();
        String[] commandArgumentUnits = commandLine.split(" --");
        boolean isAnyArguments = checkIsAnyArguments(commandArgumentUnits);
        String currentArgumentString, argumentKey, argumentValue;
        if (isAnyArguments){
            for (int i = 1; i < commandArgumentUnits.length; i++) {
                currentArgumentString = commandArgumentUnits[i];
                argumentKey = currentArgumentString.split(" ")[0].toLowerCase();
                if (currentArgumentString.length() <= argumentKey.length() + 1){
                    throw new IllegalArgumentException("Empty argument --" + argumentKey);
                }
                argumentValue = currentArgumentString.substring(argumentKey.length() + 1);
                args.put(argumentKey, argumentValue);
            }
            command.arguments.putAll(args);
        }
    }

    private static boolean checkIsAnyArguments(String[] commandArgumentUnits){
        return commandArgumentUnits.length > 1;
    }
}
