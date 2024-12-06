package libreria.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "catalogo")

@Inheritance(strategy = InheritanceType.JOINED)

@NamedQuery(name = "Trova_tutto_Catalogo", query = "SELECT a FROM Catalogo a")
public abstract class Catalogo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "codice_isbn", nullable = false)
    private String codiceISBN;

    @Column(nullable = false)
    private String titolo;

    @Column(name = "anno_di_pubblicazione", nullable = false)
    private LocalDate annoDiPubblicazione;

    @Column(name = "numero_pagine", nullable = false)
    private int numeroPagine;


}