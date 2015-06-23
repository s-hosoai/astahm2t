package jp.swest.ledcamp;

import jp.swest.ledcamp.setting.SettingManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

@SuppressWarnings("all")
public class Activator implements BundleActivator {
  public void start(final BundleContext context) {
    SettingManager manager = SettingManager.getInstance();
    manager.load();
  }
  
  public void stop(final BundleContext context) {
  }
}
