package com.bugtsa.iceandfire.utils;

public interface ConstantManager {
    String TAG_PREFIX = "IceAndFire";

    String FIRST_LAUNCH_KEY = "FIRST_LAUNCH_KEY";
    String PARCELABLE_KEY = "PARCELABLE_KEY";

    int RESPONSE_OK = 200;
    int USER_NOT_AUTHORIZED = 401;
    int LOGIN_OR_PASSWORD_INCORRECT = 404;
    int RESPONSE_NOT_OK = 666;
    int SERVER_ERROR = 677;
    int USER_LIST_LOADED_AND_SAVED = 688;
    int NETWORK_NOT_AVAILABLE = 699;
    int END_SHOW_USERS = 711;
    int HIDE_SPLASH = 733;

    int USER_LIST_NOT_SAVED = 733;
    int LANNISTER_KEY = 229;
    int TARGARIEN_KEY = 378;
    int STARK_KEY = 362;

    int STARK_MENU_ID = 0;
    int TARGARIEN_MENU_ID = 1;
    int LANNISTER_MENU_ID = 2;

    int QUANTITY_PAGE = 43;
    int PER_PAGE = 50;
    String DONE_SAVE_CHARACTER_KEY = "DONE_SAVE_CHARACTER_KEY";

    String PRE_ID_SYMBOL = "/";
    String NEW_STRING_SYMBOL_STR = "\n";
    char NEW_STRING_SYMBOL_CHAR = '\n';

}
