package JETS.ui.controllers;

public class CountryCodeData {
    private int code;
    private String countryName;
    public CountryCodeData(int code, String countryName) {
        this.code = code;
        this.countryName = countryName;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getCode() {
        return code;
    }

    public String getCountryName() {
        return countryName;
    }
    public String toString(){
        return "(+"+code+")  "+countryName;
    }
}
