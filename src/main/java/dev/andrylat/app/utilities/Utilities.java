package dev.andrylat.app.utilities;

public class Utilities {
    
    public static String generateDatabaseIdErrorMessage(String idName) {
        String prefix = "The ";
        String suffix = " provided doesn't match any columns in the database. Please verify the value and try again";
        return prefix + idName + suffix;
    }
    
    public static String generateDatabaseGetAllErrorMessage(String entityName) {
        String prefix = "Something went wrong when trying to retrieve all of the ";
        String suffix = ". Please check the database";
        return prefix + entityName + suffix;
    }
    
    public static String generateDatabaseSaveErrorMessage(String entityName) {
        String prefix = "Something went wrong when trying to save the ";
        String suffix = " object. Please check the database.";
        return prefix + entityName + suffix;
    }
    
    public static String generateDatabaseUpdateErrorMessage(String entityName, String idName) {
        String prefix = "Something went wrong when trying to update the ";
        String suffix = " object. Please check the database where ";
        String postSuffix = "=";
        return prefix + entityName + suffix + idName + postSuffix;
    }

    public static String generateDatabaseInvalidObjectErroeMessage(String entityName, String idName) {
        String suffix = " object has an invalid state. Check field values to make sure they are valid where ";
        String postSuffix = "=";
        return entityName + suffix + idName + postSuffix;
    }
}
