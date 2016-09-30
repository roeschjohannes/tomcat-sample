/*
 * User: anna
 * Date: 13-May-2010
 */
package org.jetbrains.idea.tomcat;

import com.intellij.javaee.run.configuration.J2EEConfigurationProducer;
import org.jetbrains.idea.tomcat.server.TomcatConfiguration;

public class TomcatConfigurationProducer extends J2EEConfigurationProducer {

  public TomcatConfigurationProducer() {
    super(TomcatConfiguration.getInstance());
  }
}
