package ru.itis.tsisa.blockchain.commands;

import ru.itis.tsisa.blockchain.exceptions.CommandParseException;

import java.util.HashMap;
import java.util.Map;

public enum Command {

    ADD_TRANSACTIONS_BLOCKS("/add_transactions_blocks"),
    CHECK_LAST_BLOCK("/check_last_block"),
    CHECK_BLOCK("/check_block"),
    VERIFY_BLOCK_CHAIN("/verify_block_chain"),
    HELP("/help"),
    EXIT("/exit"),
    SAVE_ANNS("/save_anns");

    private static final Map<String, Command> map;

    private final String value;

    static {
        map = new HashMap<>();
        for (Command command: Command.values()) {
            map.put(command.value, command);
        }
    }

    Command(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public static Command findByStringValue(String value) {
        Command resultCommand = map.get(value);
        if (resultCommand == null){
            throw new CommandParseException();
        }
        return resultCommand;
    }
}
