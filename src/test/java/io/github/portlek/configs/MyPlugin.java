package io.github.portlek.configs;

import io.github.portlek.configs.file.Config;
import io.github.portlek.configs.file.EnMessages;
import io.github.portlek.configs.file.Messages;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public final class MyPlugin extends JavaPlugin {

    private final AnnotationProcessor annotationProcessor = new AnnotationProcessor(this);

    public MyPlugin() {
        super();
    }

    protected MyPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, new File("build"), file);
    }

    @Override
    public void onEnable() {
        final Config config = annotationProcessor.load(
            new Config()
        );

        annotationProcessor.define("%prefix%", config.plugin_prefix);

        annotationProcessor.load(
            new Messages()
        );

        System.out.println("Plugin Prefix: " + config.plugin_prefix);
        System.out.println("Plugin Language: " + config.plugin_language);

        System.out.println("Player Not Found: " + EnMessages.error.player_not_found);
        System.out.println("Item Test: " + EnMessages.general.item_test);
        System.out.println("Reload Complete: " + EnMessages.general.reload_complete);
        System.out.println("Title Test: " + EnMessages.general.title_test);

        System.out.println("Display Name: " + EnMessages.itemTest.display_name);
        System.out.println("Data: " + EnMessages.itemTest.data);
        System.out.println("Enchantments: " + EnMessages.itemTest.enchantments);
        System.out.println("Lore: " + EnMessages.itemTest.lore);
        System.out.println("Material: " + EnMessages.itemTest.material);
    }

}