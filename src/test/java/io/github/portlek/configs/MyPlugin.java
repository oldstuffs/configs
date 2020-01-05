package io.github.portlek.configs;

import io.github.portlek.configs.file.Config;
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
        final Config config = annotationProcessor.load(new Config());

        System.out.println(config.plugin_prefix);
        System.out.println(config.plugin_language);
        System.out.println(config.test_integer);
        System.out.println(config.test_list);
        System.out.println(config.test_title.title().build());
        System.out.println(config.test_title.subTitle().build());
        System.out.println("******");
        System.out.println(config.test_1.test_title.title().build());
        System.out.println(config.test_1.test_title.subTitle().build());
        System.out.println("******");
        System.out.println(config.test_1.test_2.test_title.title().build());
        System.out.println(config.test_1.test_2.test_title.subTitle().build());
        System.out.println("******");
        System.out.println(config.test_1.test_2.test_3.test_title.title().build());
        System.out.println(config.test_1.test_2.test_3.test_title.subTitle().build());
    }

}