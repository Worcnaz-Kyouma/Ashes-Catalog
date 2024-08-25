package persistencia;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import vo.Acontecimentos;

public class AcontecimentosDAO {

    EntityManager em;

    public AcontecimentosDAO() {
        em = EntityManagerProvider.getEM();
    }

    public void salva(Acontecimentos p) {
        em.getTransaction().begin();
        if (p.getId() == 0) {
            em.persist(p);
        } else {
            em.merge(p);
        }
        em.getTransaction().commit();
    }

    public Acontecimentos localiza(String id) {
        Acontecimentos p = em.find(Acontecimentos.class, id);
        return p;
    }

    public void exclui(Acontecimentos p) {
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
    }

    public List<Acontecimentos> listaacontecimentos() {
        Query q = em.createQuery("select p from Acontecimentos p order by p.id");
        List<Acontecimentos> lista = q.getResultList();
        return lista;
    }
    
}
