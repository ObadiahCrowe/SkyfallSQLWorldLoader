package io.skyfallsdk.sqlloader.config;

import io.skyfallsdk.Server;
import io.skyfallsdk.config.type.YamlConfig;
import io.skyfallsdk.sqlloader.SQLLoaderExpansion;
import io.skyfallsdk.sqlloader.database.DatabaseType;

import java.nio.file.Path;

public class ExpansionConfig extends YamlConfig<ExpansionConfig> {

    private static final ExpansionConfig DEFAULT_CONFIG = new ExpansionConfig(
      DatabaseType.SQLITE,
      new DatabaseSection("localhost", 3306, "worlds", "username", "password")
    );

    private DatabaseType type;
    private DatabaseSection database;

    public ExpansionConfig() {
        super(ExpansionConfig.class);
    }

    public ExpansionConfig(DatabaseType type, DatabaseSection database) {
        super(ExpansionConfig.class);

        this.type = type;
        this.database = database;
    }

    public DatabaseType getDatabaseType() {
        return this.type;
    }

    public DatabaseSection getDatabaseSection() {
        return this.database;
    }

    @Override
    public Path getPath() {
        return Server.get().getExpansion(SQLLoaderExpansion.class).getPath().resolve("config.yml");
    }

    @Override
    public ExpansionConfig getDefaultConfig() {
        return DEFAULT_CONFIG;
    }

    public static class DatabaseSection {

        private String address;
        private int port;

        private String database;
        private String username;
        private String password;

        public DatabaseSection() {}

        public DatabaseSection(String address, int port, String database, String username, String password) {
            this.address = address;
            this.port = port;
            this.database = database;
            this.username = username;
            this.password = password;
        }

        public boolean isUsingAuth() {
            return !this.username.isEmpty() && !this.password.isEmpty();
        }

        public String getAddress() {
            return this.address;
        }

        public int getPort() {
            return this.port;
        }

        public String getDatabase() {
            return this.database;
        }

        public String getUsername() {
            return this.username;
        }

        public String getPassword() {
            return this.password;
        }
    }
}
