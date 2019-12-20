package commons.commands;

public interface Command {
    default void operate(String[] input) {
        if (input == null) throw new IllegalArgumentException("incorrect input");
    }
}
