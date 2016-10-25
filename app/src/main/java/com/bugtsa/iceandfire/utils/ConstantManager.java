package com.bugtsa.iceandfire.utils;

import com.bugtsa.iceandfire.R;

public interface ConstantManager {
    String TAG_PREFIX = "IceAndFire";

    String FIRST_LAUNCH_KEY = "FIRST_LAUNCH_KEY";
    String PARCELABLE_KEY = "PARCELABLE_KEY";

    String RESPONSE_OK = "Response ok";
    String RESPONSE_NOT_OK = "Response not ok";
    String FAILED_SERVER = "Server is failed";
    String EXCEPTION_PREPARE_CHARACTERSLIST = "Exception in prepareCharactersList ";
    int USER_NOT_AUTHORIZED = 401;
    int LOGIN_OR_PASSWORD_INCORRECT = 404;
    int SERVER_ERROR = 677;
    int USER_LIST_LOADED_AND_SAVED = 688;
    int NETWORK_NOT_AVAILABLE = 699;
    int END_SHOW_USERS = 711;
    int HIDE_SPLASH = 733;

    int USER_LIST_NOT_SAVED = 733;
    int NETWORK_IS_NOT_AVAILABLE = 1011;

    int STARK_KEY = 362;
    int LANNISTER_KEY = 229;
    int TARGARIEN_KEY = 378;

    int STARK_PAGE_ID = 0;
    int LANNISTER_PAGE_ID = 1;
    int TARGARIEN_PAGE_ID = 2;
    int QUANTITY_VIEW_PAGE = 3;


    int[] HOUSE_NAME_RES = new int[]{R.string.house_starks, R.string.house_lannisters, R.string.house_targaryens};
    int QUANTITY_PAGE = 43;
    int PER_PAGE = 50;

    String PRE_ID_SYMBOL = "/";
    String NEW_STRING_SYMBOL_STR = "\n";
    char NEW_STRING_SYMBOL_CHAR = '\n';

    String KEY_HOUSE_INDEX = "KEY_HOUSE_INDEX";
    String WORD_IN_DIED_CHARACTER = "In";
}
