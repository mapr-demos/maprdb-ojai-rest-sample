package com.mapr.db.samples.rest.helper;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapr.db.rowcol.DBList;
import org.ojai.Document;
import org.ojai.Value;
import com.mapr.db.DBDocument;
import com.mapr.db.MapRDB;
import com.mapr.db.rowcol.KeyValueBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is just here until the OJAI API support POJO/String/Map support natively
 *
 * TODO: remove when API is complete
 */
public class JSONHelper {

  /**
   * Return the Record as Map
   * @param record
   * @return a Map
   */
  public static Map toMap(Document record) {
    Map<String, Object> map = new HashMap();
    for (Map.Entry<String, Value> element : record) {
      if (!element.getKey().equals("_id")) {

        if ( element.getValue().getObject() instanceof DBDocument) {
          map.put(element.getKey(), toMap( (DBDocument)element.getValue().getObject() ) );
        } else if ( element.getValue().getObject() instanceof DBList) {
          map.put(element.getKey(), Arrays.asList(((DBList) element.getValue().getObject()).toArray())    );
        } else {
          map.put(element.getKey(), element.getValue().getObject());
        }
      } else {
        map.put(element.getKey(), ((DBDocument) record).getIdString());
      }
    }

    return map;
  }

}
