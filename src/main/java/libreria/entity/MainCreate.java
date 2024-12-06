package libreria.entity;

import com.github.javafaker.Faker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import libreria.Dao.LibroDAO;
import libreria.Dao.PrestitoDAO;
import libreria.Dao.RivistaDAO;
import libreria.Dao.UtenteDAO;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class MainCreate {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unit-jpa");
        EntityManager em = emf.createEntityManager();

        UtenteDAO utenteDAO = new UtenteDAO(em);
        Faker faker = new Faker();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            Utente utente = new Utente();
            utente.setNome(faker.name().firstName());
            utente.setCognome(faker.name().lastName());
            utente.setDataDiNascita(
                    LocalDate.of(
                            random.nextInt(60) + 1960,
                            random.nextInt(12) + 1,
                            random.nextInt(28) + 1
                    )
            );
            utente.setNumeroDiTessera((long) faker.number().numberBetween(10000, 99999));

            utenteDAO.save(utente);
        }

        LibroDAO libroDAO = new LibroDAO(em);
        RivistaDAO rivistaDAO = new RivistaDAO(em);


        for (int i = 0; i < 10; i++) {
            Libro libro = new Libro();
            libro.setCodiceISBN(faker.code().isbn13());
            libro.setTitolo(faker.book().title());
            libro.setAnnoDiPubblicazione(
                    LocalDate.of(
                            random.nextInt(30) + 1990,
                            random.nextInt(12) + 1,
                            random.nextInt(28) + 1
                    )
            );
            libro.setNumeroPagine(random.nextInt(900) + 100);
            libro.setAutore(faker.book().author());
            libro.setGenere(faker.book().genre());

            libroDAO.save(libro);
        }


        for (int i = 0; i < 10; i++) {
            Rivista rivista = new Rivista();
            rivista.setCodiceISBN(faker.code().isbn13());
            rivista.setTitolo(faker.lorem().sentence(3));
            rivista.setAnnoDiPubblicazione(
                    LocalDate.of(
                            random.nextInt(30) + 1990,
                            random.nextInt(12) + 1,
                            random.nextInt(28) + 1
                    )
            );
            rivista.setNumeroPagine(random.nextInt(100) + 20);
            rivista.setPeriodicita(Periodicita.values()[random.nextInt(Periodicita.values().length)]);

            rivistaDAO.save(rivista);
        }

        PrestitoDAO prestitoDAO = new PrestitoDAO(em);

        List<Utente> utenti = utenteDAO.findAll();

        List<Catalogo> cataloghi = em.createQuery("SELECT c FROM Catalogo c", Catalogo.class).getResultList();

        if (utenti.isEmpty() || cataloghi.isEmpty()) {

            return;
        }

        for (int i = 0; i < utenti.size(); i++) {
            Utente utente = utenti.get(i);

            Prestito prestito = new Prestito();
            prestito.setUtente(utente);

            Catalogo elementoPrestato = cataloghi.get(random.nextInt(cataloghi.size()));
            prestito.setElementoPrestato(elementoPrestato);

            LocalDate dataInizio = LocalDate.now().minusDays(random.nextInt(30) + 1);
            prestito.setDataInizioPrestito(dataInizio);

            LocalDate dataRestituzione = dataInizio.plusDays(15);
            prestito.setDataRestituzionePrevista(dataRestituzione);

            if (i < 5) {
                prestito.setDataRestituzioneEffettivo("Scaduto e non restitutito");
            } else {
                prestito.setDataRestituzioneEffettivo(dataRestituzione.minusDays(random.nextInt(5)).toString());
            }

            prestitoDAO.save(prestito);
        }

    }
}
