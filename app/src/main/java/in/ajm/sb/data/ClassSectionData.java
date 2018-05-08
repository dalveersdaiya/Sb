package in.ajm.sb.data;

import java.util.ArrayList;

public class ClassSectionData {

    public String className;
    public String classImage = "";
    public ArrayList<String> sections = new ArrayList<String>();

    public ClassSectionData(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return className;
    }


}
