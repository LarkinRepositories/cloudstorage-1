package commons.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class LoginCommand implements Command {
    @Getter
    private String login = "";
    @Getter
    private String password = "";


    @Override
    public void operate(String[] input) {
        if (input.length != 2) throw new IllegalArgumentException("incorrect input for login command");
        login = input[0];
        password = input[1];
    }
}
