package libreria.entity;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import libreria.Dao.CatalogoDAO;
import libreria.Dao.LibroDAO;
import libreria.Dao.PrestitoDAO;
import libreria.Dao.RivistaDAO;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class MainUpdate {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unit-jpa");
        EntityManager em = emf.createEntityManager();

        CatalogoDAO catalogoDAO = new CatalogoDAO(em);
        LibroDAO libroDAO = new LibroDAO(em);
        RivistaDAO rivistaDAO = new RivistaDAO(em);
        PrestitoDAO prestitoDAO = new PrestitoDAO(em);

        try (Scanner scanner = new Scanner(System.in)) {
            boolean exit = false;

            while (!exit) {
                System.out.println("==== MENU ====");
                System.out.println("1. Aggiungi un elemento (Libro o Rivista)");
                System.out.println("2. Cerca un elemento tramite codice ISBN");
                System.out.println("3. Elimina un elemento tramite codice ISBN");
                System.out.println("4. Ricerca per anno di pubblicazione");
                System.out.println("5. Ricerca per nome autore");
                System.out.println("6. Ricerca per Titolo o Titolo Parziale");
                System.out.println("7. Ricerca prestiti per numero tessera");
                System.out.println("8. Ricerca tutti i prestiti scaduti e non restituiti");
                System.out.println("0. Esci");
                System.out.print("Scegli un'opzione: ");

                int scelta = scanner.nextInt();
                scanner.nextLine();

                switch (scelta) {
                    case 1:
                        aggiungiElemento(scanner, libroDAO, rivistaDAO);
                        break;
                    case 2:
                        cercaElemento(scanner, catalogoDAO);
                        break;
                    case 3:
                        eliminaElemento(scanner, catalogoDAO);
                        break;
                    case 4:
                        cercaPerAnno(scanner, catalogoDAO);
                        break;
                    case 5:
                        cercaPerNomeAutore(scanner, libroDAO);
                        break;

                    case 6:
                        cercaPerTitolo(scanner, catalogoDAO);
                        break;
                    case 7:
                        cercaPerNumeroTessera(scanner, prestitoDAO);
                        break;
                    case 8:
                        cercaPrestitiScadutiENonRestituiti(prestitoDAO);
                        break;

                    case 0:
                        exit = true;
                        System.out.println("Uscita dal programma...");
                        break;
                    default:
                        System.out.println("Opzione non valida! Riprova.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }

    }

    private static void aggiungiElemento(Scanner scanner, LibroDAO libroDAO, RivistaDAO rivistaDAO) {
        System.out.println("Che tipo di elemento vuoi aggiungere? (1 per Libro, 2 per Rivista)");
        int scelta = scanner.nextInt();
        scanner.nextLine();

        if (scelta == 1) {
            libroDAO.aggiungiLibro(scanner);
        } else if (scelta == 2) {
            rivistaDAO.aggiungiRivista(scanner);
        } else {
            System.out.println("Scelta non valida!");
        }
    }

    private static void cercaElemento(Scanner scanner, CatalogoDAO catalogoDAO) {
        System.out.println("Inserisci il codice ISBN dell'elemento da cercare:");
        String codiceISBN = scanner.nextLine();

        try {
            Catalogo elemento = catalogoDAO.findByISBN(codiceISBN);
            System.out.println("Elemento trovato: " + elemento);
        } catch (Exception e) {
            System.out.println("Elemento non trovato con il codice ISBN: " + codiceISBN);
        }
    }

    private static void eliminaElemento(Scanner scanner, CatalogoDAO catalogoDAO) {
        System.out.println("Inserisci il codice ISBN dell'elemento da eliminare:");
        String codiceISBN = scanner.nextLine();

        try {
            catalogoDAO.deleteByISBN(codiceISBN);
            System.out.println("Elemento eliminato con successo!");
        } catch (Exception e) {
            System.out.println("Elemento non trovato o errore nell'eliminazione.");
        }
    }

    private static void cercaPerAnno(Scanner scanner, CatalogoDAO catalogoDAO) {
        System.out.println("Inserisci l'anno di pubblicazione (formato: YYYY-MM-DD):");
        String inputUtente = scanner.nextLine();

        try {
            LocalDate data = LocalDate.parse(inputUtente);
            List<Catalogo> risultati = catalogoDAO.findByAnnoDiPubblicazione(data);

            if (risultati.isEmpty()) {
                System.out.println("Nessun elemento trovato per l'anno di pubblicazione: " + inputUtente);
            } else {
                System.out.println("Elementi trovati:");
                risultati.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Errore: formato della data non valido o nessun elemento trovato.");
        }
    }

    private static void cercaPerNomeAutore(Scanner scanner, LibroDAO libroDAO) {
        System.out.println("Inserisci il nome dell'autore:");
        String inputUtente = scanner.nextLine();

        try {
            List<Libro> risultati = libroDAO.findByNomeAutore(inputUtente);

            if (risultati.isEmpty()) {
                System.out.println("Nessun elemento trovato per nome autore: " + inputUtente);
            } else {
                System.out.println("Elementi trovati:");
                risultati.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Errore: formato della data non valido o nessun elemento trovato.");
        }
    }

    private static void cercaPerTitolo(Scanner scanner, CatalogoDAO catalogoDAO) {
        System.out.println("Inserisci il titolo o parte del titolo:");
        String inputUtente = scanner.nextLine();

        try {
            List<Catalogo> risultati = catalogoDAO.findByTitolOPartTitol(inputUtente);

            if (risultati.isEmpty()) {
                System.out.println("Nessun elemento trovato con il titolo o parte di esso: " + inputUtente);
            } else {
                System.out.println("Elementi trovati:");
                risultati.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Errore durante la ricerca per titolo.");
        }
    }


    private static void cercaPerNumeroTessera(Scanner scanner, PrestitoDAO prestitoDAO) {
        System.out.println("Inserisci il numero di tessera dell'utente:");
        Long numeroTessera = scanner.nextLong();
        scanner.nextLine();

        try {
            List<Prestito> prestiti = prestitoDAO.cercaPerNumeroTessera(numeroTessera);

            if (prestiti.isEmpty()) {
                System.out.println("Nessun prestito trovato per il numero tessera: " + numeroTessera);
            } else {
                System.out.println("Prestiti trovati:");
                prestiti.forEach(prestito -> {
                    System.out.println("Prestito ID: " + prestito.getId());
                    System.out.println("Data inizio prestito: " + prestito.getDataInizioPrestito());
                    System.out.println("Data restituzione prevista: " + prestito.getDataRestituzionePrevista());
                    System.out.println("Data restituzione effettiva: " + prestito.getDataRestituzioneEffettivo());
                    System.out.println("Elemento prestato ID: " + prestito.getElementoPrestato().getId());

                });
            }
        } catch (Exception e) {
            System.out.println("Errore durante la ricerca.");
        }
    }

    private static void cercaPrestitiScadutiENonRestituiti(PrestitoDAO prestitoDAO) {
        System.out.println("Ricerca dei prestiti scaduti e ancora non restituiti...");

        try {
            List<Prestito> prestiti = prestitoDAO.findPrestitiScadutiENonRestituiti();

            if (prestiti.isEmpty()) {
                System.out.println("Non ci sono prestiti scaduti e ancora non restituiti.");
            } else {
                System.out.println("Prestiti trovati:");

                prestiti.forEach(prestito -> {
                    System.out.println("Prestito ID: " + prestito.getId());
                    System.out.println("Utente ID: " + prestito.getUtente().getId());
                    System.out.println("Utente Nome: " + prestito.getUtente().getNome() + " " + prestito.getUtente().getCognome());
                    System.out.println("Elemento Prestato ID: " + prestito.getElementoPrestato().getId());
                    System.out.println("Data di inizio prestito: " + prestito.getDataInizioPrestito());
                    System.out.println("Data di scadenza prestito: " + prestito.getDataRestituzionePrevista());

                });
            }
        } catch (Exception e) {
            System.out.println("Errore durante la ricerca dei prestiti scaduti.");
        }
    }


}