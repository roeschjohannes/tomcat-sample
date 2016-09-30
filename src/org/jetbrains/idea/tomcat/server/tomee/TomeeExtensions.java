package org.jetbrains.idea.tomcat.server.tomee;

import com.intellij.javaee.oss.server.JavaeeExtensionsBase;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;

/**
 * @author michael.golubev
 */
@State(name = "TomeeExtensions", storages = @Storage("tomee.extensions.xml"))
public class TomeeExtensions extends JavaeeExtensionsBase {

  public static TomeeExtensions getInstance() {
    return ServiceManager.getService(TomeeExtensions.class);
  }

  TomeeExtensions() {
    super("TomeeExtensions");
  }
}
