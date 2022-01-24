package com.example.cinemaroomrestservice;

public class Seat {
    private int row;
    private int column;
    private int price;

    public Seat() {}

    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Seat(int row, int column, int price) {
        this.row = row;
        this.column = column;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}

