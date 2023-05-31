package ru.itis.tsisa.blockchain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.tsisa.blockchain.commands.CommandsParser;
import ru.itis.tsisa.blockchain.commands.ConsoleCommand;
import ru.itis.tsisa.blockchain.exceptions.CommandParseException;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class BlockChainConsoleApplication {

    private final CommandsParser commandsParser;

    private static boolean isWorking = true;

    @Getter
    private static final Scanner commandLineScanner = new Scanner(System.in);

    public static void shutDown(){
        isWorking = false;
    }

    public void start(){
        displayWelcomeMessage();
        while (isWorking){
            handleNextConsoleCommand();
        }
    }

    private void displayWelcomeMessage(){
        System.out.println("(╮°-°)╮┳━━┳ Simple BlockChain implementation ( ╯°□°)╯ ┻━━┻ ");
    }

    private void handleNextConsoleCommand(){
        try{
            getNextConsoleCommand().execute();
        } catch (CommandParseException ex){
            System.out.println("Unknown command. Use '/help' to get a list of commands.");
        }
    }

    private ConsoleCommand getNextConsoleCommand(){
        String commandLine = commandLineScanner.nextLine();
        return handleCommandLine(commandLine);
    }

    private ConsoleCommand handleCommandLine(String commandLine){
        return commandsParser.parseCommand(commandLine);
    }

}
