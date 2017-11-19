package jp.swest.ledcamp

import jp.swest.ledcamp.setting.SettingManager
import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext

class Activator implements BundleActivator {
    override start(BundleContext context) {
        var manager = SettingManager.getInstance
        manager.load()
    }

    override stop(BundleContext context) {
    }
}
