package ru.ezhov.controlversionplugin;

import net.sourceforge.squirrel_sql.client.plugin.DefaultSessionPlugin;
import net.sourceforge.squirrel_sql.client.plugin.PluginSessionCallback;
import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.client.session.mainpanel.BaseMainPanelTab;
import net.sourceforge.squirrel_sql.client.session.mainpanel.IMainPanelTab;

import java.awt.*;
import java.util.logging.Logger;

/**
 * Как то так
 * <p>
 *
 * @author ezhov_da
 */
public class SubversionPlugin extends DefaultSessionPlugin {
    private static final Logger LOG = Logger.getLogger(SubversionPlugin.class.getName());

    @Override
    public String getInternalName() {
        return "Subversion plugin";
    }

    @Override
    public String getDescriptiveName() {
        return "Subversion plugin";
    }

    @Override
    public String getAuthor() {
        return "Ezhov D.A.";
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    @Override
    public PluginSessionCallback sessionStarted(ISession is) {

        try {
            final PanelFileTree panelFileTree = new PanelFileTree(is);
            IMainPanelTab iMainTab = new BaseMainPanelTab() {

                @Override
                protected void refreshComponent() {
                    //
                }

                @Override
                public String getTitle() {
                    return "Subversion";
                }

                @Override
                public String getHint() {
                    return "";
                }

                @Override
                public Component getComponent() {
                    return panelFileTree;
                }
            };

            is.addMainTab(iMainTab);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }
}
