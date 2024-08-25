package persistencia;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import vo.Personagens;

public class PersonagensDAO {

    EntityManager em;

    public PersonagensDAO() {
        em = EntityManagerProvider.getEM();
    }

    public void salva(Personagens p) {
        em.getTransaction().begin();
        if (p.getId() == 0) {
            em.persist(p);
        } else {
            em.merge(p);
        }
        em.getTransaction().commit();
    }

    public Personagens localiza(int id) {
        Personagens p = em.find(Personagens.class, id);
        return p;
    }

    public void exclui(Personagens p) {
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
    }

    public List<Personagens> listapersonagens() {
        Query q = em.createQuery("select p from Personagens p order by p.id");
        List<Personagens> lista = q.getResultList();
        return lista;
    }

    public Personagens localizaa(String nome) {
        Query q = em.createQuery("select p from Personagens p where p.nome= :nome order by p.id");
        q.setParameter("nome", nome);
        List<Personagens> lista = q.getResultList();
        if (lista.isEmpty() == false) {
            Personagens p = lista.get(0);
            return p;
        } else {
            return null;

        }
    }


}
