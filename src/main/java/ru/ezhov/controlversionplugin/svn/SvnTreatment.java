package ru.ezhov.controlversionplugin.svn;

/**
 * @author ezhov_da
 */
public class SvnTreatment {
//    public synchronized SvnLoad loadList() throws IOException
//    {
//        FileWriter fileWriter = null; //для тестирования записи
//        InputStreamReader fileReader = null;
//        try
//        {
//            XStream xStream = new XStream(new DomDriver());
//            Annotations.configureAliases(xStream, Command.class);
//            Annotations.configureAliases(xStream, Svn.class);
//            Annotations.configureAliases(xStream, SvnLoad.class);
////читаем объект---------------------------------------------------------------------------------------------------------------------
//            fileReader =
//                    new InputStreamReader(getClass()
//                            .getResourceAsStream("/svns.xml"), "UTF8");
//            Object o = xStream.fromXML(fileReader);
//            SvnLoad svnLoad = (SvnLoad) o;
//            return svnLoad;
////пишем объект----------------------------------------------------------------------------------------------------------------------
////            INSTANCE = new SvnLoad();
////            Command command = new Command();
////            command.setArgument("asdsad");;
////            command.setMethodFile("asdsad");
////            command.setName("asdsad");;
////            command.setPathToCommand("asdsad");;
////            Svn svn = new Svn();
////            List<Command> commands = new ArrayList<Command>();
////            commands.add(command);
////            svn.setCommands(commands);
////            svn.setName("test");
////            List<Svn> svns = new ArrayList<Svn>();
////            svns.add(svn);
////            INSTANCE.setListSvn(svns);
////            fileWriter = new FileWriter(new File(NAME_FILE_SVN));
////            xStream.toXML(INSTANCE, fileWriter);
////------------------------------------------------------------------------------------------------------------------------------------------
//        } finally
//        {
//            if (fileReader != null)
//            {
//                fileReader.close();
//            }
//        }
//    }
}
