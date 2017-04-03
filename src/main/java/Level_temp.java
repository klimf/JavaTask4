public class Level_temp {
    private final String name;
    private final int level;

    private Level_temp(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public static final Level_temp OFF = new Level_temp("OFF", Integer.MAX_VALUE);
    public static final Level_temp ERROR = new Level_temp("ERROR", 400);
    public static final Level_temp WARNING = new Level_temp("WARNING", 300);
    public static final Level_temp INFO = new Level_temp("INFO", 200);
    public static final Level_temp ALL = new Level_temp("ALL", 100);
    //off error info all
}
