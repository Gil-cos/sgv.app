package br.com.gilberto.sgv.client;

import br.com.gilberto.sgv.dto.NotificationDataDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationClient {

    @Headers({
            "Authorization:key=AAAAwamIwFE:APA91bHic23vSNsmopAYz7ARoIAkFsfDIKsGtkugTd7XH3y81L-v1337OKs5XagndXWXkhFyM0EvHsNpuVLcd1cFL-O8rXk7u0iNzrKuhs94khN3pfkKZ9PUaQ11HvRkuHezPdaLIDtc",
            "Content-Type:application/json"
    })
    @POST("send")
    Call<NotificationDataDto> sendNotification(@Body NotificationDataDto notificationDataDto);

}
