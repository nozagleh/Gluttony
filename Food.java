package nozagleh.org.gluttony;

/**
 * Created by arnarfreyr on 11.3.15.
 */
public class Food {

    //Initialize variables

    //ID for food
    int _id;
    //String for name
    String _name;
    //String for time
    String _time;
    //String for comment
    String _comment;
    //int for type
    int _type;

    //Constructor
    public Food(){

    }

    //Constructor with variables
    public Food(int id, String name, String time, String comment, int type){
        this._id = id;
        this._name = name;
        this._time = time;
        this._comment = comment;
        this._type = type;
    }

    //Constructor without id
    public Food(String name, String time, String comment,int type){
        this._name = name;
        this._time = time;
        this._comment = comment;
        this._type = type;
    }

    //constructor without comment
    public Food(String name, String time,int type){
        this._name = name;
        this._time = time;
        this._type = type;
    }

    //Getter and setter for id
    public int get_id() {
        return _id;
    }
    public void set_id(int _id) {
        this._id = _id;
    }

    //Getter and setter for name
    public String get_name() {
        return _name;
    }
    public void set_name(String _name) {
        this._name = _name;
    }

    //Getter and setter for time
    public String get_time() {
        return _time;
    }
    public void set_time(String _time) {
        this._time = _time;
    }

    //Getter and setter for comment
    public String get_comment() {
        return _comment;
    }
    public void set_comment(String _comment) {
        this._comment = _comment;
    }

    //Getter and setter for type
    public int get_type() {
        return _type;
    }
    public void set_type(int _type) {
        this._type = _type;
    }
}
