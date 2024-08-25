/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.primefaces.event.UnselectEvent;
import persistencia.AcontecimentosDAO;
import persistencia.AtivasDAO;
import persistencia.GruposDAO;
import persistencia.ItensDAO;
import persistencia.PassivasDAO;
import persistencia.PersonagensDAO;
import persistencia.RacasDAO;
import vo.Acontecimentos;
import vo.Ativas;
import vo.Grupos;
import vo.Itens;
import vo.Passivas;
import vo.Personagens;
import vo.Racas;

@ManagedBean
@SessionScoped

/**
 *
 * @author nicol
 */
public class TelaRpgBean implements Serializable {

    private Ativas ativas = new Ativas();
    private AtivasDAO pa = new AtivasDAO();
    private DataModel<Ativas> ativaslist = new ListDataModel(getPa().listaativas());

    private Passivas passivas = new Passivas();
    private PassivasDAO pp = new PassivasDAO();
    private DataModel<Passivas> passivaslist = new ListDataModel(getPp().listapassivas());

    private Acontecimentos acontecimentos = new Acontecimentos();
    private AcontecimentosDAO pac = new AcontecimentosDAO();
    private DataModel<Acontecimentos> acontecimentoslist = new ListDataModel(getPac().listaacontecimentos());

    private Grupos grupos = new Grupos();
    private GruposDAO pg = new GruposDAO();
    private DataModel<Grupos> gruposlist = new ListDataModel(getPg().listagrupos());

    private Itens itens = new Itens();
    private ItensDAO pi = new ItensDAO();
    private DataModel<Itens> itenslist = new ListDataModel(getPi().listaitens());

    private Personagens personagens = new Personagens();
    private PersonagensDAO pper = new PersonagensDAO();
    private DataModel<Personagens> personagenslist = new ListDataModel(getPper().listapersonagens());

    private Racas racas = new Racas();
    private RacasDAO pr = new RacasDAO();
    private DataModel<Racas> racaslist = new ListDataModel(getPr().listaracas());

    //---------
    private String forca, constituicao, destreza, sabedoria, inteligencia, carisma;
    private List<String> reinosselected = new ArrayList();
    private List<String> ativasselected = new ArrayList();
    private List<String> passivasselected = new ArrayList();
    private List<String> racasusadas = new ArrayList();
    private List<Racas> racasusadaslist = new ArrayList(getPr().listaracas());
    private List<String> gruposusados = new ArrayList();
    private List<Grupos> gruposusadoslist = new ArrayList(getPg().listagrupos());
    private List<String> ativasusadas = new ArrayList();
    private List<Ativas> ativasusadaslist = new ArrayList(getPa().listaativas());
    private List<String> personagensusadas = new ArrayList();
    private List<Passivas> passivasusadaslist = new ArrayList(getPp().listapassivasdisp("", ""));
    private List<String> passivasusadas = new ArrayList();
    private List<Personagens> personagensusadaslist = new ArrayList(getPper().listapersonagens());
    private List<String> classes = new ArrayList<>(Arrays.asList("Warrior", "Mage"));
    private List<String> ranksativas = new ArrayList<>(Arrays.asList("Entidade", "Deus", "Dragão", "Rei", "Avançada", "Intermediária", "Primaria"));
    private List<String> rankspassivas = new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4"));
    private List<String> reinos = new ArrayList<>(Arrays.asList("Tenebris", "Vikaedia", "Helsbury", "Reandry", "Krastia", "Kilkhamia", "Serathia", "Luthia", "Hedoria", "Warlow", "Larith", "Maveria", "Mytrea", "Traerean", "Continental"));
    private Boolean oficialb = true;
    private Boolean inoficialb = true;
    private int cont = 0;

    //Passivas------------
    public String irpassivas() { //revisar
        passivaslist = new ListDataModel(getPp().listapassivas());
        passivas = new Passivas();
        ativasusadaslist = new ArrayList(getPa().listaativas());
        racasusadaslist = new ArrayList(getPr().listaracas());
        gruposusadoslist = new ArrayList(getPg().listagrupos());
        return "passivas";
    }

    public String criapassiva() {
        passivas = new Passivas();
        for (int c = 0; c < getPersonagensusadaslist().size(); c++) {
            personagensusadas.add(getPersonagensusadaslist().get(c).getNome());
        }
        return "criapassiva";
    }

    public String adicionarpassiva() {
        Personagens antigoportador = null;
        for (int y = 0; y < pper.listapersonagens().size(); y++) {
            if (pper.listapersonagens().get(y).getPassivas().contains(passivas.getNome())) {
                antigoportador = pper.listapersonagens().get(y);
                System.out.println(pper.listapersonagens().get(y).getPassivas() + "96");
            }
        }
        if (antigoportador != null) {
            String antigoport = "";
            System.out.println(antigoportador.getPassivas() + "97");
            while (cont < antigoportador.getPassivas().length()) {
                String port = achanastring(antigoportador.getPassivas().toCharArray(), cont);
                System.out.println(port + "98");
                if (port.equals(passivas.getNome())) {
                    System.out.println(port + "99");
                } else {
                    if (antigoport == "") {
                        antigoport = port;
                    } else {
                        antigoport = antigoport + "|" + port;
                    }
                }
            }
            cont = 0;
            antigoportador.setPassivas(antigoport);
            pper.salva(antigoportador);
        }

        Personagens portadoratual = pper.localizaa(passivas.getPortador());
        if (portadoratual != null) {
            if (portadoratual.getPassivas() == "" || portadoratual.getPassivas() == null) {
                portadoratual.setPassivas(passivas.getNome());
            } else {
                portadoratual.setPassivas(portadoratual.getPassivas() + "|" + passivas.getNome());
            }
        }
        pp.salva(passivas);
        passivas = new Passivas();
        passivaslist = new ListDataModel(getPp().listapassivas());
        personagensusadas.clear();
        return "passivas";
    }

    public String editarpassiva() {
        passivas = passivaslist.getRowData();
        for (int c = 0; c < getPersonagensusadaslist().size(); c++) {
            personagensusadas.add(getPersonagensusadaslist().get(c).getNome());
        }
        return "criapassiva";
    }

    public void excluirpassiva() {
        pp.exclui(passivas);
        Personagens antigoportador = null;
        for (int y = 0; y < pper.listapersonagens().size(); y++) {
            if (pper.listapersonagens().get(y).getPassivas().contains(passivas.getNome())) {
                antigoportador = pper.listapersonagens().get(y);
            }
        }
        if (antigoportador != null) {
            String antigoport = "";
            while (cont < antigoportador.getPassivas().length()) {
                String port = achanastring(antigoportador.getPassivas().toCharArray(), cont);
                if (port.equals(passivas.getNome())) {
                } else {
                    if (antigoport == "") {
                        antigoport = port;
                    } else {
                        antigoport = antigoport + "|" + port;
                    }
                }
            }
            cont = 0;
            antigoportador.setPassivas(antigoport);
            pper.salva(antigoportador);
        }
        passivaslist = new ListDataModel(getPp().listapassivas());
    }

    public String voltarpassivas() {
        passivas = new Passivas();
        passivaslist = new ListDataModel(getPp().listapassivas());
        personagensusadas.clear();
        return "passivas";
    }

    //Ativas--------------
    public String irativas() {
        ativaslist = new ListDataModel(getPa().listaativas());
        ativas = new Ativas();
        ativasusadaslist = new ArrayList(getPa().listaativas());
        racasusadaslist = new ArrayList(getPr().listaracas());
        gruposusadoslist = new ArrayList(getPg().listagrupos());
        return "ativas";
    }

    public String criaativa() {
        ativas = new Ativas();
        return "criaativa";
    }

    public String adicionarativa() {
        ativas.setReq(forca + " " + constituicao + " " + destreza + " " + sabedoria + " " + inteligencia + " " + carisma);
        pa.salva(ativas);
        ativas = new Ativas();
        forca = " ";
        constituicao = " ";
        destreza = " ";
        sabedoria = " ";
        inteligencia = " ";
        carisma = " ";
        ativaslist = new ListDataModel(getPa().listaativas());
        return "ativas";
    }

    public String editarativa() {
        ativas = ativaslist.getRowData();
        return "editaativa";
    }

    public void excluirativa() {
        pa.exclui(ativas);
        ativaslist = new ListDataModel(getPa().listaativas());
    }

    public String voltarativas() {
        ativas = new Ativas();
        forca = "";
        constituicao = "";
        destreza = "";
        sabedoria = "";
        inteligencia = "";
        carisma = "";
        ativaslist = new ListDataModel(getPa().listaativas());
        return "ativas";
    }

    //Acontecimentos------
    public String iracontecimentos() {
        acontecimentoslist = new ListDataModel(getPac().listaacontecimentos());
        acontecimentos = new Acontecimentos();
        ativasusadaslist = new ArrayList(getPa().listaativas());
        racasusadaslist = new ArrayList(getPr().listaracas());
        gruposusadoslist = new ArrayList(getPg().listagrupos());
        return "acontecimentos";
    }

    public String criaacontecimento() {
        acontecimentos = new Acontecimentos();
        return "criaacontecimento";
    }

    public String adicionaracontecimento() {
        acontecimentos.setReinos(null);
        for (int c = 0; c < reinosselected.size(); c++) {
            if (acontecimentos.getReinos() == null || acontecimentos.getReinos() == "") {
                acontecimentos.setReinos(reinosselected.get(c));
            } else {
                acontecimentos.setReinos(acontecimentos.getReinos() + "|" + reinosselected.get(c));
            }
        }
        if (reinosselected.isEmpty()) {
            acontecimentos.setReinos("");
        }
        pac.salva(acontecimentos);
        acontecimentos = new Acontecimentos();
        acontecimentoslist = new ListDataModel(getPac().listaacontecimentos());
        reinosselected.clear();
        return "acontecimentos";
    }

    public String editaracontecimento() {
        acontecimentos = acontecimentoslist.getRowData();
        char[] texto = acontecimentos.getReinos().toCharArray();
        String reino = "";
        int c = 0;
        while (c < acontecimentos.getReinos().length()) {
            while (texto[c] != '|') {
                reino = reino + texto[c];
                c++;
                if (c >= acontecimentos.getReinos().length()) {
                    c--;
                    texto[c] = '|';
                }
            }
            reinosselected.add(reino);
            reino = "";
            c++;
        }//Botão pra limpar reinos do edita

        return "criaacontecimento";
    }

    public void excluiracontecimento() {
        pac.exclui(acontecimentos);
        acontecimentoslist = new ListDataModel(getPac().listaacontecimentos());
    }

    public String voltaracontecimentos() {
        acontecimentos = new Acontecimentos();
        acontecimentoslist = new ListDataModel(getPac().listaacontecimentos());
        if (reinosselected.isEmpty() == false) {
            reinosselected.clear();
        }
        return "acontecimentos";
    }

    //Grupos-------
    public String irgrupos() {
        ativasusadaslist = new ArrayList(getPa().listaativas());
        racasusadaslist = new ArrayList(getPr().listaracas());
        gruposusadoslist = new ArrayList(getPg().listagrupos());
        gruposlist = new ListDataModel(getPg().listagrupos());
        grupos = new Grupos();
        reinosselected.clear();
        ativasselected.clear();
        return "grupos";
    }

    public String criagrupo() {
        grupos = new Grupos();
        oficialb = true;
        inoficialb = true;
        reinosselected.clear();
        return "criagrupo";
    }

    public String adicionargrupo() {
        grupos.setLocalidades(null);
        for (int c = 0; c < reinosselected.size(); c++) {
            if (grupos.getLocalidades() == null || grupos.getLocalidades() == "") {
                grupos.setLocalidades(reinosselected.get(c));
            } else {
                grupos.setLocalidades(grupos.getLocalidades() + "|" + reinosselected.get(c));
            }
        }
        if (reinosselected.isEmpty()) {
            grupos.setLocalidades("");
        }
        pg.salva(grupos);
        grupos = new Grupos();
        gruposlist = new ListDataModel(getPg().listagrupos());
        reinosselected.clear();
        oficialb = true;
        inoficialb = true;
        return "grupos";
    }

    public String editargrupo() {
        grupos = gruposlist.getRowData();
        if (grupos.getTipo().equals("Oficial")) {
            oficialb = false;
        }
        if (grupos.getTipo().equals("Inoficial")) {
            inoficialb = false;
        }
        char[] texto = grupos.getLocalidades().toCharArray();
        String reino = "";
        int c = 0;
        while (c < grupos.getLocalidades().length()) {
            while (texto[c] != '|') {
                reino = reino + texto[c];
                c++;
                if (c >= grupos.getLocalidades().length()) {
                    c--;
                    texto[c] = '|';
                }
            }
            reinosselected.add(reino);
            reino = "";
            c++;
        }
        return "criagrupo";
    }

    public void excluirgrupo() {
        pg.exclui(grupos);
        Personagens antigoparticipante = null;
        for (int y = 0; y < pper.listapersonagens().size(); y++) {
            if (pper.listapersonagens().get(y).getGrupo().contains(grupos.getNome())) {
                antigoparticipante = pper.listapersonagens().get(y);
            }
        }
        if (antigoparticipante != null) {
            String antigoport = "";
            while (cont < antigoparticipante.getGrupo().length()) {
                String port = achanastring(antigoparticipante.getGrupo().toCharArray(), cont);
                if (port.equals(grupos.getNome())) {
                } else {
                    if (antigoport == "") {
                        antigoport = port;
                    } else {
                        antigoport = antigoport + "|" + port;
                    }
                }
            }
            cont = 0;
            antigoparticipante.setGrupo(antigoport);
            pper.salva(antigoparticipante);
        }
        gruposlist = new ListDataModel(getPg().listagrupos());
    }

    public String voltargrupos() {
        grupos = new Grupos();
        gruposlist = new ListDataModel(getPg().listagrupos());
        oficialb = true;
        inoficialb = true;
        if (reinosselected.isEmpty() == false) {
            reinosselected.clear();
        }
        return "grupos";
    }

    public void oficial() {
        grupos.setTipo("Oficial");
        oficialb = false;
        inoficialb = true;
    }

    public void inoficial() {
        grupos.setTipo("Inoficial");
        inoficialb = false;
        oficialb = true;
    }

    //Personagens-------
    public String irpersonagens() { //revisar
        reinosselected.clear();
        ativasselected.clear();
        passivasselected.clear();
        personagenslist = new ListDataModel(getPper().listapersonagens());
        ativasusadaslist = new ArrayList(getPa().listaativas());
        racasusadaslist = new ArrayList(getPr().listaracas());
        gruposusadoslist = new ArrayList(getPg().listagrupos());
        passivasusadaslist = new ArrayList(getPp().listapassivasdisp("", ""));
        personagens = new Personagens();
        return "personagens";
    }

    public String criapersonagem() {
        personagens = new Personagens();
        for (int c = 0; c < racasusadaslist.size(); c++) {
            racasusadas.add(racasusadaslist.get(c).getNome());
        }
        for (int c = 0; c < getGruposusadoslist().size(); c++) {
            gruposusados.add(getGruposusadoslist().get(c).getNome());
        }
        for (int c = 0; c < getAtivasusadaslist().size(); c++) {
            getAtivasusadas().add(getAtivasusadaslist().get(c).getNome());
        }
        for (int c = 0; c < getPassivasusadaslist().size(); c++) {
            getPassivasusadas().add(getPassivasusadaslist().get(c).getNome());// Mudar
        }
        reinosselected.clear();
        ativasselected.clear();
        passivasselected.clear();
        return "criapersonagem";
    }

    public String adicionarpersonagem() {
        personagens.setLocalidade(reinosselected.get(0));
        personagens.setSkills(null);
        if (personagens.getPassivas() != null) {
            while (cont < personagens.getPassivas().length()) {
                String gado = achanastring(personagens.getPassivas().toCharArray(), cont);
                if (!passivasselected.contains(gado)) {
                    passivas = pp.localizaa(gado);
                    passivas.setPortador("");
                    pp.salva(passivas);
                    passivas = new Passivas();
                }
            }
            cont = 0;
        }
        personagens.setPassivas(null);
        if (ativasselected.isEmpty()) {
            personagens.setSkills("");
        }
        for (int c = 0; c < ativasselected.size(); c++) {
            if (personagens.getSkills() == null || personagens.getSkills() == "") {
                personagens.setSkills(ativasselected.get(c));
            } else {
                personagens.setSkills(personagens.getSkills() + "|" + ativasselected.get(c));
            }
        }
        if (ativasselected.isEmpty()) {
            personagens.setSkills("");
        }
        if (passivasselected.isEmpty()) {
            personagens.setPassivas("");
        }
        for (int c = 0; c < passivasselected.size(); c++) {
            if (personagens.getPassivas() == null || personagens.getPassivas() == "") {
                passivas = pp.localizaa(passivasselected.get(c));
                passivas.setPortador(personagens.getNome());
                pp.salva(passivas);
                passivas = new Passivas();
                personagens.setPassivas(passivasselected.get(c));
            } else {
                passivas = pp.localizaa(passivasselected.get(c));
                passivas.setPortador(personagens.getNome());
                pp.salva(passivas);
                passivas = new Passivas();
                personagens.setPassivas(personagens.getPassivas() + "|" + passivasselected.get(c));
            }
        }
        if (passivasselected.isEmpty()) {
            personagens.setPassivas("");
        }
        pper.salva(personagens);
        personagens = new Personagens();
        personagenslist = new ListDataModel(getPper().listapersonagens());
        ativasselected.clear();
        reinosselected.clear();
        passivasselected.clear();
        passivasusadas.clear();
        return "personagens";
    }

    public String editarpersonagem() {
        personagens = personagenslist.getRowData();
        passivasusadaslist = new ArrayList(getPp().listapassivasdisp("", personagens.getNome()));
        for (int c = 0; c < getPassivasusadaslist().size(); c++) {
            getPassivasusadas().add(getPassivasusadaslist().get(c).getNome());
        }
        racasusadas.clear();
        for (int c = 0; c < racasusadaslist.size(); c++) {
            racasusadas.add(racasusadaslist.get(c).getNome());
        }
        gruposusados.clear();
        for (int c = 0; c < getGruposusadoslist().size(); c++) {
            gruposusados.add(getGruposusadoslist().get(c).getNome());
        }
        ativasusadas.clear();
        for (int c = 0; c < getAtivasusadaslist().size(); c++) {
            getAtivasusadas().add(getAtivasusadaslist().get(c).getNome());
        }
        passivasusadas.clear();
        for (int c = 0; c < getPassivasusadaslist().size(); c++) {
            getPassivasusadas().add(getPassivasusadaslist().get(c).getNome());
        }

        if (personagens.getLocalidade() == null || personagens.getLocalidade() == "") {

        } else {
            while (cont < personagens.getLocalidade().length()) {
                reinosselected.add(achanastring(personagens.getLocalidade().toCharArray(), cont));
            }
            cont = 0;
        }

        if (personagens.getSkills() == null || personagens.getSkills() == "") {

        } else {
            while (cont < personagens.getSkills().length()) {
                ativasselected.add(achanastring(personagens.getSkills().toCharArray(), cont));
            }
            cont = 0;
        }

        if (personagens.getPassivas() == null || personagens.getPassivas() == "") {

        } else {
            while (cont < personagens.getPassivas().length()) {
                passivasselected.add(achanastring(personagens.getPassivas().toCharArray(), cont));
            }
            cont = 0;
        }

        return "criapersonagem";
    }

    public void excluirpersonagem() {
        pper.exclui(personagens);
        personagenslist = new ListDataModel(getPper().listapersonagens());
    }

    public String voltarpersonagens() {
        personagens = new Personagens();
        personagenslist = new ListDataModel(getPper().listapersonagens());
        if (reinosselected.isEmpty() == false) {
            reinosselected.clear();
        }
        if (ativasselected.isEmpty() == false) {
            ativasselected.clear();
        }
        if (passivasselected.isEmpty() == false) {
            passivasselected.clear();
        }
        return "personagens";
    }

    //---------
    public void onItemUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage();
        msg.setSummary("Item unselected: " + event.getObject().toString());
        msg.setSeverity(FacesMessage.SEVERITY_INFO);

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public String achanastring(char[] cha, int c) {
        char[] textopp = cha;
        String stringa = "";
        while (c < cha.length) {
            while (textopp[c] != '|') {
                stringa = stringa + textopp[c];
                c++;
                if (c >= cha.length) {
                    c--;
                    textopp[c] = '|';
                }
            }
            String ka = stringa;
            stringa = "";
            c++;
            cont = c;
            return ka;
        }
        return null;
    }

    /**
     * @return the ativas
     */
    public Ativas getAtivas() {
        return ativas;
    }

    /**
     * @param ativas the ativas to set
     */
    public void setAtivas(Ativas ativas) {
        this.ativas = ativas;
    }

    /**
     * @return the passivas
     */
    public Passivas getPassivas() {
        return passivas;
    }

    /**
     * @param passivas the passivas to set
     */
    public void setPassivas(Passivas passivas) {
        this.passivas = passivas;
    }

    /**
     * @return the ativaslist
     */
    public DataModel<Ativas> getAtivaslist() {
        return ativaslist;
    }

    /**
     * @param ativaslist the ativaslist to set
     */
    public void setAtivaslist(DataModel<Ativas> ativaslist) {
        this.ativaslist = ativaslist;
    }

    /**
     * @return the passivaslist
     */
    public DataModel<Passivas> getPassivaslist() {
        return passivaslist;
    }

    /**
     * @param passivaslist the passivaslist to set
     */
    public void setPassivaslist(DataModel<Passivas> passivaslist) {
        this.passivaslist = passivaslist;
    }

    /**
     * @return the pa
     */
    public AtivasDAO getPa() {
        return pa;
    }

    /**
     * @param pa the pa to set
     */
    public void setPa(AtivasDAO pa) {
        this.pa = pa;
    }

    /**
     * @return the pp
     */
    public PassivasDAO getPp() {
        return pp;
    }

    /**
     * @param pp the pp to set
     */
    public void setPp(PassivasDAO pp) {
        this.pp = pp;
    }

    /**
     * @return the forca
     */
    public String getForca() {
        return forca;
    }

    /**
     * @param forca the forca to set
     */
    public void setForca(String forca) {
        this.forca = forca;
    }

    /**
     * @return the constituicao
     */
    public String getConstituicao() {
        return constituicao;
    }

    /**
     * @param constituicao the constituicao to set
     */
    public void setConstituicao(String constituicao) {
        this.constituicao = constituicao;
    }

    /**
     * @return the destreza
     */
    public String getDestreza() {
        return destreza;
    }

    /**
     * @param destreza the destreza to set
     */
    public void setDestreza(String destreza) {
        this.destreza = destreza;
    }

    /**
     * @return the sabedoria
     */
    public String getSabedoria() {
        return sabedoria;
    }

    /**
     * @param sabedoria the sabedoria to set
     */
    public void setSabedoria(String sabedoria) {
        this.sabedoria = sabedoria;
    }

    /**
     * @return the inteligencia
     */
    public String getInteligencia() {
        return inteligencia;
    }

    /**
     * @param inteligencia the inteligencia to set
     */
    public void setInteligencia(String inteligencia) {
        this.inteligencia = inteligencia;
    }

    /**
     * @return the carisma
     */
    public String getCarisma() {
        return carisma;
    }

    /**
     * @param carisma the carisma to set
     */
    public void setCarisma(String carisma) {
        this.carisma = carisma;
    }

    /**
     * @return the classes
     */
    public List<String> getClasses() {
        return classes;
    }

    /**
     * @param classes the classes to set
     */
    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

    /**
     * @return the ranksativas
     */
    public List<String> getRanksativas() {
        return ranksativas;
    }

    /**
     * @param ranksativas the ranksativas to set
     */
    public void setRanksativas(List<String> ranksativas) {
        this.ranksativas = ranksativas;
    }

    /**
     * @return the rankspassivas
     */
    public List<String> getRankspassivas() {
        return rankspassivas;
    }

    /**
     * @param rankspassivas the rankspassivas to set
     */
    public void setRankspassivas(List<String> rankspassivas) {
        this.rankspassivas = rankspassivas;
    }

    /**
     * @return the acontecimentos
     */
    public Acontecimentos getAcontecimentos() {
        return acontecimentos;
    }

    /**
     * @param acontecimentos the acontecimentos to set
     */
    public void setAcontecimentos(Acontecimentos acontecimentos) {
        this.acontecimentos = acontecimentos;
    }

    /**
     * @return the grupos
     */
    public Grupos getGrupos() {
        return grupos;
    }

    /**
     * @param grupos the grupos to set
     */
    public void setGrupos(Grupos grupos) {
        this.grupos = grupos;
    }

    /**
     * @return the itens
     */
    public Itens getItens() {
        return itens;
    }

    /**
     * @param itens the itens to set
     */
    public void setItens(Itens itens) {
        this.itens = itens;
    }

    /**
     * @return the personagens
     */
    public Personagens getPersonagens() {
        return personagens;
    }

    /**
     * @param personagens the personagens to set
     */
    public void setPersonagens(Personagens personagens) {
        this.personagens = personagens;
    }

    /**
     * @return the racas
     */
    public Racas getRacas() {
        return racas;
    }

    /**
     * @param racas the racas to set
     */
    public void setRacas(Racas racas) {
        this.racas = racas;
    }

    /**
     * @return the pac
     */
    public AcontecimentosDAO getPac() {
        return pac;
    }

    /**
     * @param pac the pac to set
     */
    public void setPac(AcontecimentosDAO pac) {
        this.pac = pac;
    }

    /**
     * @return the pg
     */
    public GruposDAO getPg() {
        return pg;
    }

    /**
     * @param pg the pg to set
     */
    public void setPg(GruposDAO pg) {
        this.pg = pg;
    }

    /**
     * @return the pi
     */
    public ItensDAO getPi() {
        return pi;
    }

    /**
     * @param pi the pi to set
     */
    public void setPi(ItensDAO pi) {
        this.pi = pi;
    }

    /**
     * @return the pper
     */
    public PersonagensDAO getPper() {
        return pper;
    }

    /**
     * @param pper the pper to set
     */
    public void setPper(PersonagensDAO pper) {
        this.pper = pper;
    }

    /**
     * @return the pr
     */
    public RacasDAO getPr() {
        return pr;
    }

    /**
     * @param pr the pr to set
     */
    public void setPr(RacasDAO pr) {
        this.pr = pr;
    }

    /**
     * @return the acontecimentoslist
     */
    public DataModel<Acontecimentos> getAcontecimentoslist() {
        return acontecimentoslist;
    }

    /**
     * @param acontecimentoslist the acontecimentoslist to set
     */
    public void setAcontecimentoslist(DataModel<Acontecimentos> acontecimentoslist) {
        this.acontecimentoslist = acontecimentoslist;
    }

    /**
     * @return the gruposlist
     */
    public DataModel<Grupos> getGruposlist() {
        return gruposlist;
    }

    /**
     * @param gruposlist the gruposlist to set
     */
    public void setGruposlist(DataModel<Grupos> gruposlist) {
        this.gruposlist = gruposlist;
    }

    /**
     * @return the itenslist
     */
    public DataModel<Itens> getItenslist() {
        return itenslist;
    }

    /**
     * @param itenslist the itenslist to set
     */
    public void setItenslist(DataModel<Itens> itenslist) {
        this.itenslist = itenslist;
    }

    /**
     * @return the personagenslist
     */
    public DataModel<Personagens> getPersonagenslist() {
        return personagenslist;
    }

    /**
     * @param personagenslist the personagenslist to set
     */
    public void setPersonagenslist(DataModel<Personagens> personagenslist) {
        this.personagenslist = personagenslist;
    }

    /**
     * @return the racaslist
     */
    public DataModel<Racas> getRacaslist() {
        return racaslist;
    }

    /**
     * @param racaslist the racaslist to set
     */
    public void setRacaslist(DataModel<Racas> racaslist) {
        this.racaslist = racaslist;
    }

    /**
     * @return the reinos
     */
    public List<String> getReinos() {
        return reinos;
    }

    /**
     * @param reinos the reinos to set
     */
    public void setReinos(List<String> reinos) {
        this.reinos = reinos;
    }

    /**
     * @return the reinosselected
     */
    public List<String> getReinosselected() {
        return reinosselected;
    }

    /**
     * @param reinosselected the reinosselected to set
     */
    public void setReinosselected(List<String> reinosselected) {
        this.reinosselected = reinosselected;
    }

    /**
     * @return the oficialb
     */
    public Boolean getOficialb() {
        return oficialb;
    }

    /**
     * @param oficialb the oficial to set
     */
    public void setOficialb(Boolean oficialb) {
        this.oficialb = oficialb;
    }

    /**
     * @return the inoficialb
     */
    public Boolean getInoficialb() {
        return inoficialb;
    }

    /**
     * @param inoficialb the inoficial to set
     */
    public void setInoficialb(Boolean inoficialb) {
        this.inoficialb = inoficialb;
    }

    /**
     * @return the racasusadas
     */
    public List<String> getRacasusadas() {
        return racasusadas;
    }

    /**
     * @param racasusadas the racasusadas to set
     */
    public void setRacasusadas(List<String> racasusadas) {
        this.racasusadas = racasusadas;
    }

    /**
     * @return the racasusadaslist
     */
    public List<Racas> getRacasusadaslist() {
        return racasusadaslist;
    }

    /**
     * @param racasusadaslist the racasusadaslist to set
     */
    public void setRacasusadaslist(List<Racas> racasusadaslist) {
        this.racasusadaslist = racasusadaslist;
    }

    /**
     * @return the gruposusados
     */
    public List<String> getGruposusados() {
        return gruposusados;
    }

    /**
     * @param gruposusados the gruposusados to set
     */
    public void setGruposusados(List<String> gruposusados) {
        this.gruposusados = gruposusados;
    }

    /**
     * @return the ativasselected
     */
    public List<String> getAtivasselected() {
        return ativasselected;
    }

    /**
     * @param ativasselected the ativasselected to set
     */
    public void setAtivasselected(List<String> ativasselected) {
        this.ativasselected = ativasselected;
    }

    /**
     * @return the ativasusadas
     */
    public List<String> getAtivasusadas() {
        return ativasusadas;
    }

    /**
     * @param ativasusadas the ativasusadas to set
     */
    public void setAtivasusadas(List<String> ativasusadas) {
        this.ativasusadas = ativasusadas;
    }

    /**
     * @return the gruposusadoslist
     */
    public List<Grupos> getGruposusadoslist() {
        return gruposusadoslist;
    }

    /**
     * @param gruposusadoslist the gruposusadoslist to set
     */
    public void setGruposusadoslist(List<Grupos> gruposusadoslist) {
        this.gruposusadoslist = gruposusadoslist;
    }

    /**
     * @return the ativasusadaslist
     */
    public List<Ativas> getAtivasusadaslist() {
        return ativasusadaslist;
    }

    /**
     * @param ativasusadaslist the ativasusadaslist to set
     */
    public void setAtivasusadaslist(List<Ativas> ativasusadaslist) {
        this.ativasusadaslist = ativasusadaslist;
    }

    /**
     * @return the personagensusadaslist
     */
    public List<Personagens> getPersonagensusadaslist() {
        return personagensusadaslist;
    }

    /**
     * @param personagensusadaslist the personagensusadaslist to set
     */
    public void setPersonagensusadaslist(List<Personagens> personagensusadaslist) {
        this.personagensusadaslist = personagensusadaslist;
    }

    /**
     * @return the personagensusadas
     */
    public List<String> getPersonagensusadas() {
        return personagensusadas;
    }

    /**
     * @param personagensusadas the personagensusadas to set
     */
    public void setPersonagensusadas(List<String> personagensusadas) {
        this.personagensusadas = personagensusadas;
    }

    /**
     * @return the passivasselected
     */
    public List<String> getPassivasselected() {
        return passivasselected;
    }

    /**
     * @param passivasselected the passivasselected to set
     */
    public void setPassivasselected(List<String> passivasselected) {
        this.passivasselected = passivasselected;
    }

    /**
     * @return the passivasusadaslist
     */
    public List<Passivas> getPassivasusadaslist() {
        return passivasusadaslist;
    }

    /**
     * @param passivasusadaslist the passivasusadaslist to set
     */
    public void setPassivasusadaslist(List<Passivas> passivasusadaslist) {
        this.passivasusadaslist = passivasusadaslist;
    }

    /**
     * @return the passivasusadas
     */
    public List<String> getPassivasusadas() {
        return passivasusadas;
    }

    /**
     * @param passivasusadas the passivasusadas to set
     */
    public void setPassivasusadas(List<String> passivasusadas) {
        this.passivasusadas = passivasusadas;
    }

    /**
     * @return the cont
     */
    public int getCont() {
        return cont;
    }

    /**
     * @param cont the cont to set
     */
    public void setCont(int cont) {
        this.cont = cont;
    }

}
