package materialtest.theartistandtheengineer.co.materialtest.app;

import android.util.Log;
import  materialtest.theartistandtheengineer.co.materialtest.LoginActivity;

public class AppConfig {
	// Server user login url
	public static String URL_LOGIN = "http://theartistandtheengineer.co/ubooks/";

	public static final String API_KEY_GOOGLE_BOOKS = "AIzaSyBlZGnFuy7ro_RieQn8zRNr2QSM5CjHatI";

	// Server user register url
	public static String URL_REGISTER = "http://theartistandtheengineer.co/ubooks/";

    // Server sell url
    public static String URL_SELL = "http://theartistandtheengineer.co/ubooks/";



    //(this)
	public String unique_id;
    public static String name;
    public static String email;
    public static String created_at;
    public static String updated_at;
    public static int reputation_avg;
    public static int reputation_total;

    /*
     $response["uid"] = $user["unique_id"];
  $response["user"]["name"] = $user["name"];
  $response["user"]["email"] = $user["email"];
  $response["user"]["created_at"] = $user["created_at"];
  $response["user"]["updated_at"] = $user["updated_at"];
  $response["user"]["reputation_avg"] = $user["reputation_avg"];
  $response["user"]["reputation_total"] = $user["reputation_avg"];

*/
/*
    public void setUid(String unique_id){
        Log.d("Inside Appconfig", unique_id);
        this.unique_id = unique_id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setEmail(String email){
        this.name = email;
    }

    public void setCreatedAt(String created_at){
        this.created_at = created_at;
    }

    public void setUpdatedAt(String updated_at){
        this.updated_at = updated_at;
    }

    public void setReputationAvg(int reputation_avg){
        this.reputation_avg = reputation_avg;
    }

    public void setReputationtoTotal(int reputation_total){
        this.reputation_total = reputation_total;
    }*/

    //take all vars at once

    public void setUserInfo(String unique_id, String name, String email, String created_at, String updated_at, int reputation_avg, int reputation_total){
        Log.d("Inside Appconfig", unique_id);
        this.unique_id = unique_id;
        this.name = name;
        this.email = email;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.reputation_avg = reputation_avg;
        this.reputation_total = reputation_total;
    }


}
