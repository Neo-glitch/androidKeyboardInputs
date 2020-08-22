package com.neo.androidkeyboardinputs;

import com.neo.androidkeyboardinputs.models.Message;
import com.neo.androidkeyboardinputs.models.User;


/**
 * Created by User on 1/24/2018.
 */

public interface IMainActivity {

    // interface methods for comm from fragment 1 - activity - fragment2
    void inflateViewProfileFragment(User user);

    void onMessageSelected(Message message);            // sends msg obj btw fragments

    void onBackPressed();


}
