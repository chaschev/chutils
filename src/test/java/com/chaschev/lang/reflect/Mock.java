package com.chaschev.lang.reflect;

import java.util.ArrayList;
import java.util.List;

/**
* @author Andrey Chaschev chaschev@gmail.com
*/
class Mock {
    int c;

    public Mock(ArrayList l1) {
        c = 1;
    }

    public Mock(List l1, ArrayList l2) {
        c = 2;
    }

    public Mock(List l1) {
        c = 3;
    }
}
