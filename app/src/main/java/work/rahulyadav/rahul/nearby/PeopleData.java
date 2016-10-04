package work.rahulyadav.rahul.nearby;

/**
 * Created by RAHUL on 5/27/2016.
 */
public class PeopleData {
    public String userName;
    public String userMobile;
    public String userLat;
    public String userLong;
    public String userDetails;
    public String userCategory;
    public PeopleData(String name,String mobile,String category,String details,String lati,String longi){
        this.userCategory=category;
        this.userDetails=details;
        this.userName=name;
        this.userMobile=mobile;
        this.userLat=lati;
        this.userLong=longi;
    }
}
