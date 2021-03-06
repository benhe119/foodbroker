package org.biiig.foodbroker.configuration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by peet on 18.11.14.
 */
public class ProcessConfiguration {

    private Date startDate;
    private Date endDate;
    private int casesPerScaleFactor;

    public ProcessConfiguration(String className){
        try {
            PropertiesConfiguration config = new PropertiesConfiguration("config.properties");
            SimpleDateFormat dateFormatter = new SimpleDateFormat( "yyyy-MM-dd" );

            this.startDate = dateFormatter.parse(config.getString(className+"StartDate"));
            this.endDate = dateFormatter.parse(config.getString(className+"EndDate"));
            this.casesPerScaleFactor = config.getInt(className+"CasesPerScaleFactor");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getCases(int scaleFactor) {
        return scaleFactor * casesPerScaleFactor;
    }
}
