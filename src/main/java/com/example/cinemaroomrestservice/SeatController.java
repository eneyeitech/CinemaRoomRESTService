package com.example.cinemaroomrestservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SeatController {
    private Map<String, Object> requestedData;
    private List<Seat> seatList;

    public SeatController() {
        requestedData = new HashMap<>();
        requestedData.put("total_rows", 9);
        requestedData.put("total_columns", 9);
        seatList = new ArrayList<>(81);
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                Seat seat = new Seat(i, j);
                seatList.add(seat);
            }
        }
        requestedData.put("available_seats", seatList);
    }

    @GetMapping("/seats")
    public Map<String, Object> getSeats() {
        return requestedData;
    }
}
