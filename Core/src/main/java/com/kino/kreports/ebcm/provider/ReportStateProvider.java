package com.kino.kreports.ebcm.provider;

import com.kino.kreports.utils.report.ReportState;
import me.fixeddev.ebcm.NamespaceAccesor;
import me.fixeddev.ebcm.parameter.provider.SingleArgumentProvider;
import me.fixeddev.ebcm.part.CommandPart;

public class ReportStateProvider implements SingleArgumentProvider<ReportState> {

    @Override
    public Result<ReportState> transform(String s, NamespaceAccesor namespaceAccesor, CommandPart commandPart) {
        try {
            return Result.createResult(ReportState.valueOf(s.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            return Result.createResultOfMessage("Invalid state", s);
        }
    }
}
