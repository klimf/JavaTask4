import java.io.*;
import java.util.*;

public class Logger {
    private static int id;
    private String basePath = "src/main/resources/";
    private String configPath = basePath + "logging.properties";
    private Map<String, String> config;
    private String logPath;
    private String logFormat;
    private Level globalLevel;
    private boolean consoleLog;

    enum Level {
        OFF,
        ERROR,
        WARNING,
        INFO,
        DEBUG,
        ALL
    }


    public Logger() {
        setConfig();
    }

    public void setConfig() {
        config = getConfig();
        if (!config.containsKey("CONSOLE_LOG")) {
            config.put("CONSOLE_LOG", "false");
        }
        if (!config.containsKey("LOG_PATH")) {
            config.put("LOG_PATH", "logs/log.txt");
        }
        if (!config.containsKey("LOG_FORMAT")) {
            config.put("LOG_FORMAT", "#<id> Date: <date>; Level: <level>; message: <message>");
        }
        if (!config.containsKey("LOG_LEVEL")) {
            config.put("LOG_LEVEL", "ALL");
        }
        globalLevel = Level.valueOf(config.get("LOG_LEVEL"));
        this.logPath = config.get("LOG_PATH");
        this.logFormat = config.get("LOG_FORMAT");
    }



    private void log(String log, Level level) {
        Map<String, String> props = new HashMap<String, String>() {
        };
        props.put("date", new Date().toString());
        props.put("id", id + "");
        props.put("level", level.name());
        props.put("message", log);

        log = replace(config.get("LOG_FORMAT"), props);
        //System.out.println("level.ordinal() = " + level.ordinal() + "globalLevel.ordinal() = " + globalLevel.ordinal());
        if (level.ordinal() >= globalLevel.ordinal() /*|| globalLevel!=Level.OFF*/) {
            if (Objects.equals(config.get("CONSOLE_LOG"), "false")) {
                File logFile = new File(logPath);
                try (Writer writer = new FileWriter(logFile, true)) {
                    writer.write(log + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(log);
            }
        }
        id++;
    }

    public void info(String log) {
        log(log, Level.INFO);
    }

    public void warning(String log) {
        log(log, Level.WARNING);
    }

    public void debug(String log) {
        log(log, Level.DEBUG);
    }

    public void error(String log) {
        log(log, Level.ERROR);
    }

    private String replace(String string, Map<String, String> props) {
        Set<String> keys = props.keySet();
        for (String key : keys) {
            String tag = "<" + key + ">";
            string = string.replaceAll(tag, props.get(key));
        }
        return string;
    }

    private Map<String, String> getConfig() {
        String buffer = "";
        String key = "";
        String value = "";
        Map<String, String> configs = new HashMap<>();
        try (Reader reader = new FileReader(configPath)) {
            int character;
            while ((character = reader.read()) != -1) {
                if (character == '=') {
                    key = buffer;
                    buffer = "";
                } else if (character == '\n') {//fixme
                    value = buffer;
                    buffer = "";
                    configs.put(key, value);
                } else {
                    buffer += (char) character;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configs;
    }

    public Level getLevel() {
        return globalLevel;
    }

    public void setLevel(Level globalLevel) {
        this.globalLevel = globalLevel;
    }
}
