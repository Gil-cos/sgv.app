package br.com.gilberto.sgv.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public class NotificationDataDto {

    private String to;
    private NotificationDto notification;

}
