package jp.swest.ledcamp;


import jp.swest.ledcamp.setting.SettingManager;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	public void start(BundleContext context) {
		SettingManager manager = SettingManager.getInstance();
	}

	public void stop(BundleContext context) {
	}
}
