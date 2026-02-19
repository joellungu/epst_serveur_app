package org.epst.beans;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.persistence.Entity;


@Entity
public class MessageBean
        extends PanacheEntity
        {

    private String fromt;
    private String tot;
    private String contentt;
    private String hostIdt;
    private String clientIdt;
    private Boolean closet;
    private Boolean allst;
    private String visiblet;
    private Boolean conversationt;
    private String matriculet;
    private String datet;
    private String heuret;

    //
            public MessageBean(){}
            public MessageBean(String fromt, String tot, String contentt, String hostIdt,
                               String clientIdt, Boolean closet, Boolean allst, String visiblet,
                               Boolean conversationt, String matriculet, String datet, String heuret) {
                this.fromt = fromt;
                this.tot = tot;
                this.contentt = contentt;
                this.hostIdt = hostIdt;
                this.clientIdt = clientIdt;
                this.closet = closet;
                this.allst = allst;
                this.visiblet = visiblet;
                this.conversationt = conversationt;
                this.matriculet = matriculet;
                this.datet = datet;
                this.heuret = heuret;
            }
    // Getters
    public String getFromt() {
        return fromt;
    }

            public String getTot() {
                return tot;
            }

            public String getContentt() {
                return contentt;
            }

            public String getHostIdt() {
                return hostIdt;
            }

            public String getClientIdt() {
                return clientIdt;
            }

            public Boolean getCloset() {
                return closet;
            }

            public Boolean getAllst() {
                return allst;
            }

            public String getVisiblet() {
                return visiblet;
            }

            public Boolean getConversationt() {
                return conversationt;
            }

            public String getMatriculet() {
                return matriculet;
            }

            public String getDatet() {
                return datet;
            }

            public String getHeuret() {
                return heuret;
            }

            // Setters
            public void setFromt(String fromt) {
                this.fromt = fromt;
            }

            public void setTot(String tot) {
                this.tot = tot;
            }

            public void setContentt(String contentt) {
                this.contentt = contentt;
            }

            public void setHostIdt(String hostIdt) {
                this.hostIdt = hostIdt;
            }

            public void setClientIdt(String clientIdt) {
                this.clientIdt = clientIdt;
            }

            public void setCloset(Boolean closet) {
                this.closet = closet;
            }

            public void setAllst(Boolean allst) {
                this.allst = allst;
            }

            public void setVisiblet(String visiblet) {
                this.visiblet = visiblet;
            }

            public void setConversationt(Boolean conversationt) {
                this.conversationt = conversationt;
            }

            public void setMatriculet(String matriculet) {
                this.matriculet = matriculet;
            }

            public void setDatet(String datet) {
                this.datet = datet;
            }

            public void setHeuret(String heuret) {
                this.heuret = heuret;
            }

}
