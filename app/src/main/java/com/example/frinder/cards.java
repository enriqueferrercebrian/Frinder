package com.example.frinder;




public class cards {
    private String userId;
    private String name,age, city;
    private String profileImageUrl;


    public cards (String userId, String name, String age, String city){
        this.userId = userId;
        this.name = name;
        this.age = age;
        this.city = city;


    }

    public String getUserId(){
        return userId;
    }
    public void setUserID(String userID){
        this.userId = userId;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "cards{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", city='" + city + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                '}';
    }
}