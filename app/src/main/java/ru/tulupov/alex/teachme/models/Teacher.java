package ru.tulupov.alex.teachme.models;


import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.tulupov.alex.teachme.R;

public class Teacher implements Parcelable {

    private int id;
    private String firstName;
    private String lastName;
    private String fatherName;
    private String birthDate;
    private City city;
    private String okrug;
    private String district;
    private List<PriceList> priceLists;

    private String description;

    private String email;
    private String phoneNumber;
    private String subways;
    private boolean leaveHome;
    private String photo;
    private boolean onlyDistanceLearning;
    private boolean distanceLearning;
    private boolean showEmail;
    private boolean showPhone;

//    private String strPriceList;
//    private String strCity;

    private boolean isFavorite = false;

    public Teacher() {
    }

    public Teacher(Parcel in) {
        String city;
        int cityId;
        boolean hasSubway;

        id = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        fatherName = in.readString();
        birthDate = in.readString();
        okrug = in.readString();
        district = in.readString();
        description = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        subways = in.readString();
        leaveHome = in.readByte() != 0;
        onlyDistanceLearning = in.readByte() != 0;
        distanceLearning = in.readByte() != 0;
        isFavorite = in.readByte() != 0;
        showEmail = in.readByte() != 0;
        showPhone = in.readByte() != 0;
        photo = in.readString();
        city = in.readString();
        cityId = in.readInt();
        hasSubway = in.readByte() != 0;
        this.city = new City(cityId, city, hasSubway);

        int size = in.readInt();

        priceLists = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int price = in.readInt();
            String exp = in.readString();
            String titleSbj = in.readString();

            PriceList priceList = new PriceList();
            priceList.setExperience(exp);
            priceList.setPrice(price);
            priceList.setSubject(new Subject(titleSbj));
            priceLists.add(priceList);
        }

//        strPriceList = in.readString();
    }

    public static final Creator<Teacher> CREATOR = new Creator<Teacher>() {
        @Override
        public Teacher createFromParcel(Parcel in) {
            return new Teacher(in);
        }

        @Override
        public Teacher[] newArray(int size) {
            return new Teacher[size];
        }
    };

    public String getAge(Resources resources) {
        String[] dateArr = birthDate.split("\\.");
        int year = Integer.parseInt(dateArr[2]);
        int month = Integer.parseInt(dateArr[1]);
        int day = Integer.parseInt(dateArr[0]);

        Calendar c = Calendar.getInstance();
        int currYear = c.get(Calendar.YEAR);
        int currMonth = c.get(Calendar.MONTH);
        int currDay = c.get(Calendar.DAY_OF_MONTH);

        int age = currYear - year;

        if ((currMonth - month > 0) || (currMonth - month == 0 || currDay - day > 0)) {
            age--;
        }

        String typeStrYear;

        if (age % 10 == 1) {
            typeStrYear = resources.getString(R.string.typeStrYearOne);
        } else if (age % 10 > 1 && age % 10 < 5) {
            typeStrYear = resources.getString(R.string.typeStrYearTwo);
        } else {
            typeStrYear = resources.getString(R.string.typeStrYearThree);
        }

        String ageStr = resources.getString(R.string.ageShowTeacher);

        return ageStr + " " + age + " " + typeStrYear;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getFullName() {
        return lastName + " " + firstName + " " + fatherName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getOkrug() {
        return okrug;
    }

    public void setOkrug(String okrug) {
        this.okrug = okrug;
    }

    public String getDistrict() {
        return district;
    }

//    public String getStrPriceList() {
//        return strPriceList;
//    }
//
//    public void setStrPriceList(String strPriceList) {
//        this.strPriceList = strPriceList;
//    }
//
//    public String getStrCity() {
//        return strCity;
//    }
//
//    public void setStrCity(String strCity) {
//        this.strCity = strCity;
//    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public List<PriceList> getPriceLists() {
        return priceLists;
    }

    public void setPriceLists(List<PriceList> priceLists) {
        this.priceLists = priceLists;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSubways() {
        return subways;
    }

    public void setSubways(String subways) {
        this.subways = subways;
    }

    public boolean isLeaveHome() {
        return leaveHome;
    }

    public void setLeaveHome(boolean leaveHome) {
        this.leaveHome = leaveHome;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isOnlyDistanceLearning() {
        return onlyDistanceLearning;
    }

    public void setOnlyDistanceLearning(boolean onlyDistanceLearning) {
        this.onlyDistanceLearning = onlyDistanceLearning;
    }

    public boolean isDistanceLearning() {
        return distanceLearning;
    }

    public void setDistanceLearning(boolean distanceLearning) {
        this.distanceLearning = distanceLearning;
    }

    public boolean isShowEmail() {
        return showEmail;
    }

    public void setShowEmail(boolean showEmail) {
        this.showEmail = showEmail;
    }

    public boolean isShowPhone() {
        return showPhone;
    }

    public void setShowPhone(boolean showPhone) {
        this.showPhone = showPhone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(fatherName);
        parcel.writeString(birthDate);
        parcel.writeString(okrug);
        parcel.writeString(district);
        parcel.writeString(description);
        parcel.writeString(email);
        parcel.writeString(phoneNumber);
        parcel.writeString(subways);
        parcel.writeByte((byte) (leaveHome ? 1 : 0));
        parcel.writeByte((byte) (onlyDistanceLearning ? 1 : 0));
        parcel.writeByte((byte) (distanceLearning ? 1 : 0));
        parcel.writeByte((byte) (isFavorite ? 1 : 0));
        parcel.writeByte((byte) (showEmail ? 1 : 0));
        parcel.writeByte((byte) (showPhone ? 1 : 0));
        parcel.writeString(photo);
        if (city != null) {
            parcel.writeString(city.getTitle());
            parcel.writeInt(city.getId());
            parcel.writeByte((byte) (city.isHasSubway() ? 1 : 0 ));
        } else {
            parcel.writeString("");
            parcel.writeInt(-1);
            parcel.writeByte((byte) 0);
        }
        parcel.writeInt(priceLists.size());

        for (PriceList priceList : priceLists) {
            parcel.writeInt(priceList.getPrice());
            parcel.writeString(priceList.getExperience());
            parcel.writeString(priceList.getSubject().getTitle());
        }
    }
}
