package com.example.samppleimage;

public class Datas {
    private  String Name;
    private String Imageid;
    private  String ImageUrl;

    public Datas() {
    }

    public Datas(String name , String imageurl){
        Name = name;
        ImageUrl = imageurl;
    }

    public String getImageUrl() {
        return ImageUrl;
    }


    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }



    public String getImageid() {
        return Imageid;
    }

    public void setImageid(String imageid) {
        Imageid = imageid;
    }
}
