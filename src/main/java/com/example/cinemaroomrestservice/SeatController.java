package com.example.cinemaroomrestservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@RestController
public class SeatController {
    private Map<String, Object> requestedData;
    private List<Seat> purchasedSeats;
    private Map<UUID, Seat> tickets;
    private List<Seat> seatList;
    private UUID token;

    {
        requestedData = new HashMap<>();
        tickets = new ConcurrentHashMap<>();
        purchasedSeats = new ArrayList<>();
        requestedData.put("total_rows", 9);
        requestedData.put("total_columns", 9);
        seatList = new ArrayList<>(81);
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                int price = i <= 4 ? 10 : 8;
                Seat seat = new Seat(i, j, price);
                seatList.add(seat);
            }
        }
        requestedData.put("available_seats", seatList);
    }

    @GetMapping("/seats")
    public Map<String, Object> getSeats() {
        return requestedData;
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseSeat(@RequestBody Seat seat) {
        if (seat.getColumn() < 1 || seat.getColumn() > 9 || seat.getRow() < 1 || seat.getRow() > 9) {
            return new ResponseEntity<>(Map.of("error", "The number of a row or a column is out of bounds!"), HttpStatus.BAD_REQUEST);
        }
        long c = purchasedSeats.stream()
                .filter(s -> seat.getRow() == s.getRow() && seat.getColumn() == s.getColumn())
                .count();
        if (c == 0) {
            int price = seat.getRow() <= 4 ? 10 : 8;
            seat.setPrice(price);
            purchasedSeats.add(seat);

            token = UUID.randomUUID();
            tickets.put(token, seat);
            return new ResponseEntity<>(Map.of("token", token, "ticket", seat), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("error", "The ticket has been already purchased!"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/return")
    public ResponseEntity<?> purchaseSeat(@RequestBody Token token) {
        if(tickets.containsKey(token.getToken())) {
            Seat s = tickets.get(token.getToken());
            tickets.remove(token.getToken());
            return new ResponseEntity<>(Map.of("returned_ticket", s), HttpStatus.OK);
        }
        return new ResponseEntity<>(Map.of("error", "Wrong token!"), HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/stats")
    private ResponseEntity<?> stat(@RequestParam(required = false) String password) {
        if (password != null && password.equals("super_secret")) {
            int number_of_purchased_tickets = tickets.size();
            int number_of_available_seats = 81 - number_of_purchased_tickets;
            int current_income = 0;
            for (Map.Entry<UUID, Seat> s : tickets.entrySet()) {
                current_income = current_income + s.getValue().getPrice();
            }
            return new ResponseEntity<>(Map.of("current_income", current_income,
                    "number_of_available_seats", number_of_available_seats,
                    "number_of_purchased_tickets", number_of_purchased_tickets), HttpStatus.OK);
        }

        return new ResponseEntity<>(Map.of("error", "The password is wrong!"), HttpStatus.UNAUTHORIZED);
    }
}