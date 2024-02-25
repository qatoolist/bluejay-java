package com.qatoolist.bluejay.core.pages;

public interface INavigable {
    void navigate(); // Navigate to the URL of implementing class
    void goBack();  // Go back in navigation history
    void goForward(); // Go forward in navigation history
}
