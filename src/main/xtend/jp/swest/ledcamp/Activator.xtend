package jp.swest.ledcamp import org.osgi.framework.BundleContext
import org.osgi.framework.BundleActivator
import jp.swest.ledcamp.setting.SettingManager
import java.io.File
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.util.zip.ZipFile
import java.util.Enumeration
import java.util.zip.ZipEntry
import java.io.IOException
import java.io.FileOutputStream

class Activator implements BundleActivator{
    override start(BundleContext context) {
        var manager = SettingManager.getInstance
        manager.load()
    }        

    override stop(BundleContext context) {
    }
}