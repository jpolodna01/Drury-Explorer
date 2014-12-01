package edu.drury.mcs.icarus.druryexplorer;

/**
 * Created by Josef Polodna on 11/13/2014.
 */
public class DruryHall {
    public String _name, _history, _year;
    public int id;

    public DruryHall(){}

    public DruryHall(int id, String name, String year, String history) {
        _name = name;
        _year = year;
        _history = history;
    }

    public int getID(){
        return id;
    }
    public String getName(){return _name;}
    public String getYear(){return _year;}
    public String getHistory(){return _history;}

    @Override
    public String toString() {
        return "Location [id=" + id
                + ",name=" + _name
                + ",lat=" + _history
                + ",lng=" + _year +"]";
    }

}
