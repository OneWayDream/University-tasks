package ru.itis.tsisa.blockchain.commands;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommandsBundle {

    private final List<DescribedCommand> commands;

    public CommandsBundle(
            List<? extends DescribedCommand> consoleCommands
    )
    {
        this.commands = consoleCommands.stream().map(consoleCommand -> (DescribedCommand) consoleCommand).toList();
    }

    public List<DescribedCommand> getCommands(){
        return commands;
    }

}
