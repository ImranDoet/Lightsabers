package com.forcemc.lightsabers.command;

import cloud.commandframework.annotations.*;
import cloud.commandframework.annotations.specifier.Greedy;
import cloud.commandframework.arguments.parser.ParserParameters;
import cloud.commandframework.arguments.parser.StandardParameters;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;
import cloud.commandframework.minecraft.extras.MinecraftHelp;
import cloud.commandframework.paper.PaperCommandManager;
import com.forcemc.lightsabers.Lightsabers;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Function;

import static net.kyori.adventure.text.Component.text;

public class CommandManager {
    private final Lightsabers lightsabers;
    private PaperCommandManager<CommandSender> paperCommandManager;
    private AnnotationParser<CommandSender> annotationParser;
    private MinecraftHelp<CommandSender> minecraftHelp;
    private BukkitAudiences bukkitAudiences;


    public CommandManager(Lightsabers lightsabers) {
        this.lightsabers = lightsabers;
    }

    public void load() {
        try {
            paperCommandManager = new PaperCommandManager<>(lightsabers, CommandExecutionCoordinator.simpleCoordinator(), Function.identity(), Function.identity());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        bukkitAudiences = BukkitAudiences.create(lightsabers);
        minecraftHelp = new MinecraftHelp<>("/lightsaber help", bukkitAudiences::sender, paperCommandManager);
        if (paperCommandManager.hasCapability(CloudBukkitCapabilities.BRIGADIER)) {
            paperCommandManager.registerBrigadier();
        }
        if (paperCommandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            paperCommandManager.registerAsynchronousCompletions();
        }
        final Function<ParserParameters, CommandMeta> commandMetaFunction = p -> CommandMeta.simple().with(CommandMeta.DESCRIPTION, p.get(StandardParameters.DESCRIPTION, "No description")).build();
        annotationParser = new AnnotationParser<>(paperCommandManager, CommandSender.class, commandMetaFunction);
        annotationParser.parse(this);
        new MinecraftExceptionHandler<CommandSender>().withInvalidSyntaxHandler().withInvalidSenderHandler().withNoPermissionHandler().withArgumentParsingHandler().withCommandExecutionHandler().withDecorator(component -> text().append(text("[xLightsabers]", NamedTextColor.GREEN)).append(component).build()).apply(paperCommandManager, this.bukkitAudiences::sender);

    }

    @CommandMethod("lightsabers|lightsaber help [query]")
    @CommandDescription("Help menu")
    public void commandHelp(final @NonNull CommandSender sender, final @Argument(value = "query", suggestions = "help_queries") @Greedy String query) {
        minecraftHelp.queryCommands(query == null ? "" : query, sender);
    }

    @CommandMethod("lightsabers crystals")
    @CommandDescription("Opens the crystal menu")
    @CommandPermission("lightsabers.crystals.manage")
    public void commandCrystals(final @NonNull Player player) {
        lightsabers.getInventoryManager().buildCrystals(player);
    }

}
