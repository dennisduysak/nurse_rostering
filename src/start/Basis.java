package start;

import model.schedule.SchedulingPeriod;
import parser.XmlParser;

import java.io.FileWriter;

public class Basis {
    private String file;
    protected FileWriter writer;

    public Basis() {
    }

    /**
     * Instantiates the parser, loads and returns a scheduling period definitions.
     *
     * @param file override from JVM arguments (if any)
     * @return SchedulingPeriod instance or null.
     */
    protected SchedulingPeriod parseSchedulingPeriod(String file) {
        this.file = file;
        XmlParser parser = new XmlParser();

        String filePath = "data/" + this.file + ".xml";

        // try to load and return the desired scheduling period
        return parser.loadFile(filePath);
    }
}
