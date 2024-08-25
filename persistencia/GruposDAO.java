package persistencia;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import vo.Grupos;

public class GruposDAO {

    EntityManager em;

    public GruposDAO() {
        em = EntityManagerProvider.getEM();
    }

    public void salva(Grupos p) {
        em.getTransaction().begin();
        if (p.getId() == 0) {
            em.persist(p);
        } else {
            em.merge(p);
        }
        em.getTransaction().commit();
    }

    public Grupos localiza(String id) {
        Grupos p = em.find(Grupos.class, id);
        return p;
    }

    public void exclui(Grupos p) {
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
    }

    public List<Grupos> listagrupos() {
        Query q = em.createQuery("select p from Grupos p order by p.id");
        List<Grupos> lista = q.getResultList();
        return lista;
    }
    
}
