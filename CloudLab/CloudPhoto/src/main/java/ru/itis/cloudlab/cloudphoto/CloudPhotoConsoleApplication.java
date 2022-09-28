package ru.itis.cloudlab.cloudphoto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.cloudlab.cloudphoto.commands.CommandsParser;
import ru.itis.cloudlab.cloudphoto.commands.ConsoleCommand;
import ru.itis.cloudlab.cloudphoto.exceptions.CommandParseException;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class CloudPhotoConsoleApplication {

    private final CommandsParser commandsParser;
    @Getter
    private static final Scanner commandLineScanner = new Scanner(System.in);

    public void start(String command){
//        ConsoleCommand command = getNextConsoleCommand();
        try{
            ConsoleCommand consoleCommand = handleCommandLine(command);
            handleNextConsoleCommand(consoleCommand);
        } catch (CommandParseException ex){
            System.err.println("Unknown command.");
            System.exit(1);
        } catch (Exception ex){
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        System.exit(0);
    }

    private void handleNextConsoleCommand(ConsoleCommand command){
        command.execute();
    }

//    private ConsoleCommand getNextConsoleCommand(){
//        String commandLine = commandLineScanner.nextLine();
//        return handleCommandLine(commandLine);
//    }

    private ConsoleCommand handleCommandLine(String commandLine){
        return commandsParser.parseCommand(commandLine);
    }

}
