package site.haruhana.www.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CalendarEntryDto {
    private LocalDate date;
    private boolean status;

    public CalendarEntryDto(LocalDate date, boolean status) {
        this.date = date;
        this.status = status;
    }
}
