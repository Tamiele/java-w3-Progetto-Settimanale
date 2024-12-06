package libreria.Dao;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import libreria.entity.Prestito;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
public class PrestitoDAO {
    private EntityManager em;

    public void save(Prestito oggetto) {
        em.getTransaction().begin();
        em.persist(oggetto);
        em.getTransaction().commit();
    }

    public Prestito findById(Long id) {
        return em.find(Prestito.class, id);
    }

    public List<Prestito> findAll() {
        return em.createNamedQuery("Trova_tutto_Prestito", Prestito.class).getResultList();
    }

    public void update(Prestito oggetto) {
        em.getTransaction().begin();
        em.merge(oggetto);
        em.getTransaction().commit();
    }

    public void delete(Prestito oggetto) {
        em.getTransaction().begin();
        em.remove(oggetto);
        em.getTransaction().commit();
    }

    public List<Prestito> cercaPerNumeroTessera(Long numeroDiTessera) {
        TypedQuery<Prestito> query = em.createQuery("SELECT p FROM Prestito p WHERE p.utente.numeroDiTessera = :numeroDiTessera", Prestito.class);
        query.setParameter("numeroDiTessera", numeroDiTessera);
        return query.getResultList();
    }


    public List<Prestito> findPrestitiScadutiENonRestituiti() {
        TypedQuery<Prestito> query = em.createQuery(
                "SELECT p FROM Prestito p " +
                        "WHERE (p.dataRestituzioneEffettivo IS NULL OR p.dataRestituzioneEffettivo = '') " +
                        "AND p.dataRestituzionePrevista < :oggi", Prestito.class
        );
        query.setParameter("oggi", LocalDate.now());
        return query.getResultList();
    }


}