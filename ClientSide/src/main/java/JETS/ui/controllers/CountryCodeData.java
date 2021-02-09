package JETS.ui.controllers;

public class CountryCodeData implements Comparable<CountryCodeData>{
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

    @Override
    public int compareTo(CountryCodeData o) {
        return countryName.compareTo(o.countryName);
    }

    public String toString(){
        return countryName+"(+"+code+")";
    }
}
