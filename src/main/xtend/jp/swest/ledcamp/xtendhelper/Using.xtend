package jp.swest.ledcamp.xtendhelper

import java.io.Closeable

class Using {
    def static <T extends Closeable, R> R using(T resource, (T)=>R proc) {
        var Throwable throwable = null
        try {
            return proc.apply(resource)
        } catch (Throwable t) {
            throwable = t
            throw t
        } finally {
            if (throwable == null) {
                resource.close
            } else {
                try {
                    resource.close
                } catch (Throwable unused) {
                    // ignore because throwable is present
                }
            }
        }
    }
}
