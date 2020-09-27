package com.kino.kreports.ebcm.i18n;

import com.kino.kore.utils.files.YMLFile;
import me.fixeddev.ebcm.i18n.DefaultI18n;
import me.fixeddev.ebcm.i18n.Message;
import team.unnamed.inject.InjectAll;
import team.unnamed.inject.name.Named;

public class CustomI18n extends DefaultI18n {


    public CustomI18n (YMLFile messages) {
        setMessage(Message.COMMAND_NO_PERMISSIONS.getId(), messages.getString("noPermission"));
        setMessage(Message.COMMAND_USAGE.getId(), messages.getString("wrongUsage"));
        setMessage(Message.MISSING_ARGUMENT.getId(), messages.getString("missingArgument"));
        setMessage(Message.MISSING_SUBCOMMAND.getId(), messages.getString("missingSubCommand"));
        setMessage(Message.INVALID_SUBCOMMAND.getId(), messages.getString("invalidSubCommand"));
    }
}
