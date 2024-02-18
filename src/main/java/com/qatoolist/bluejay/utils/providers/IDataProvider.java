package com.qatoolist.bluejay.utils.providers;

import java.lang.reflect.Method;
import java.util.List;

public interface IDataProvider {
    List<Object[]> fetchData(Method testMethod);
}
