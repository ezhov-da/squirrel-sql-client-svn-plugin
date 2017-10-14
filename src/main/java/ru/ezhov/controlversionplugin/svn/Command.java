package ru.ezhov.controlversionplugin.svn;

/**
 * класс, который содержит информацию о комманде для svn
 * <p>
 *
 * @author ezhov_da
 */
public class Command {

    private String name;
    private String pathToCommand;
    private String argument;
    private String methodFile;

    public void setName(String name) {
        this.name = name;
    }

    public void setPathToCommand(String pathToCommand) {
        this.pathToCommand = pathToCommand;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public void setMethodFile(String methodFile) {
        this.methodFile = methodFile;
    }

    public String getName() {
        return name;
    }

    public String getPathToCommand() {
        return pathToCommand;
    }

    public String getArgument() {
        return argument;
    }

    public String getMethodFile() {
        return methodFile;
    }

    @Override
    public String toString() {
        return name;
    }

}
