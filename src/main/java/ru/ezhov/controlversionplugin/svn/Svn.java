package ru.ezhov.controlversionplugin.svn;

import java.util.List;

/**
 * класс, который содержит список комманд для конкретного svn
 * <p>
 *
 * @author ezhov_da
 */
public class Svn {

    private String name;
    private List<Command> commands;

    public String getName() {
        return name;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public String toString() {
        return name;
    }
}
