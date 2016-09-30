package org.jetbrains.idea.tomcat.admin;

import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.javaee.oss.admin.jmx.JmxAdminException;
import org.jetbrains.idea.tomcat.server.TomcatRemoteModel;

/**
 * Created by IntelliJ IDEA.
 * User: michael.golubev
 */
public class TomcatAdminRemoteServerImpl extends TomcatAdminServerBase<TomcatRemoteModel> {

  public TomcatAdminRemoteServerImpl(TomcatRemoteModel serverModel, TomEEAgentAdminServerImpl tomEEAdmin) {
    super(serverModel, tomEEAdmin);
  }

  @Override
  public boolean doConnectTomcat() {
    if (isUseJmx()) {
      return doConnectJmx();
    }
    else {
      return true;
    }
  }

  @Override
  protected String prepareDeployment(String deploymentPath) throws JmxAdminException {
    try {
      return getServerModel().prepareDeployment(deploymentPath, false);
    }
    catch (RuntimeConfigurationException e) {
      throw new JmxAdminException(e);
    }
  }
}
