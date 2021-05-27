package br.com.gilberto.sgv.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public class NotificationDto {

    private String title;
    private String body;

}
