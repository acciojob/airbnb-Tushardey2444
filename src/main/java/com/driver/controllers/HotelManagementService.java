package com.driver.controllers;
import com.driver.model.User;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.Booking;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
public class HotelManagementService {
    HashMap<String, Hotel> hotels = new HashMap<>();
    HashMap<Integer, User> users = new HashMap<>();
    HashMap<String,Booking> bookings=new HashMap<>();

    public String addHotel(Hotel hotel) {
        if (hotel == null) {
            return "FAILURE";
        }
        if (hotel.getHotelName() == null) {
            return "FAILURE";
        }
        if (this.hotels.containsKey(hotel.getHotelName())) {
            return "FAILURE";
        }
        this.hotels.put(hotel.getHotelName(), hotel);
        return "SUCCESS";
    }

    public Integer addUser(User user) {
        users.put(user.getaadharCardNo(), user);
        return user.getaadharCardNo();
    }
    public String getMostFailities(){
        int max=0;
        String mostFacilityHotelName="";
        for(String names:hotels.keySet()){
            if(hotels.get(names).getFacilities().size()>0){
                if(hotels.get(names).getFacilities().size()>max){
                    max=hotels.get(names).getFacilities().size();
                    mostFacilityHotelName=hotels.get(names).getHotelName();
                }else if(hotels.get(names).getFacilities().size()==max){
                    if(mostFacilityHotelName.compareTo(names)>0){
                        mostFacilityHotelName=names;
                    }
                }
            }
        }
        return mostFacilityHotelName;
    }
    public int bookARoom(Booking booking){
        UUID uuid=UUID.randomUUID();
        String bookingId=uuid.toString();
        booking.setBookingId(bookingId);
        String hotelName=booking.getHotelName();
        if(hotels.get(hotelName).getAvailableRooms()<booking.getNoOfRooms()){
            return -1;
        }
        int amountToBePaid=booking.getNoOfRooms()*hotels.get(hotelName).getPricePerNight();
        booking.setAmountToBePaid(amountToBePaid);
        this.bookings.put(bookingId,booking);
        hotels.get(hotelName).setAvailableRooms(hotels.get(hotelName).getAvailableRooms()-booking.getNoOfRooms());
        return amountToBePaid;
    }
    public int getBooking(Integer aadharCard){
        int booking=0;
        for(String bk:bookings.keySet()){
            if(bookings.get(bk).getBookingAadharCard()==aadharCard){
                booking++;
            }
        }
        return booking;
    }
    public Hotel updateFacility(String hotelName,List<Facility> newFacilities){
        Hotel hotel=hotels.get(hotelName);
        List<Facility> oldFacilities=hotel.getFacilities();

        for(Facility faci:newFacilities){
            if(!oldFacilities.contains(faci)){
                oldFacilities.add(faci);
            }
        }
        hotel.setFacilities(oldFacilities);
        hotels.put(hotelName,hotel);
        return hotel;
    }
}
