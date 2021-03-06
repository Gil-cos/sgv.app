package br.com.gilberto.sgv.util;

import android.content.SharedPreferences;

import br.com.gilberto.sgv.R;
import br.com.gilberto.sgv.domain.user.Role;
import br.com.gilberto.sgv.dto.TokenDto;

public class SharedPreferencesUtils {

    private static String TOKEN = "token";
    private static String USER_ID = "userId";
    private static String ROLE = "role";

    public void saveToken(final TokenDto tokenDto, final SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN, tokenDto.getType() + " " + tokenDto.getToken());
        editor.commit();
    }

    public String retrieveToken(final SharedPreferences sharedPreferences) {
        return sharedPreferences.getString(TOKEN, "");
    }

    public void saveUserId(final Long id, final SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(USER_ID, id);
        editor.commit();
    }

    public void saveUserRole(final Role role, final SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ROLE, role.name());
        editor.commit();
    }

    public Long retrieveUserId(final SharedPreferences sharedPreferences) {
        return sharedPreferences.getLong(USER_ID, 0);
    }

    public String retrieveUserRole(final SharedPreferences sharedPreferences) {
        return sharedPreferences.getString(ROLE, Role.ADMIN.name());
    }
}
