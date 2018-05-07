package in.ajm.sb_library.jobscheduling_one.util;

import android.os.SystemClock;

/**
 * @author rwondratschek
 */
public interface Clock {

    long currentTimeMillis();

    long elapsedRealtime();

    Clock DEFAULT = new Clock() {
        @Override
        public long currentTimeMillis() {
            return System.currentTimeMillis();
        }

        @Override
        public long elapsedRealtime() {
            return SystemClock.elapsedRealtime();
        }
    };
}
