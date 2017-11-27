package start;

import model.schedule.SchedulingPeriod;
import parser.XmlParser;

public class Basis {

    /**
     * Instantiates the parser, loads and returns a scheduling period definitions.
     * @param file override from JVM arguments (if any)
     * @return SchedulingPeriod instance or null.
     */
    protected SchedulingPeriod parseSchedulingPeriod(String file) {
        XmlParser parser = new XmlParser();

        String filePath = "data/" + file;

        // try to load and return the desired scheduling period
        return parser.loadFile(filePath);
    }
}
