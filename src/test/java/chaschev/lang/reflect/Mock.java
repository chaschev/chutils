package chaschev.lang.reflect;

import java.util.ArrayList;
import java.util.List;

/**
* @author Andrey Chaschev chaschev@gmail.com
*/
public class Mock {
    public int c;

    public Mock(ArrayList l1) {
        c = 1;
    }

    public Mock(List l1, ArrayList l2) {
        c = 2;
    }

    public Mock(List l1) {
        c = 3;
    }

    public Mock(int c) {
        this.c = 4;
    }

    public Mock(String s, int c) {
        this.c = 5;
    }

    public Mock(Double d) {
        this.c = 6;
    }
}
