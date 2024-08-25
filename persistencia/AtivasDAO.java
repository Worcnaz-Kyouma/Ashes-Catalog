package persistencia;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import vo.Ativas;

public class AtivasDAO {

    EntityManager em;

    public AtivasDAO() {
        em = EntityManagerProvider.getEM();
    }

    public void salva(Ativas p) {
        em.getTransaction().begin();
        if (p.getId() == 0) {
            em.persist(p);
        } else {
            em.merge(p);
        }
        em.getTransaction().commit();
    }

    public Ativas localiza(String id) {
        Ativas p = em.find(Ativas.class, id);
        return p;
    }

    public void exclui(Ativas p) {
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
    }

    public List<Ativas> listaativas() {
        Query q = em.createQuery("select p from Ativas p order by p.id");
        List<Ativas> lista = q.getResultList();
        return lista;
    }
    
}
