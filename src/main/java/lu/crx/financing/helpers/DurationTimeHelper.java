package lu.crx.financing.helpers;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
public class DurationTimeHelper {

    public static <T> T measured(Supplier<T> function, String caption) {
        long start = System.nanoTime();
        T result = function.get();
        long stop = System.nanoTime();
        log.info(caption + millisToShortDHMS(stop - start));
        return result;
    }

    public static void measure(Runnable function, String caption) {
        long start = System.nanoTime();
        function.run();
        long stop = System.nanoTime();
        log.info(caption + millisToShortDHMS(stop - start));
    }

    public static String millisToShortDHMS(long nanoDuration) {
        long duration = TimeUnit.NANOSECONDS.toMillis(nanoDuration);
        String res = "";    // java.util.concurrent.TimeUnit;
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        long hours = TimeUnit.MILLISECONDS.toHours(duration) -
                TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
        long millis = TimeUnit.MILLISECONDS.toMillis(duration) -
                TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(duration));

        if (days == 0) res = String.format("%02d:%02d:%02d.%04d", hours, minutes, seconds, millis);
        else res = String.format("%dd %02d:%02d:%02d.%04d", days, hours, minutes, seconds, millis);
        return res;
    }
}
