package ru.itis.tsisa.blockchain.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.tsisa.blockchain.BlockChainConsoleApplication;

@Component
@RequiredArgsConstructor
public class ExitCommand implements DescribedCommand {

    @Override
    public void execute() {
        System.out.println("(ﾉ◕ヮ◕)ﾉ*:･ﾟ✧ Bye, have a great time ･ﾟ✧");
        BlockChainConsoleApplication.shutDown();
    }

    @Override
    public Command getCommandType() {
        return Command.EXIT;
    }

    @Override
    public String getInfo() {
        return getCommandType().getValue() + " - Shutdown the application.";
    }

}
