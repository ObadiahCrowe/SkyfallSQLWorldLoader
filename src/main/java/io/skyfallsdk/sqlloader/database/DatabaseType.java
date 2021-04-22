package io.skyfallsdk.sqlloader.database;

public enum DatabaseType {

    SQLITE("sqlite"),
    MARIADB("mysql");

    private final String name;

    DatabaseType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
