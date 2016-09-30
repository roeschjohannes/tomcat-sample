package org.jetbrains.idea.tomcat.descriptor;

import com.intellij.javaee.oss.descriptor.JavaeeWebDescriptor;
import com.intellij.javaee.oss.server.JavaeeIntegration;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.idea.tomcat.TomcatBundle;

/**
 * Created by IntelliJ IDEA.
 * User: michael.golubev
 */
public class TomcatContextDescriptor extends JavaeeWebDescriptor {

  public TomcatContextDescriptor(JavaeeIntegration integration, Class<?> type, @NonNls String name) {
    super(integration, type, name);
  }

  @Override
  protected String getPath() {
    return "META-INF";
  }

  @Override
  protected String getTitle(JavaeeIntegration integration) {
    return TomcatBundle.message("tomcat.deployment.descriptor.title");
  }
}
