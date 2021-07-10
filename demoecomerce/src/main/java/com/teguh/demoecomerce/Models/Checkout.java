package com.teguh.demoecomerce.Models;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Checkout {
    private String nama;
    private String harga;
    private Integer jumlah;
    private Integer totalharga;
}
