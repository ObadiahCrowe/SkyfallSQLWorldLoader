package io.skyfallsdk.sqlloader.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.skyfallsdk.Server;
import io.skyfallsdk.sqlloader.SQLLoaderExpansion;
import io.skyfallsdk.sqlloader.config.ExpansionConfig;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;

public class AbstractDatabaseHandler {

    private static HikariConfig HIKARI_CONFIG;
    private static HikariDataSource DATA_SOURCE;

    static {
        try {
            ExpansionConfig eConfig = Server.get().getExpansion(SQLLoaderExpansion.class).getConfig();
            ExpansionConfig.DatabaseSection dbConfig = eConfig.getDatabaseSection();
            HikariConfig config = new HikariConfig();

            config.setJdbcUrl("jdbc:" + eConfig.getDatabaseType().getName() + "://" + dbConfig.getAddress() + ":" + dbConfig.getPort() + "/" + dbConfig.getDatabase());
            config.setDriverClassName(eConfig.getDatabaseType() == DatabaseType.MARIADB ? "org.mariadb.jdbc.Driver" : "");

            if (dbConfig.isUsingAuth()) {
                config.setUsername(dbConfig.getUsername());
                config.setPassword(dbConfig.getPassword());
            }

            config.setConnectionTimeout(20 * 1000);
            config.setLeakDetectionThreshold(5 * 1000);
            config.setMaximumPoolSize(100);
            config.setMaxLifetime(300_000);

            config.addDataSourceProperty("cachePrepStmts", true);
            config.addDataSourceProperty("prepStmtCacheSize", 250);
            config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
            config.addDataSourceProperty("useServerPrepStmts","true");
            config.addDataSourceProperty("useLocalSessionState","true");
            config.addDataSourceProperty("zeroDateTimeBehavior", "convertToNull");
            config.addDataSourceProperty("rewriteBatchedStatements","true");
            config.addDataSourceProperty("cacheResultSetMetadata","true");
            config.addDataSourceProperty("cacheServerConfiguration","true");
            config.addDataSourceProperty("maintainTimeStats","true");
            config.addDataSourceProperty("elideSetAutoCommits","true");

            HIKARI_CONFIG = config;
            DATA_SOURCE = new HikariDataSource(HIKARI_CONFIG);
        } catch (Throwable e) {
            Server.get().getExpansion(SQLLoaderExpansion.class).getLogger().error(e);
        }
    }

    /**
     * @return The data source that is in use with this DatabaseHandler.
     */
    protected HikariDataSource getDataSource() {
        return DATA_SOURCE;
    }

    /**
     * @return The connection stream to the database as specified by the {@code #getDataSource()} method.
     * @throws SQLException If the database cannot be connected to.
     */
    public Connection getConnection() throws SQLException {
        return this.getDataSource().getConnection();
    }
}
