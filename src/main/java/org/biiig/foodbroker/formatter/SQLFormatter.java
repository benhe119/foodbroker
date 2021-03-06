package org.biiig.foodbroker.formatter;

import org.biiig.foodbroker.model.DataObject;
import org.biiig.foodbroker.model.PropertyContainer;
import org.biiig.foodbroker.model.Relationship;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by peet on 28.11.14.
 */
public class SQLFormatter extends AbstractFormatter {

    private final SimpleDateFormat dateFormatter = new SimpleDateFormat( "yyyy-MM-dd");

    public SQLFormatter(String directory) {
      super(directory);
    }

    @Override
    public String format(DataObject dataObject) {
        String insertStmt = String.format("INSERT INTO `%s`.`%s`(",
          dataObject.getMetaData("system"), dataObject.getMetaData("class"));

        Map<String,Object> fields = new HashMap<>();

        for (String key : dataObject.getNestedRelationshipKeys()){
            fields.put(key,dataObject
                    .getNestedRelationship(key)
                    .getEndDataObject()
                    .getLocalID());
        }

        return formatInsertStmt(dataObject, insertStmt, fields);
    }

    @Override
    public String format(Relationship relationship) {
        String insertStmt = String.format("INSERT INTO `%s`.`%s`(",
          relationship.getMetaData("system"), relationship.getMetaData("type"));

        Map<String,Object> fields = new HashMap<>();

        fields.put( relationship.getStartAlias(),
                relationship.getStartDataObject().getLocalID());
        fields.put( relationship.getEndAlias(),
                relationship.getEndDataObject().getLocalID());

        return formatInsertStmt(relationship, insertStmt, fields);
    }

    private String formatInsertStmt(PropertyContainer dataObject, String insertStmt, Map<String, Object> fields) {
        for (String key : dataObject.getPropertyKeys()){
            fields.put(key,dataObject
                    .getProperty(key));
        }

      StringBuilder insertStmtBuilder = new StringBuilder();

        boolean isFirstColumn = true;

        for (String column : fields.keySet()){
            insertStmtBuilder.append(
              String.format("%s`%s`", (isFirstColumn ? "" : ","), column));
            isFirstColumn = false;
        }
        isFirstColumn = true;

        insertStmtBuilder.append(") VALUES (");

        for (String column : fields.keySet()){
            Object value = fields.get(column);

            StringBuilder stringBuilder = new StringBuilder();
            if (value instanceof Number){
                stringBuilder.append(String.valueOf(value));
            }
            else if (value instanceof Date) {
                stringBuilder.append(
                  String.format("'%s'", dateFormatter.format((Date) value)));
            }
            else {
                stringBuilder.append(String.format("'%s'",value.toString()));
            }
          insertStmtBuilder.append(String.
            format("%s%s", (isFirstColumn ? "" : ","), stringBuilder.toString()));
            isFirstColumn = false;
        }
        insertStmtBuilder.append(");");

        return insertStmtBuilder.toString();
    }

    @Override
    public String getFileExtension() {
        return ".sql";
    }

    @Override
    public boolean hasSeparateRelationshipHandling() {
        return false;
    }

    @Override
    public String getNodeOpeningFilePath() {
        return SQLFormatter.class.getResource("/createTables.sql").getPath();
    }

    @Override
    public String getNodeFinishFilePath() {
        return SQLFormatter.class.getResource("/createKeys.sql").getPath();
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
        return true;
    }

    @Override
    public boolean requiresNodeFinish() {
        return true;
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
