package io.skyfallsdk.sqlloader;

import io.skyfallsdk.Server;
import io.skyfallsdk.SkyfallServer;
import io.skyfallsdk.config.LoadableConfig;
import io.skyfallsdk.expansion.Expansion;
import io.skyfallsdk.expansion.ExpansionInfo;
import io.skyfallsdk.sqlloader.config.ExpansionConfig;
import io.skyfallsdk.sqlloader.loader.SQLWorldLoader;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@ExpansionInfo(name = "SQLWorldLoader", version = "1.0", authors = { "Obadiah Crowe" })
public class SQLLoaderExpansion implements Expansion {

    private ExpansionConfig config;

    @Override
    public void onStartup() {
        this.config = LoadableConfig.getByClass(ExpansionConfig.class).load();

        this.getLogger().info("Injecting SQLWorldLoader with database implementation: " + this.config.getDatabaseType() + "...");
        try {
            SkyfallServer server = (SkyfallServer) this.getServer();
            Field field = server.getClass().getDeclaredField("worldLoader");
            field.setAccessible(true);

            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.set(server, new SQLWorldLoader(server, server.getPath()));
        } catch (Exception e) {
            this.getLogger().error(e);
            Server.get().getExpansionRegistry().unloadExpansion(this);
            return;
        }

        this.getLogger().info("Successfully injected SQLWorldLoader!");
    }

    @Override
    public void onShutdown() {
        this.config.save();
    }

    public ExpansionConfig getConfig() {
        return this.config;
    }
}
