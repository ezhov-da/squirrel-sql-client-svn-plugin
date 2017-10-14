package ru.ezhov.controlversionplugin.svn;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ezhov_da
 */
public class SvnLoad {

    private List<Svn> listSvn;

    public List<Svn> getListSvn() {
        List<Svn> svns = new ArrayList<>();

        Svn svn = new Svn();
        svn.setName("tortoise");

        List<Command> commands = new ArrayList<>();
        Command command = new Command();
        command.setArgument("/Command:commit /path:");
        command.setMethodFile("getAbsolutePath");
        command.setName("commit");
        command.setPathToCommand("cmd /c C:\\\"Program Files\"\\TortoiseSVN\\bin\\TortoiseProc.exe ");
        commands.add(command);

        command = new Command();
        command.setArgument("/Command:update /path:");
        command.setMethodFile("getAbsolutePath");
        command.setName("update");
        command.setPathToCommand("cmd /c C:\\\"Program Files\"\\TortoiseSVN\\bin\\TortoiseProc.exe ");
        commands.add(command);

        command = new Command();
        command.setArgument("/Command:log /path:");
        command.setMethodFile("getAbsolutePath");
        command.setName("log");
        command.setPathToCommand("cmd /c C:\\\"Program Files\"\\TortoiseSVN\\bin\\TortoiseProc.exe ");
        commands.add(command);

        command = new Command();
        command.setArgument("/Command:diff /path:");
        command.setMethodFile("getAbsolutePath");
        command.setName("diff");
        command.setPathToCommand("cmd /c C:\\\"Program Files\"\\TortoiseSVN\\bin\\TortoiseProc.exe ");
        commands.add(command);

        svn.setCommands(commands);

        svns.add(svn);
        listSvn = svns;
        return svns;
    }

    public void setListSvn(List<Svn> listSvn) {
        this.listSvn = listSvn;
    }
}
