package ru.itis.tsisa.blockchain.commands;

public interface ConsoleCommand {

    void execute();
    Command getCommandType();

}
