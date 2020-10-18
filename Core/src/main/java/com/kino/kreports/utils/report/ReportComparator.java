package com.kino.kreports.utils.report;

import com.kino.kreports.models.reports.Report;

import java.util.Comparator;

public class ReportComparator implements Comparator<Report> {

    @Override
    public int compare (Report report, Report report2) {

        return compareAccepted(report, report2) !=0 ?
                compareAccepted(report, report2) :
                compareState (report, report2) != 0 ?
                        compareState (report, report2) :
                        comparePriority (report, report2) != 0 ?
                                comparePriority(report, report2) :
                                compareDate(report, report2);
    }

    private int compareAccepted (Report report, Report report2) {
        return report.isAccepted() && report2.isAccepted() ? 0 : report.isAccepted() ? 1 : -1;
    }

    private int compareDate (Report report, Report report2) {
        return report.getDate().equals(report2.getDate()) ?
                0 : report.getDate().compareTo(report2.getDate());
    }

    private int comparePriority (Report report, Report report2) {
        return report.getPriority().equals(report2.getPriority()) ?
                0 : report.getPriority().ordinal() == 0 ?
                1 : report.getPriority().ordinal() == 1 && report2.getPriority().ordinal() == 0 ?
                -1 :  report.getPriority().ordinal() == 1 && report2.getPriority().ordinal() == 2 ? 1 : -1;
    }

    private int compareState (Report report, Report report2) {
        return report.getState().equals(report2.getState()) ?
                0 : report.getState().ordinal() == 0 ?
                1 : report.getState().ordinal() == 1 && report2.getState().ordinal() == 0 ?
                -1 :  report.getState().ordinal() == 1 && report2.getState().ordinal() == 2 ? 1 : -1;
    }


    /*
    Collections.sort(reports, Comparator.comparing(Report::isAccepted)
  .thenComparing((reportOne, reportTwo) -> {
    ReportState stateOne = reportOne.getState();
    ReportState stateTwo = reportTwo.getState();

    if (stateOne != stateTwo) {
      switch(stateOne) {
        case WAITING: {
          if (stateTwo != ANOTHER) {
            return 1;
          }

          return -1;
        }
      }
    } //más mamadas
  })
  .thenComparing(Report::getPriority))
  .thenComparing((reportOne, reportTwo) -> {
    comparas las fechas con respecto a la actual con Date#after() si es que usas eso si no te chingas
  })
     */
}
