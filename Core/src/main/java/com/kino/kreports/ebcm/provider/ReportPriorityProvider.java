package com.kino.kreports.ebcm.provider;

import com.kino.kreports.utils.report.ReportPriority;
import me.fixeddev.ebcm.NamespaceAccesor;
import me.fixeddev.ebcm.parameter.provider.SingleArgumentProvider;
import me.fixeddev.ebcm.part.CommandPart;


public class ReportPriorityProvider implements SingleArgumentProvider<ReportPriority> {

    @Override
    public Result<ReportPriority> transform(String s, NamespaceAccesor namespaceAccesor, CommandPart commandPart) {
        try {
            return Result.createResult(ReportPriority.valueOf(s.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            return Result.createResultOfMessage("Invalid priority", s);
        }
    }
}
