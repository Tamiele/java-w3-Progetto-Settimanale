package libreria.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "libri")
@NamedQuery(name = "Trova_tutto_Libro", query = "SELECT a FROM Libro a")
public class Libro extends Catalogo {

    @Column(nullable = false)
    private String autore;

    @Column(nullable = false)
    private String genere;

    @Override
    public String toString() {
        return "Libro{" +
                "titolo ='" + getTitolo() + '\'' +
                ", codice ISBN ='" + getCodiceISBN() + '\'' +
                ", Anno Di Pubblicazione =" + getAnnoDiPubblicazione() +
                ", numero Pagine =" + getNumeroPagine() +
                ", autore ='" + getAutore() + '\'' +
                ", genere ='" + getGenere() + '\'' +
                '}';
    }

}