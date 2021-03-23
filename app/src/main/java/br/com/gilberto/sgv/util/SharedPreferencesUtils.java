package br.com.gilberto.sgv.util;

import android.content.SharedPreferences;

import br.com.gilberto.sgv.R;
import br.com.gilberto.sgv.dto.TokenDto;

public class SharedPreferencesUtils {

    private static String TOKEN = "token";

    public void saveToken(final TokenDto tokenDto, final SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN, tokenDto.getType() + " " + tokenDto.getToken());
        editor.commit();
    }

    public String retrieveToken(final SharedPreferences sharedPreferences) {
        return sharedPreferences.getString(TOKEN, "");
    }
}
