package com.mapr.db.samples.rest.helper;

//import com.mapr.db.MapRDB;
//import com.mapr.db.Table;

import com.mapr.db.MapRDB;
import com.mapr.db.Table;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;

public class MaprDBHelper extends ResourceConfig {

    public MaprDBHelper() {
    }


    /**
     * Return the table, and create if does not exist
     * @param tablePath
     * @return the Table for the path
     * @throws IOException
     */
    public static Table getTable(String tablePath) throws IOException {
        Table table = null;
        if (MapRDB.tableExists(tablePath)) {
            table = MapRDB.getTable(tablePath);
        } else {
            table = MapRDB.createTable(tablePath);
        }
        return table;
    }


    public static void deleteTable(String tablePath) throws IOException {
        MapRDB.deleteTable( tablePath );
    }



}
