package org.biiig.foodbroker.formatter;

/**
 * Created by peet on 27.11.14.
 */
public class JSONFormatterFactory extends AbstractFormatterFactory {

    @Override
    public Formatter newInstance(String directory) {
        return new JSONFormatter(directory);
    }
}
