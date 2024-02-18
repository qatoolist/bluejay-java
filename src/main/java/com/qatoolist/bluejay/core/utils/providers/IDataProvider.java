package com.qatoolist.bluejay.core.utils.providers;

import java.lang.reflect.Method;
import java.util.List;

public interface IDataProvider {
    List<Object[]> fetchData(Method testMethod);
}
