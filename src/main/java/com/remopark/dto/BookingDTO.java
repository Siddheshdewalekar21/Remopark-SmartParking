package com.remopark.dto;

import com.remopark.model.Booking;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingDTO {
    private Long id;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String spotCode;
    private String centerName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private Double price;

    public static BookingDTO fromBooking(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setUserName(booking.getUser().getFullName());
        dto.setUserEmail(booking.getUser().getEmail());
        dto.setUserPhone(booking.getUser().getPhone());
        dto.setSpotCode(booking.getSpotCode());
        dto.setCenterName(booking.getParkingCenter().getName());
        dto.setStartTime(booking.getStartTime());
        dto.setEndTime(booking.getEndTime());
        dto.setStatus("BOOKED");
        dto.setPrice(booking.getPrice());
        return dto;
    }
} 