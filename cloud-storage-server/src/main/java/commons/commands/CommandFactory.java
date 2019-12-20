package commons.commands;

public class CommandFactory {
    public Command createCommand(CommandTypes commandType) {
        Command command = null;
        switch (commandType) {
            case LOGIN:
                command = new LoginCommand();
                break;
            case LOGOUT:
                command = new LogoutCommand();
                break;
            case UPLOAD:
                command = new UploadCommand();
                break;
            case DOWNLOAD:
                command = new DownloadCommand();
                break;
            default:
                throw new IllegalArgumentException("Wrong command type:" +commandType);
        }
        return command;
    }
}
