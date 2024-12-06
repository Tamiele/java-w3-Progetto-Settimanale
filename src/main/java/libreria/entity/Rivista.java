package libreria.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "rivista")
@NamedQuery(name = "Trova_tutto_Rivista", query = "SELECT a FROM Rivista a")
public class Rivista extends Catalogo {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Periodicita periodicita;

    @Override
    public String toString() {
        return "Rivista{" +
                "titolo ='" + getTitolo() + '\'' +
                ", codice ISBN ='" + getCodiceISBN() + '\'' +
                ", Anno Di Pubblicazione =" + getAnnoDiPubblicazione() +
                ", numero Pagine =" + getNumeroPagine() +
                ", periodo =" + periodicita +
                '}';
    }
}