package ru.itis.tsisa.blockchain.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HelpCommand implements ConsoleCommand {

    private final CommandsBundle bundle;

    @Override
    public void execute() {
        String helpContent = getHelpContent();
        System.out.println(helpContent);
    }

    @Override
    public Command getCommandType() {
        return Command.HELP;
    }

    private String getHelpContent(){
        return bundle.getCommands().stream()
                .map(DescribedCommand::getInfo)
                .collect(Collectors.joining("\n"));
    }

}
