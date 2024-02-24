package tables;

public class Section {

    private String section;

    public Section(String section){
        this.section = section;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    @Override
    public String toString(){
        return "Section " + section;
    }
}
