package com.gardencommunity.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Multithreaded activity logger:
 * - single background thread writes logs
 * - synchronized list stores events
 * - admin can read snapshot safely
 */
public class ActivityLogger {

    private static final ActivityLogger INSTANCE = new ActivityLogger();

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final List<String> events = new ArrayList<>();
    private static final int MAX_EVENTS = 200;
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private ActivityLogger() {
    }

    public static ActivityLogger getInstance() {
        return INSTANCE;
    }

    public void log(String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String entry = timestamp + " - " + message;

        // run in background thread
        executor.submit(() -> {
            synchronized (events) {
                events.add(0, entry);              // newest at top
                if (events.size() > MAX_EVENTS) {
                    events.remove(events.size() - 1);
                }
            }
        });
    }

    public List<String> snapshot() {
        synchronized (events) {
            return new ArrayList<>(events); // copy for safe iteration
        }
    }

    // not strictly needed now, but good practice
    public void shutdown() {
        executor.shutdown();
    }
}
