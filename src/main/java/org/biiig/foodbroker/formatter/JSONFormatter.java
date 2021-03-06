package org.biiig.foodbroker.formatter;

import org.biiig.foodbroker.model.DataObject;
import org.biiig.foodbroker.model.PropertyContainer;
import org.biiig.foodbroker.model.Relationship;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by peet on 18.11.14.
 */
public class JSONFormatter extends AbstractFormatter {

    private final SimpleDateFormat dateFormatter = new SimpleDateFormat( "yyyy-MM-dd");

    private static final String ID = "id";
    private static final String DATA = "data";
    private static final String META = "meta";
    private static final String SOURCE = "source";
    private static final String TARGET = "target";

    public JSONFormatter(String directory) {
        super(directory);
    }

    @Override
    public String getFileExtension() {
        return ".json";
    }

    public String format (DataObject dataObject){
        JSONObject json = new JSONObject();

        json.put(ID, dataObject.getID());
        json.put(DATA, getProperties(dataObject));
        json.put(META, getMetaData(dataObject));

        return json.toString();
    }

    @Override
    public String format(Relationship relationship) {
        JSONObject json = new JSONObject();

        json.put(ID,relationship.getID());
        json.put(SOURCE,relationship.getStartDataObject().getID());
        json.put(TARGET,relationship.getEndDataObject().getID());
        json.put(DATA, getProperties(relationship));
        json.put(META, getMetaData(relationship));

        return json.toString();
    }

    private JSONObject getProperties(PropertyContainer propertyContainer){
        JSONObject json = new JSONObject();

        for(String key : propertyContainer.getPropertyKeys()){
            Object value = propertyContainer.getProperty(key);

            if (value instanceof Date) {
                value = dateFormatter.format((Date) value);
            }
            json.put(key, value);
        }
        return json;
    }

    private JSONObject getMetaData(PropertyContainer propertyContainer){
        JSONObject json = new JSONObject();

        for(String key : propertyContainer.getMetaDataKeys()){
            String value = propertyContainer.getMetaData(key);
            json.put(key,value);
        }
        return json;
    }

    protected String getExtension(){
        return ".json";
    }

    @Override
    public boolean hasSeparateRelationshipHandling() {
        return true;
    }

    @Override
    public String getNodeOpeningFilePath() {
        return null;
    }

    @Override
    public String getNodeFinishFilePath() {
        return null;
    }

    @Override
    public String getEdgeOpeningFilePath() {
        return null;
    }

    @Override
    public String getEdgeFinishFilePath() {
        return null;
    }

    @Override
    public boolean requiresNodeOpening() {
        return false;
    }

    @Override
    public boolean requiresNodeFinish() {
        return false;
    }

    @Override
    public boolean requiresEdgeOpening() {
        return false;
    }

    @Override
    public boolean requiresEdgeFinish() {
        return false;
    }
}
