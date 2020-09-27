package com.kino.kreports.ebcm.provider;

import me.fixeddev.ebcm.NamespaceAccesor;
import me.fixeddev.ebcm.parameter.provider.SingleArgumentProvider;
import me.fixeddev.ebcm.part.CommandPart;

import java.util.UUID;

public class UUIDProvider implements SingleArgumentProvider<UUID> {

    @Override
    public Result<UUID> transform(String s, NamespaceAccesor namespaceAccesor, CommandPart commandPart) {
        try {
            return Result.createResult(UUID.fromString(s));
        } catch (IllegalArgumentException ex) {
            return Result.createResultOfMessage("Invalid UUID", s);
        }
    }
}
