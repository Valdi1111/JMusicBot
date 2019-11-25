package org.valdi.jmusicbot.settings.locale.lang;

/**
 * {@link Lang} implementation for Manage command related subcommand language.
 *
 * @author Rsl1122
 */
public enum ManageLang implements Lang {

    HOTSWAP_REMINDER("Manage - Remind HotSwap", "§eRemember to swap to the new database (/plan m hotswap ${0}) & reload the plugin."),
    PROGRESS_START("Manage - Start", "> §2Processing data.."),
    PROGRESS_SUCCESS("Manage - Success", "> §aSuccess!"),
    PROGRESS_FAIL("Manage - Fail", "> §cSomething went wrong: ${0}"),

    CONFIRMATION("Manage - Fail, Confirmation", "> §cAdd '-a' argument to confirm execution: ${0}"),
    IMPORTERS("Manage - List Importers", "Importers: "),

    CONFIRM_OVERWRITE("Manage - Confirm Overwrite", "Data in ${0} will be overwritten!"),
    CONFIRM_REMOVAL("Manage - Confirm Removal", "Data in ${0} will be removed!"),

    FAIL_SAME_DB("Manage - Fail Same Database", "> §cCan not operate on to and from the same database!"),
    FAIL_INCORRECT_DB("Manage - Fail Incorrect Database", "> §c'${0}' is not a supported database."),
    FAIL_FILE_NOT_FOUND("Manage - Fail File not found", "> §cNo File found at ${0}"),
    FAIL_IMPORTER_NOT_FOUND("Manage - Fail No Importer", "§eImporter '${0}' doesn't exist"),
    FAIL_EXPORTER_NOT_FOUND("Manage - Fail No Exporter", "§eExporter '${0}' doesn't exist"),
    NO_SERVER("Manage - Fail No Server", "No server found with given parameters."),
    UNINSTALLING_SAME_SERVER("Manage - Fail Same server", "Can not mark this server as uninstalled (You are on it)");

    private final String identifier;
    private final String defaultValue;

    ManageLang(String identifier, String defaultValue) {
        this.identifier = identifier;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getDefault() {
        return defaultValue;
    }
}