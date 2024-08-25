package persistencia;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import vo.Passivas;

public class PassivasDAO {

    EntityManager em;

    public PassivasDAO() {
        em = EntityManagerProvider.getEM();
    }

    public void salva(Passivas p) {
        em.getTransaction().begin();
        if (p.getId() == 0) {
            em.persist(p);
        } else {
            em.merge(p);
        }
        em.getTransaction().commit();
    }

    public Passivas localiza(int id) {
        Passivas p = em.find(Passivas.class, id);
        return p;
    }

    public void exclui(Passivas p) {
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
    }

    public List<Passivas> listapassivas() {
        Query q = em.createQuery("select p from Passivas p order by p.id");
        List<Passivas> lista = q.getResultList();
        return lista;
    }

    public List<Passivas> listapassivasdisp(String nada, String portador) {
        Query q = em.createQuery("select p from Passivas p where p.portador= :nada or p.portador= :portador order by p.id");
        q.setParameter("nada", nada);
        q.setParameter("portador", portador);
        List<Passivas> lista = q.getResultList();
        return lista;
    }

    public Passivas localizaa(String nome) {
        Query q = em.createQuery("select p from Passivas p where p.nome= :nome order by p.id");
        q.setParameter("nome", nome);
        List<Passivas> lista = q.getResultList();
        if (lista.isEmpty() == false) {
            Passivas p = lista.get(0);
            return p;
        } else {
            return null;

        }
    }

}
