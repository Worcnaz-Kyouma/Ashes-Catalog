package persistencia;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import vo.Racas;

public class RacasDAO {

    EntityManager em;

    public RacasDAO() {
        em = EntityManagerProvider.getEM();
    }

    public void salva(Racas p) {
        em.getTransaction().begin();
        if (p.getId() == 0) {
            em.persist(p);
        } else {
            em.merge(p);
        }
        em.getTransaction().commit();
    }

    public Racas localiza(String id) {
        Racas p = em.find(Racas.class, id);
        return p;
    }

    public void exclui(Racas p) {
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
    }

    public List<Racas> listaracas() {
        Query q = em.createQuery("select p from Racas p order by p.id");
        List<Racas> lista = q.getResultList();
        return lista;
    }
    
}
