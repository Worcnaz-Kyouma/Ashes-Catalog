package persistencia;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import vo.Itens;

public class ItensDAO {

    EntityManager em;

    public ItensDAO() {
        em = EntityManagerProvider.getEM();
    }

    public void salva(Itens p) {
        em.getTransaction().begin();
        if (p.getId() == 0) {
            em.persist(p);
        } else {
            em.merge(p);
        }
        em.getTransaction().commit();
    }

    public Itens localiza(String id) {
        Itens p = em.find(Itens.class, id);
        return p;
    }

    public void exclui(Itens p) {
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
    }

    public List<Itens> listaitens() {
        Query q = em.createQuery("select p from Itens p order by p.id");
        List<Itens> lista = q.getResultList();
        return lista;
    }
    
}
