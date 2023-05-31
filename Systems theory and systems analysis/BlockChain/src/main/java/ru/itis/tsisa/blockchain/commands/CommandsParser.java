package ru.itis.tsisa.blockchain.commands;

import org.springframework.stereotype.Component;
import ru.itis.tsisa.blockchain.exceptions.CommandParseException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CommandsParser {

    private final Map<Command, ConsoleCommand> commands;

    public CommandsParser(
            List<? extends ConsoleCommand> consoleCommands
    ){
        this.commands = consoleCommands.stream()
                .collect(Collectors.toMap(ConsoleCommand::getCommandType, consoleCommand -> consoleCommand));
    }

    public ConsoleCommand parseCommand(String commandLine){
        Command command = getCommandByCommandString(commandLine);
        return getConsoleCommandByCommand(command);
    }

    private Command getCommandByCommandString(String commandLine){
        commandLine = prepareCommandLine(commandLine);
        return Command.findByStringValue(commandLine);
    }

    private String prepareCommandLine(String commandLine){
        return commandLine.trim().toLowerCase();
    }

    private ConsoleCommand getConsoleCommandByCommand(Command command){
        ConsoleCommand resultCommand = commands.get(command);
        if (resultCommand == null){
            throw new CommandParseException();
        }
        return resultCommand;
    }

}
