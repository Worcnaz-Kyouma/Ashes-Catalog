/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
public class EntityManagerProvider {
 private static EntityManagerFactory emf = null;
 private static EntityManager em = null;
 private EntityManagerProvider() {
 }
 public static EntityManagerFactory getEMF() { 
 if (emf == null) {
 emf = Persistence.createEntityManagerFactory("Ashes_CatalogPU");
 }
 return emf;
 }
 public static EntityManager getEM() {
 if (em == null) {
 em = getEMF().createEntityManager();
 }
 return em;
 }
 
} 

