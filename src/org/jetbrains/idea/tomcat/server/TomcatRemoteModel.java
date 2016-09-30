package org.jetbrains.idea.tomcat.server;

import com.intellij.execution.configurations.RuntimeConfigurationError;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.javaee.oss.server.JavaeeServerInstance;
import com.intellij.javaee.oss.transport.SimpleRemoteServerModel;
import com.intellij.javaee.oss.transport.SimpleRemoteServerModelData;
import com.intellij.javaee.oss.transport.SimpleRemoteServerModelDelegate;
import com.intellij.javaee.run.configuration.CommonModel;
import com.intellij.javaee.transport.TransportTarget;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.util.xmlb.SkipDefaultValuesSerializationFilters;
import com.intellij.util.xmlb.XmlSerializer;
import com.intellij.util.xmlb.annotations.Tag;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.idea.tomcat.TomcatBundle;
import org.jetbrains.idea.tomcat.TomcatRemoteRunConfigurationEditor;
import org.jetbrains.idea.tomcat.admin.TomEEAgentAdminServerImpl;
import org.jetbrains.idea.tomcat.admin.TomcatAdminRemoteServerImpl;
import org.jetbrains.idea.tomcat.admin.TomcatAdminServerBase;

/**
 * Created by IntelliJ IDEA.
 * User: michael.golubev
 */
public class TomcatRemoteModel extends TomcatServerModel implements SimpleRemoteServerModel {

  @NonNls
  private static final String DATA_ELEMENT = "data";

  private int myJndiPort = DEFAULT_JNDI_PORT;

  private SimpleRemoteServerModelDelegate myTransportDelegate = new SimpleRemoteServerModelDelegate().init(this);

  @Override
  protected TomcatAdminServerBase<?> createTomcatServerAdmin(JavaeeServerInstance processHandler, TomEEAgentAdminServerImpl tomEEAdmin) {
    return new TomcatAdminRemoteServerImpl(this, tomEEAdmin);
  }

  public int getJndiPort() {
    return myJndiPort;
  }

  public void setJndiPort(int jndiPort) {
    myJndiPort = jndiPort;
  }

  public String getTransportHostId() {
    return myTransportDelegate.getTransportHostId();
  }

  public void setTransportHostId(String transportHostId) {
    myTransportDelegate.setTransportHostId(transportHostId);
  }

  public TransportTarget getTransportStagingTarget() {
    return myTransportDelegate.getTransportStagingTarget();
  }

  public void setTransportStagingTarget(TransportTarget transportStagingTarget) {
    myTransportDelegate.setTransportStagingTarget(transportStagingTarget);
  }

  public String getStagingRemotePath() {
    return myTransportDelegate.getStagingRemotePath();
  }

  public void setStagingRemotePath(String stagingRemotePath) {
    myTransportDelegate.setStagingRemotePath(stagingRemotePath);
  }

  @Override
  public boolean isDeployAllowed() {
    return myTransportDelegate.isDeployAllowed();
  }

  @Override
  public String prepareDeployment(String sourcePath, boolean prepareNameOnly) throws RuntimeConfigurationException {
    return myTransportDelegate.prepareDeployment(sourcePath, prepareNameOnly);
  }

  @Override
  public boolean isUseJmx() {
    return super.isUseJmx() && hasDeployments();
  }

  public boolean hasDeployments() {
    return !getCommonModel().getDeploymentModels().isEmpty();
  }

  @Override
  public void checkConfiguration() throws RuntimeConfigurationException {
    if (isVersion5OrHigher()) {
      super.checkConfiguration();
      if (hasDeployments() && myJndiPort <= 0) {
        throw new RuntimeConfigurationError(TomcatBundle.message("error.jmx.port.not.specified"));
      }
      myTransportDelegate.checkConfiguration();
    }
    else {
      if (hasDeployments()) {
        throw new RuntimeConfigurationError(TomcatBundle.message("error.remote.deploy.not.supported.for.under.v5"));
      }
    }
  }

  @Override
  public SettingsEditor<CommonModel> getEditor() {
    return new TomcatRemoteRunConfigurationEditor();
  }

  public void readExternal(Element element) throws InvalidDataException {
    Element dataElement = element.getChild(DATA_ELEMENT);
    if (dataElement == null) {
      return;
    }
    final TomcatRemoteModelData settings = XmlSerializer.deserialize(dataElement, TomcatRemoteModelData.class);
    if (settings == null) {
      return;
    }
    myTransportDelegate.readFromData(settings);
    myJndiPort = settings.getJndiPort();
  }

  public void writeExternal(Element element) throws WriteExternalException {
    final TomcatRemoteModelData settings = new TomcatRemoteModelData();
    myTransportDelegate.writeToData(settings);
    settings.setJndiPort(myJndiPort);
    Element dataElement = XmlSerializer.serialize(settings, new SkipDefaultValuesSerializationFilters());
    element.addContent(dataElement);
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    TomcatRemoteModel result = (TomcatRemoteModel)super.clone();
    result.myTransportDelegate = ((SimpleRemoteServerModelDelegate)myTransportDelegate.clone()).init(result);
    return result;
  }

  @Tag(DATA_ELEMENT)
  public static class TomcatRemoteModelData extends SimpleRemoteServerModelData {

    @Tag("jndi-port")
    private int myJndiPort = DEFAULT_JNDI_PORT;


    public int getJndiPort() {
      return myJndiPort;
    }

    public void setJndiPort(int jndiPort) {
      myJndiPort = jndiPort;
    }
  }
}
