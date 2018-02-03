/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.model.app.prefs;


import com.bitbill.www.common.base.model.prefs.Prefs;

import java.util.Locale;

/**
 * Created by isanwenyu@163.com on 2017/7/25.
 */
public interface AppPreferences extends Prefs {
    String IS_RECEIVE_REMIND_DIALOG_SHOWN = "is_remind_dialog_shown";
    String IS_SHORT_CUT_SHOWN = "is_shortcut_shown";
    String IS_SOUND_ENABLED = "is_sound_enabled";
    String SELECTED_CURRENCY = "selected_currency";
    String CONTACTKEY = "contactkey";
    String UUID_MD5 = "uuid_md5";
    String SELECTED_LOCALE = "selected_locale";
    String IS_ALIAS_SETED = "is_alias_seted";
    String FORCE_VERSION = "force_version";
    String UPDATE_VERSION = "update_version";

    void setReceiveRemindDialogShown();

    boolean isReceiveRemindDialogShown();

    boolean isShortcutShown();

    void setShortcutShown(boolean shown);

    boolean isSoundEnable();

    void setSoundEnabled(boolean soundEnabled);

    SelectedCurrency getSelectedCurrency();

    void setSelectedCurrency(SelectedCurrency selectedCurrency);

    String getContactKey();

    void setContactkey(String contactKey);

    String getUUIDMD5();

    void setUUIDMD5(String uuidmd5);

    Locale getSelectedLocale();

    void setSelectedLocale(Locale locale);

    boolean isAliasSeted();

    void setAliasSeted(boolean isSeted);

    String getForceVersion();

    void setForceVersion(String aforceVersion);

    String getUpdateVersion();

    void setUpdateVersion(String aversion);

    enum SelectedCurrency {
        CNY,
        USD
    }
}
