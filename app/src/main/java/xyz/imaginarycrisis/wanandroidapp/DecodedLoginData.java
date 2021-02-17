package xyz.imaginarycrisis.wanandroidapp;

import org.json.JSONObject;

import java.io.Serializable;

public class DecodedLoginData implements Serializable {
    private String username;
    private String nickname;
    private int id;
    private int coinCount;
    private boolean admin;
    private String email;
    private String icon;
    private String publicName;

    public DecodedLoginData(){
        this.username = "未知";
        nickname = "未知";
        id = -1;
        coinCount = -1;
        admin = false;
        email = "unknown@unknown.com";
        icon = "";
        publicName = "未知";
    }

    public DecodedLoginData(String username,String nickname,int id,int coinCount,boolean admin,
                             String email,String icon,String publicName){
        this.username = username;
        this.nickname = nickname;
        this.id = id;
        this.coinCount = coinCount;
        this.admin = admin;
        this.email=email;
        this.icon = icon;
        this.publicName = publicName;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public int getId() {
        return id;
    }

    public int getCoinCount() {
        return coinCount;
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getEmail() {
        return email;
    }


    public String getPublicName() {
        return publicName;
    }

    public void setLoginData(String username,String nickname,int id,int coinCount,boolean admin,
                             String email,String icon,String publicName){
        this.username = username;
        this.nickname = nickname;
        this.id = id;
        this.coinCount = coinCount;
        this.admin = admin;
        this.email=email;
        this.icon = icon;
        this.publicName = publicName;
    }

    public static DecodedLoginData spawnDecodedJsonData(String data){
        try{
            JSONObject jsonObject = new JSONObject(data);
            if(jsonObject.getInt("errorCode")==0){
                JSONObject tData = jsonObject.getJSONObject("data");
                String tUsername = tData.getString("username");
                String tNickname = tData.getString("nickname");
                int tId = tData.getInt("id");
                int tCoinCount = tData.getInt("coinCount");
                boolean tAdmin = tData.getBoolean("admin");
                String tEmail = tData.getString("email");
                String tIcon = tData.getString("icon");
                String tPublicName = tData.getString("publicName");
                return new DecodedLoginData(tUsername,tNickname,
                        tId,tCoinCount,tAdmin,tEmail,tIcon,tPublicName);
            }
            return new DecodedLoginData();
        }catch(Exception e){
            e.printStackTrace();
        }
        return new DecodedLoginData();
    }
}
