/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.model.app.prefs;


import com.bitbill.www.common.base.model.prefs.Prefs;

/**
 * Created by isanwenyu@163.com on 2017/7/25.
 */
public interface AppPreferences extends Prefs {
    String IS_RECEIVE_REMIND_DIALOG_SHOWN = "is_remind_dialog_shown";
    String IS_SHORT_CUT_SHOWN = "is_shortcut_shown";
    String IS_SOUND_ENABLED = "is_sound_enabled";
    String SELECTED_CURRENCY = "selected_currency";

    void setReceiveRemindDialogShown();

    boolean isReceiveRemindDialogShown();

    boolean isShortcutShown();

    void setShortcutShown(boolean shown);

    boolean isSoundEnable();

    void setSoundEnabled(boolean soundEnabled);

    SelectedCurrency getSelectedCurrency();

    void setSelectedCurrency(SelectedCurrency selectedCurrency);

    enum SelectedCurrency {
        CNY,
        USD
    }

}
